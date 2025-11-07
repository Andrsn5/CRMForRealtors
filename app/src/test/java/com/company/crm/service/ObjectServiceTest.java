package com.company.crm.service;

import com.company.crm.config.AppConfig;
import com.company.crm.model.Object;
import com.company.crm.service.interfaces.ObjectService;
import com.company.crm.util.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObjectServiceTest {
    private ObjectService objectService;
    private AppConfig appConfig;

    @BeforeEach
    void setUp() {
        appConfig = new AppConfig();
        objectService = appConfig.getObjectService();
    }

    @Test
    void testCreateObject_WithAllFields_ShouldSuccess() {
        Object obj = createTestObject("Luxury Apartment", "Apartment", "Sale");

        Object created = objectService.create(obj);

        assertNotNull(created);
        assertEquals("Apartment", created.getObjectType());
        assertEquals("Sale", created.getDealType());
        assertEquals(0, new BigDecimal("250000.00").compareTo(created.getPrice()));
        assertEquals(3, created.getRooms());
    }

    @Test
    void testCreateObject_WithZeroPrice_ShouldThrowValidationException() {
        Object obj = createTestObject("Zero Price House", "House", "Rent");
        obj.setPrice(BigDecimal.ZERO);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> objectService.create(obj));

        assertTrue(exception.getMessage().contains("Price must be at least 1000"));
    }

    @Test
    void testCreateObject_WithNullRequiredFields_ShouldThrowValidationException() {
        Object obj = new Object();
        obj.setTitle("Test Object");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> objectService.create(obj));

        assertTrue(exception.getMessage().contains("is required"));
    }

    @Test
    void testCreateObject_WithOnlyRequiredFields_ShouldSuccess() {
        Object obj = new Object();
        obj.setTitle("Unique Test Property " + System.currentTimeMillis());
        obj.setObjectType("Apartment");
        obj.setDealType("Sale");
        obj.setStatus("Available");
        obj.setPrice(new BigDecimal("150000.00")); // Price теперь обязателен

        Object created = objectService.create(obj);

        assertNotNull(created);
        assertEquals("Apartment", created.getObjectType());
        assertEquals("Sale", created.getDealType());
        assertEquals("Available", created.getStatus());
        assertNotNull(created.getPrice());
    }

    @Test
    void testCreateObject_WithNullOptionalNumericFields_ShouldSuccess() {
        Object obj = new Object();
        obj.setTitle("Optional Fields Property " + System.currentTimeMillis());
        obj.setObjectType("Apartment");
        obj.setDealType("Sale");
        obj.setStatus("Available");
        obj.setPrice(new BigDecimal("200000.00"));
        // Area, rooms, bathrooms остаются null - это допустимо

        Object created = objectService.create(obj);

        assertNotNull(created);
        assertNull(created.getArea());
        assertNull(created.getRooms());
        assertNull(created.getBathrooms());
    }

    @Test
    void testUpdateObject_ChangeStatus_ShouldUpdate() {
        Object obj = objectService.create(createTestObject("Update Test " + System.currentTimeMillis(), "Apartment", "Sale"));
        obj.setStatus("Sold");

        objectService.update(obj);
        Object updated = objectService.getById(obj.getId()).orElse(null);

        assertNotNull(updated);
        assertEquals("Sold", updated.getStatus());
    }

    @Test
    void testFindObjects_ByMultipleTypes_ShouldReturnCorrect() {
        int initialCount = objectService.getAll().size();

        objectService.create(createTestObject("Test Apt " + System.currentTimeMillis(), "Apartment", "Sale"));
        objectService.create(createTestObject("Test House " + System.currentTimeMillis(), "House", "Rent"));
        objectService.create(createTestObject("Test Commercial " + System.currentTimeMillis(), "Commercial", "Sale"));

        List<Object> allObjects = objectService.getAll();

        assertEquals(initialCount + 3, allObjects.size());
    }

    @Test
    void testCreateObject_WithInvalidObjectType_ShouldThrowValidationException() {
        Object obj = createTestObject("Invalid Type Test", "InvalidType", "Sale");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> objectService.create(obj));

        assertTrue(exception.getMessage().contains("Invalid object type"));
    }

    @Test
    void testCreateObject_WithInvalidDealType_ShouldThrowValidationException() {
        Object obj = createTestObject("Invalid Deal Test", "Apartment", "InvalidDeal");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> objectService.create(obj));

        assertTrue(exception.getMessage().contains("Invalid deal type"));
    }

    @Test
    void testCreateObject_WithValidObjectTypes_ShouldSuccess() {
        String[] validTypes = {"Apartment", "House", "Commercial", "Land", "Villa"};

        for (String objectType : validTypes) {
            Object obj = createTestObject("Test " + objectType + " " + System.currentTimeMillis(), objectType, "Sale");
            assertDoesNotThrow(() -> objectService.create(obj));
        }
    }

    @Test
    void testCreateObject_WithValidDealTypes_ShouldSuccess() {
        String[] validDealTypes = {"Sale", "Rent"};

        for (String dealType : validDealTypes) {
            Object obj = createTestObject("Test Deal " + dealType + " " + System.currentTimeMillis(), "Apartment", dealType);
            assertDoesNotThrow(() -> objectService.create(obj));
        }
    }

    @Test
    void testCreateObject_WithNegativePrice_ShouldThrowValidationException() {
        Object obj = createTestObject("Negative Price Test", "Apartment", "Sale");
        obj.setPrice(new BigDecimal("-1000"));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> objectService.create(obj));

        assertEquals("Price must be positive", exception.getMessage());
    }

    @Test
    void testCreateObject_WithMinPrice_ShouldSuccess() {
        Object obj = createTestObject("Min Price Test", "Apartment", "Sale");
        obj.setPrice(new BigDecimal("1000"));

        assertDoesNotThrow(() -> objectService.create(obj));
    }

    @Test
    void testCreateObject_WithNegativeArea_ShouldThrowValidationException() {
        Object obj = createTestObject("Negative Area Test", "Apartment", "Sale");
        obj.setArea(new BigDecimal("-10"));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> objectService.create(obj));

        assertEquals("Area must be positive", exception.getMessage());
    }

    @Test
    void testCreateObject_WithMinArea_ShouldSuccess() {
        Object obj = createTestObject("Min Area Test", "Apartment", "Sale");
        obj.setArea(new BigDecimal("1"));

        assertDoesNotThrow(() -> objectService.create(obj));
    }

    @Test
    void testCreateObject_WithExcessiveRooms_ShouldThrowValidationException() {
        Object obj = createTestObject("Many Rooms Test", "Apartment", "Sale");
        obj.setRooms(51); // Максимум 50 по текущей валидации

        ValidationException exception = assertThrows(ValidationException.class,
                () -> objectService.create(obj));

        assertTrue(exception.getMessage().contains("Rooms must be between 0 and 50"));
    }

    @Test
    void testCreateObject_WithExcessiveBathrooms_ShouldThrowValidationException() {
        Object obj = createTestObject("Many Bathrooms Test", "Apartment", "Sale");
        obj.setBathrooms(21); // Максимум 20 по текущей валидации

        ValidationException exception = assertThrows(ValidationException.class,
                () -> objectService.create(obj));

        assertTrue(exception.getMessage().contains("Bathrooms must be between 0 and 20"));
    }

    @Test
    void testCreateObject_WithDuplicateTitle_ShouldThrowValidationException() {
        String uniqueTitle = "Duplicate Title Test " + System.currentTimeMillis();
        Object obj1 = createTestObject(uniqueTitle, "Apartment", "Sale");
        objectService.create(obj1);

        Object obj2 = createTestObject(uniqueTitle, "House", "Rent");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> objectService.create(obj2));

        assertTrue(exception.getMessage().contains("already exists"));
    }

    @Test
    void testCreateObject_WithValidStatuses_ShouldSuccess() {
        String[] validStatuses = {"Available", "Sold", "Rented", "Under Contract", "Reserved", "Draft"};

        for (String status : validStatuses) {
            Object obj = createTestObject("Status Test " + status + " " + System.currentTimeMillis(), "Apartment", "Sale");
            obj.setStatus(status);
            assertDoesNotThrow(() -> objectService.create(obj),
                    "Создание объекта со статусом '" + status + "' должно работать");
        }
    }



    private Object createTestObject(String title, String objectType, String dealType) {
        Object obj = new Object();
        obj.setTitle(title);
        obj.setDescription("Test description for " + title);
        obj.setObjectType(objectType);
        obj.setDealType(dealType);
        obj.setPrice(new BigDecimal("250000.00"));
        obj.setAddress("123 Test Street");
        obj.setArea(new BigDecimal("120.5"));
        obj.setRooms(3);
        obj.setBathrooms(2);
        obj.setStatus("Available");
        return obj;
    }
}