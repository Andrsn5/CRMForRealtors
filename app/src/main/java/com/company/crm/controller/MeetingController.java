package com.company.crm.controller;

import com.company.crm.model.Meeting;
import com.company.crm.service.interfaces.MeetingService;
import com.company.crm.util.ConsoleHelper;
import com.company.crm.util.ValidationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class MeetingController {
    private final MeetingService service;

    public MeetingController(MeetingService service) {
        this.service = service;
    }

    public void seedData() {
        if (service.getAll().isEmpty()) {
            for (int i = 1; i <= 10; i++) {
                try {
                    Meeting meeting = new Meeting();
                    meeting.setTitle("Meeting " + i);
                    meeting.setMeetingDate(LocalDateTime.now().plusDays(i).plusHours(i));
                    meeting.setLocation("Office " + (i % 3 + 1));
                    meeting.setNotes("Discussion about property " + i);
                    meeting.setClientId(i);
                    meeting.setTaskId(i);
                    meeting.setStatus("in progress");
                    service.create(meeting);
                } catch (ValidationException e) {
                    System.out.println("❌ Seed data validation error: " + e.getMessage());
                }
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
            System.out.println("\n=== MEETINGS LIST ===");
            all.forEach(System.out::println);
        }
    }

    private void create() {
        while (true) {
            try {
                System.out.println("\n=== SCHEDULE NEW MEETING ===");
                String title = ConsoleHelper.ask("Meeting Title");
                String dateStr = ConsoleHelper.ask("Date (YYYY-MM-DD)");
                String timeStr = ConsoleHelper.ask("Time (HH:MM)");
                String location = ConsoleHelper.ask("Location");
                String clientIdStr = ConsoleHelper.ask("Client ID");
                String taskIdStr = ConsoleHelper.ask("Task ID (optional)");
                String status = ConsoleHelper.ask("Status (scheduled,completed,cancelled,rescheduled,in progress)");
                String notes = ConsoleHelper.ask("Notes");

                Meeting meeting = new Meeting();
                meeting.setTitle(title);
                meeting.setLocation(location);
                meeting.setStatus(status);
                meeting.setNotes(notes);

                // Валидация даты и времени
                try {
                    LocalDate date = LocalDate.parse(dateStr);
                    LocalTime time = LocalTime.parse(timeStr + ":00");
                    LocalDateTime meetingDateTime = LocalDateTime.of(date, time);
                    meeting.setMeetingDate(meetingDateTime);
                } catch (DateTimeParseException e) {
                    throw new ValidationException("Invalid date/time format. Please use YYYY-MM-DD for date and HH:MM for time.");
                }

                // Валидация Client ID
                try {
                    if (clientIdStr != null && !clientIdStr.trim().isEmpty()) {
                        meeting.setClientId(Integer.parseInt(clientIdStr));
                    } else {
                        throw new ValidationException("Client ID is required");
                    }
                } catch (NumberFormatException e) {
                    throw new ValidationException("Invalid Client ID format. Please enter a valid number.");
                }

                // Валидация Task ID (опционально)
                try {
                    if (taskIdStr != null && !taskIdStr.trim().isEmpty()) {
                        meeting.setTaskId(Integer.parseInt(taskIdStr));
                    }
                } catch (NumberFormatException e) {
                    throw new ValidationException("Invalid Task ID format. Please enter a valid number.");
                }

                service.create(meeting);
                System.out.println("✅ Meeting scheduled: " + meeting);
                break; // Выход из цикла при успешном создании

            } catch (ValidationException e) {
                System.out.println("❌ Validation error: " + e.getMessage());
                System.out.println("Please correct the data and try again.\n");

                String retry = ConsoleHelper.ask("Try again? (y/n)");
                if (!retry.equalsIgnoreCase("y")) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
                System.out.println("Please check your input and try again.\n");

                String retry = ConsoleHelper.ask("Try again? (y/n)");
                if (!retry.equalsIgnoreCase("y")) {
                    break;
                }
            }
        }
        ConsoleHelper.pause();
    }

    private void edit() {
        while (true) {
            try {
                System.out.println("\n=== EDIT MEETING ===");
                int id = ConsoleHelper.askInt("Enter meeting ID");

                Optional<Meeting> meetingOpt = service.getById(id);
                if (meetingOpt.isEmpty()) {
                    System.out.println("❌ Meeting not found with ID: " + id);

                    String retry = ConsoleHelper.ask("Try different ID? (y/n)");
                    if (!retry.equalsIgnoreCase("y")) {
                        break;
                    }
                    continue;
                }

                Meeting meeting = meetingOpt.get();
                System.out.println("Editing: " + meeting);

                // Запрашиваем новые данные с текущими значениями по умолчанию
                meeting.setTitle(ConsoleHelper.ask("Title (" + meeting.getTitle() + ")"));
                meeting.setLocation(ConsoleHelper.ask("Location (" + meeting.getLocation() + ")"));

                // Обработка даты и времени
                String dateStr = ConsoleHelper.ask("Date (" + meeting.getMeetingDate().toLocalDate() + ")");
                String timeStr = ConsoleHelper.ask("Time (" + meeting.getMeetingDate().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) + ")");

                if (!dateStr.trim().isEmpty() || !timeStr.trim().isEmpty()) {
                    try {
                        LocalDate date = dateStr.trim().isEmpty() ?
                                meeting.getMeetingDate().toLocalDate() : LocalDate.parse(dateStr);
                        LocalTime time = timeStr.trim().isEmpty() ?
                                meeting.getMeetingDate().toLocalTime() : LocalTime.parse(timeStr + ":00");
                        LocalDateTime meetingDateTime = LocalDateTime.of(date, time);
                        meeting.setMeetingDate(meetingDateTime);
                    } catch (DateTimeParseException e) {
                        throw new ValidationException("Invalid date/time format. Please use YYYY-MM-DD for date and HH:MM for time.");
                    }
                }

                // Обработка Client ID
                String clientIdStr = ConsoleHelper.ask("Client ID (" + meeting.getClientId() + ")");
                if (!clientIdStr.trim().isEmpty()) {
                    try {
                        meeting.setClientId(Integer.parseInt(clientIdStr));
                    } catch (NumberFormatException e) {
                        throw new ValidationException("Invalid Client ID format. Please enter a valid number.");
                    }
                }

                // Обработка Task ID
                String taskIdStr = ConsoleHelper.ask("Task ID (" + (meeting.getTaskId() != null ? meeting.getTaskId() : "null") + ")");
                if (!taskIdStr.trim().isEmpty()) {
                    try {
                        if ("null".equalsIgnoreCase(taskIdStr.trim())) {
                            meeting.setTaskId(null);
                        } else {
                            meeting.setTaskId(Integer.parseInt(taskIdStr));
                        }
                    } catch (NumberFormatException e) {
                        throw new ValidationException("Invalid Task ID format. Please enter a valid number or 'null'.");
                    }
                }

                meeting.setStatus(ConsoleHelper.ask("Status (" + meeting.getStatus() + ")"));
                meeting.setNotes(ConsoleHelper.ask("Notes (" + meeting.getNotes() + ")"));

                service.update(meeting);
                System.out.println("✅ Meeting updated: " + meeting);
                break; // Выход из цикла при успешном обновлении

            } catch (ValidationException e) {
                System.out.println("❌ Validation error: " + e.getMessage());
                System.out.println("Please correct the data and try again.\n");

                String retry = ConsoleHelper.ask("Try again? (y/n)");
                if (!retry.equalsIgnoreCase("y")) {
                    break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Error: " + e.getMessage());

                String retry = ConsoleHelper.ask("Try again? (y/n)");
                if (!retry.equalsIgnoreCase("y")) {
                    break;
                }
            }
        }
        ConsoleHelper.pause();
    }

    private void delete() {
        while (true) {
            try {
                System.out.println("\n=== DELETE MEETING ===");
                int id = ConsoleHelper.askInt("Enter meeting ID to delete");

                // Проверяем существование встречи перед удалением
                Optional<Meeting> meetingOpt = service.getById(id);
                if (meetingOpt.isEmpty()) {
                    System.out.println("❌ Meeting not found with ID: " + id);

                    String retry = ConsoleHelper.ask("Try different ID? (y/n)");
                    if (!retry.equalsIgnoreCase("y")) {
                        break;
                    }
                    continue;
                }

                Meeting meeting = meetingOpt.get();
                System.out.println("About to delete: " + meeting);

                String confirm = ConsoleHelper.ask("Are you sure you want to delete this meeting? (y/n)");
                if (confirm.equalsIgnoreCase("y")) {
                    service.delete(id);
                    System.out.println("✅ Meeting deleted successfully");
                } else {
                    System.out.println("❌ Deletion cancelled");
                }
                break; // Выход из цикла

            } catch (ValidationException e) {
                System.out.println("❌ Validation error: " + e.getMessage());
                System.out.println("Please correct the data and try again.\n");

                String retry = ConsoleHelper.ask("Try again? (y/n)");
                if (!retry.equalsIgnoreCase("y")) {
                    break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Error: " + e.getMessage());

                String retry = ConsoleHelper.ask("Try again? (y/n)");
                if (!retry.equalsIgnoreCase("y")) {
                    break;
                }
            }
        }
        ConsoleHelper.pause();
    }
}