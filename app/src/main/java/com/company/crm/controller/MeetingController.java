package com.company.crm.controller;

import com.company.crm.model.Meeting;
import com.company.crm.service.interfaces.MeetingService;
import com.company.crm.util.ConsoleHelper;

import java.time.LocalDateTime;
import java.util.List;

public class MeetingController {
    private final MeetingService service;

    public MeetingController(MeetingService service) {
        this.service = service;
        seedData();
    }

    private void seedData() {
        if (service.getAll().isEmpty()) {
            for (int i = 1; i <= 5; i++) {
                Meeting meeting = new Meeting();
                meeting.setTitle("Meeting " + i);
                meeting.setMeetingDate(LocalDateTime.now().plusDays(i).plusHours(i));
                meeting.setLocation("Office " + i);
                meeting.setNotes("Discussion about property " + i);
                meeting.setClientId(i);
                meeting.setTaskId(i);
                meeting.setStatus("Scheduled");
                service.create(meeting);
            }
        }
    }

    public void menu() {
        while (true) {
            System.out.println("\n=== MEETING MANAGEMENT ===");
            System.out.println("1. List 2. Create 3. Edit 4. Delete 0. Back");
            int choice = ConsoleHelper.askInt("Choose option");
            switch (choice) {
                case 1 -> list();
                case 2 -> create();
                case 3 -> edit();
                case 4 -> delete();
                case 0 -> { return; }
                default -> System.out.println("❌ Invalid option");
            }
        }
    }

    private void list() {
        List<Meeting> all = service.getAll();
        if (all.isEmpty()) {
            System.out.println("No meetings found");
        } else {
            all.forEach(System.out::println);
        }
    }

    private void create() {
        String title = ConsoleHelper.ask("Meeting Title");
        String dateStr = ConsoleHelper.ask("Date (YYYY-MM-DD)");
        String timeStr = ConsoleHelper.ask("Time (HH:MM)");
        String location = ConsoleHelper.ask("Location");

        Meeting meeting = new Meeting();
        meeting.setTitle(title);

        try {
            LocalDateTime meetingDate = LocalDateTime.parse(dateStr + "T" + timeStr + ":00");
            meeting.setMeetingDate(meetingDate);
        } catch (Exception e) {
            System.out.println("❌ Invalid date/time format");
            return;
        }

        meeting.setLocation(location);
        meeting.setNotes(ConsoleHelper.ask("Notes"));
        meeting.setStatus("Scheduled");
        service.create(meeting);
        System.out.println("✅ Meeting scheduled: " + meeting);
        ConsoleHelper.pause();
    }

    private void edit() {
        int id = ConsoleHelper.askInt("Enter meeting ID");
        service.getById(id).ifPresentOrElse(meeting -> {
            meeting.setTitle(ConsoleHelper.ask("Title (" + meeting.getTitle() + ")"));
            meeting.setLocation(ConsoleHelper.ask("Location (" + meeting.getLocation() + ")"));
            meeting.setStatus(ConsoleHelper.ask("Status (" + meeting.getStatus() + ")"));
            service.update(meeting);
            System.out.println("✅ Meeting updated: " + meeting);
        }, () -> System.out.println("❌ Meeting not found."));
        ConsoleHelper.pause();
    }

    private void delete() {
        int id = ConsoleHelper.askInt("Enter meeting ID to delete");
        service.delete(id);
        System.out.println("✅ Meeting deleted");
        ConsoleHelper.pause();
    }
}