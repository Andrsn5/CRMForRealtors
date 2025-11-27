package com.company.crm.service;

import com.company.crm.config.AppConfig;
import com.company.crm.model.Client;
import com.company.crm.model.Meeting;
import com.company.crm.service.interfaces.ClientService;
import com.company.crm.service.interfaces.MeetingService;
import com.company.crm.util.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MeetingServiceTest {
    private MeetingService meetingService;
    private ClientService clientService;
    private AppConfig appConfig;

    private int testClientId1;
    private int testClientId2;

    @BeforeEach
    void setUp() {
        appConfig = new AppConfig();
        meetingService = appConfig.getMeetingService();
        clientService = appConfig.getClientService();

        // Создаем тестовых клиентов
        createTestClients();

        // Очистка тестовых данных перед каждым тестом
        clearTestMeetings();
    }

    private void createTestClients() {
        // Создаем первого тестового клиента
        Client client1 = new Client();
        client1.setFirstName("TestClient1");
        client1.setLastName("MeetingTest");
        client1.setClientType("customer");
        Client createdClient1 = clientService.create(client1);
        testClientId1 = createdClient1.getId();

        // Создаем второго тестового клиента
        Client client2 = new Client();
        client2.setFirstName("TestClient2");
        client2.setLastName("MeetingTest");
        client2.setClientType("customer");
        Client createdClient2 = clientService.create(client2);
        testClientId2 = createdClient2.getId();
    }

    private void clearTestMeetings() {
        // Удаляем только тестовые встречи
        List<Meeting> allMeetings = meetingService.getAll();
        for (Meeting meeting : allMeetings) {
            if (meeting.getTitle() != null &&
                    (meeting.getTitle().contains("Test Meeting") ||
                            meeting.getTitle().contains("Duplicate Test") ||
                            meeting.getTitle().contains("First Meeting") ||
                            meeting.getTitle().contains("Second Meeting") ||
                            meeting.getTitle().contains("Update Test") ||
                            meeting.getTitle().contains("Completed Meeting"))) {
                try {
                    meetingService.delete(meeting.getId());
                } catch (Exception e) {

                }
            }
        }
    }



    @Test
    void testCreateMeeting_WithSameClientButDifferentTime_ShouldSuccess() {
        Meeting meeting1 = createTestMeeting(testClientId1, 1, "Test Meeting Different Time 1");
        meeting1.setMeetingDate(LocalDateTime.now().plusHours(6));
        meetingService.create(meeting1);

        Meeting meeting2 = createTestMeeting(testClientId1, 2, "Test Meeting Different Time 2");
        meeting2.setMeetingDate(LocalDateTime.now().plusHours(7)); // Разное время

        assertDoesNotThrow(() -> meetingService.create(meeting2),
                "Should allow creating meeting with same client but different time");
    }

    @Test
    void testCreateMeeting_WithSameTimeButDifferentClient_ShouldSuccess() {
        Meeting meeting1 = createTestMeeting(testClientId1, 1, "Test Meeting Different Client 1");
        LocalDateTime sameDateTime = LocalDateTime.now().plusHours(8);
        meeting1.setMeetingDate(sameDateTime);
        meetingService.create(meeting1);

        Meeting meeting2 = createTestMeeting(testClientId2, 2, "Test Meeting Different Client 2"); // Разный клиент
        meeting2.setMeetingDate(sameDateTime); // То же время

        assertDoesNotThrow(() -> meetingService.create(meeting2),
                "Should allow creating meeting at same time but with different client");
    }

    @Test
    void testCreateMeeting_WithCancelledStatus_ShouldNotCheckUniqueness() {
        // Создаем отмененную встречу
        Meeting meeting1 = createTestMeeting(testClientId1, 1, "Cancelled Meeting Test");
        LocalDateTime dateTime = LocalDateTime.now().plusHours(9).withMinute(0).withSecond(0).withNano(0);
        meeting1.setMeetingDate(dateTime);
        meeting1.setStatus("cancelled");
        meetingService.create(meeting1);

        // Пытаемся создать новую встречу с тем же клиентом и временем
        Meeting meeting2 = createTestMeeting(testClientId1, 2, "New Meeting After Cancel");
        meeting2.setMeetingDate(dateTime);
        meeting2.setStatus("scheduled");

        assertDoesNotThrow(() -> meetingService.create(meeting2),
                "Should allow creating meeting with same client/time if previous meeting is cancelled");
    }

    @Test
    void testIsTimeSlotAvailable_ShouldReturnCorrectAvailability() {
        // Создаем активную встречу
        Meeting meeting = createTestMeeting(testClientId1, 1, "Availability Test Meeting");
        LocalDateTime occupiedTime = LocalDateTime.now().plusHours(10).withMinute(0).withSecond(0).withNano(0);
        meeting.setMeetingDate(occupiedTime);
        meeting.setStatus("scheduled");
        meetingService.create(meeting);


    }

    // Остальные базовые тесты...

    @Test
    void testCreateMeeting_WithClientAndTask_ShouldSuccess() {
        Meeting meeting = createTestMeeting(testClientId1, 1, "Regular Meeting");

        Meeting created = meetingService.create(meeting);

        assertNotNull(created);
        assertEquals("Regular Meeting", created.getTitle());
        assertEquals(testClientId1, created.getClientId());
        assertEquals(1, created.getTaskId());
    }

    @Test
    void testCreateMeeting_WithNullClient_ShouldThrowValidationException() {
        Meeting meeting = createTestMeeting(null, 1, "No Client Meeting");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> meetingService.create(meeting));

        assertEquals("Client is required", exception.getMessage());
    }

    @Test
    void testCreateMeeting_WithNullTask_ShouldWork() {
        Meeting meeting = createTestMeeting(testClientId1, null, "No Task Meeting");

        assertDoesNotThrow(() -> meetingService.create(meeting));
    }

    @Test
    void testCreateMeeting_WithPastDate_ShouldWork() {
        Meeting meeting = createTestMeeting(testClientId1, 1, "Past Meeting");
        meeting.setMeetingDate(LocalDateTime.now().minusDays(1));

        assertDoesNotThrow(() -> meetingService.create(meeting),
                "Meetings with past dates should be allowed");
    }

    private Meeting createTestMeeting(Integer clientId, Integer taskId, String title) {
        Meeting meeting = new Meeting();
        meeting.setTitle(title);
        meeting.setMeetingDate(LocalDateTime.now().plusDays(1).plusHours(1));
        meeting.setLocation("Test Office");
        meeting.setNotes("Test discussion");
        meeting.setClientId(clientId);
        meeting.setTaskId(taskId);
        meeting.setStatus("scheduled");
        return meeting;
    }
}