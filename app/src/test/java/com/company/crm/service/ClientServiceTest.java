package com.company.crm.service;

import com.company.crm.config.AppConfig;
import com.company.crm.model.Client;
import com.company.crm.service.interfaces.ClientService;
import com.company.crm.util.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ClientServiceTest {
    private ClientService clientService;
    private AppConfig appConfig;

    @BeforeEach
    void setUp() {
        appConfig = new AppConfig();
        clientService = appConfig.getClientService();
    }

    @Test
    void testCreateClient_WithBudget_ShouldSuccess() {
        Client client = createTestClient("customer", new BigDecimal("150000.00"));

        Client created = clientService.create(client);

        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("customer", created.getClientType());
        assertEquals(0, new BigDecimal("150000.00").compareTo(created.getBudget()));
    }

    @Test
    void testCreateClient_WithNullBudget_ShouldWork() {
        Client client = createTestClient("seller", null);

        Client created = clientService.create(client);

        assertNotNull(created);
        assertNull(created.getBudget());
    }

    @Test
    void testCreateClient_WithNegativeBudget_ShouldThrowValidationException() {
        Client client = createTestClient("customer", new BigDecimal("-1000.00"));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> clientService.create(client));

        assertTrue(exception.getMessage().contains("Budget must be positive"));
    }

    @Test
    void testUpdateClient_ChangeClientType_ShouldUpdate() {
        Client client = clientService.create(createTestClient("customer", new BigDecimal("100000")));
        client.setClientType("seller");

        clientService.update(client);
        Optional<Client> updated = clientService.getById(client.getId());

        assertTrue(updated.isPresent());
        assertEquals("seller", updated.get().getClientType());
    }

    @Test
    void testFindAll_ClientsWithDifferentTypes_ShouldReturnAll() {
        clientService.create(createTestClient("customer", new BigDecimal("100000")));
        clientService.create(createTestClient("seller", new BigDecimal("200000")));
        clientService.create(createTestClient("tenant", new BigDecimal("50000")));

        List<Client> allClients = clientService.getAll();

        assertTrue(allClients.size() >= 3);
    }

    @Test
    void testDeleteClient_ThenTryToFind_ShouldBeEmpty() {
        Client client = clientService.create(createTestClient("customer", new BigDecimal("100000")));
        int clientId = client.getId();

        clientService.delete(clientId);
        Optional<Client> found = clientService.getById(clientId);

        assertTrue(found.isEmpty());
    }

    @Test
    void testClientWithNullRequiredFields_ShouldThrowValidationException() {
        Client client = new Client();
        client.setFirstName(null);
        client.setLastName(null);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> clientService.create(client));

        assertEquals("First name is required", exception.getMessage());
    }

    @Test
    void testCreateClient_WithZeroBudget_ShouldSuccess() {
        Client client = createTestClient("customer", BigDecimal.ZERO);

        Client created = clientService.create(client);

        assertNotNull(created);
        assertEquals(0, BigDecimal.ZERO.compareTo(created.getBudget()));
    }

    @Test
    void testCreateClient_WithInvalidClientType_ShouldThrowValidationException() {
        Client client = createTestClient("InvalidType", new BigDecimal("100000"));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> clientService.create(client));

        assertTrue(exception.getMessage().contains("Invalid client type"));
    }

    @Test
    void testCreateClient_WithValidClientTypes_ShouldSuccess() {
        String[] validTypes = {"customer", "seller", "tenant", "landlord"};

        for (String clientType : validTypes) {
            Client client = createTestClient(clientType, new BigDecimal("100000"));
            assertDoesNotThrow(() -> clientService.create(client));
        }
    }

    @Test
    void testCreateClient_WithOnlyRequiredFields_ShouldSuccess() {
        Client client = new Client();
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setClientType("customer");

        Client created = clientService.create(client);

        assertNotNull(created);
        assertEquals("John", created.getFirstName());
        assertEquals("Doe", created.getLastName());
        assertEquals("customer", created.getClientType());
    }

    private Client createTestClient(String clientType, BigDecimal budget) {
        Client client = new Client();
        client.setFirstName("Test");
        client.setLastName("Client");
        client.setEmail("test@client.com");
        client.setPhone("555-1234");
        client.setClientType(clientType);
        client.setBudget(budget);
        client.setNotes("Test notes");
        return client;
    }
}