package com.company.crm.controller;

import com.company.crm.model.Photo;
import com.company.crm.service.interfaces.PhotoService;
import com.company.crm.util.ConsoleHelper;
import com.company.crm.util.ValidationException;

import java.util.List;
import java.util.Optional;

public class PhotoController {
    private final PhotoService service;

    public PhotoController(PhotoService service) {
        this.service = service;
    }

    public void seedData() {
        if (service.getAll().isEmpty()) {
            for (int i = 1; i <= 10; i++) {
                try {
                    Photo photo = new Photo();
                    photo.setPhotoUrl("https://example.com/photo" + i + ".jpg");
                    photo.setDisplayOrder(i);
                    photo.setCaption("Property photo " + i);
                    photo.setObjectId(i); // Каждое фото для своего объекта
                    service.create(photo);
                } catch (ValidationException e) {
                    System.out.println("❌ Seed data validation error: " + e.getMessage());
                }
            }
        }
    }

    public void menu() {
        while (true) {
            System.out.println("\n=== PHOTO MANAGEMENT ===");
            System.out.println("1. List 2. Create 3. Edit 4. Delete 0. Back");
            int choice = ConsoleHelper.askInt("Choose option");
            switch (choice) {
                case 1 -> list();
                case 2 -> create();
                case 3 -> edit();
                case 4 -> delete();
                case 0 -> {
                    return;
                }
                default -> System.out.println("❌ Invalid option");
            }
        }
    }

    private void list() {
        List<Photo> all = service.getAll();
        if (all.isEmpty()) {
            System.out.println("No photos found");
        } else {
            System.out.println("\n=== PHOTOS LIST ===");
            all.forEach(System.out::println);
        }
    }

    private void create() {
        while (true) {
            try {
                System.out.println("\n=== ADD NEW PHOTO ===");
                String photoUrl = ConsoleHelper.ask("Photo URL");
                String displayOrderStr = ConsoleHelper.ask("Display Order");
                String caption = ConsoleHelper.ask("Caption");
                String objectIdStr = ConsoleHelper.ask("Object ID");

                Photo photo = new Photo();
                photo.setPhotoUrl(photoUrl);
                photo.setCaption(caption);

                // Валидация числовых полей
                try {
                    photo.setDisplayOrder(Integer.parseInt(displayOrderStr));
                } catch (NumberFormatException e) {
                    throw new ValidationException("Invalid display order format. Please enter a valid integer.");
                }

                try {
                    if (objectIdStr != null && !objectIdStr.trim().isEmpty()) {
                        photo.setObjectId(Integer.parseInt(objectIdStr));
                    } else {
                        throw new ValidationException("Object ID is required");
                    }
                } catch (NumberFormatException e) {
                    throw new ValidationException("Invalid Object ID format. Please enter a valid number.");
                }

                service.create(photo);
                System.out.println("✅ Photo added: " + photo);
                break; // Выход из цикла при успешном создании

            } catch (ValidationException e) {
                System.out.println("❌ Validation error: " + e.getMessage());
                System.out.println("Please correct the data and try again.\n");

                String retry = ConsoleHelper.ask("Try again? (y/n)");
                if (!retry.equalsIgnoreCase("y")) {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Number format error: " + e.getMessage());
                System.out.println("Please enter valid numbers for display order and object ID.\n");

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
                System.out.println("\n=== EDIT PHOTO ===");
                int id = ConsoleHelper.askInt("Enter photo ID");

                Optional<Photo> photoOpt = service.getById(id);
                if (photoOpt.isEmpty()) {
                    System.out.println("❌ Photo not found with ID: " + id);

                    String retry = ConsoleHelper.ask("Try different ID? (y/n)");
                    if (!retry.equalsIgnoreCase("y")) {
                        break;
                    }
                    continue;
                }

                Photo photo = photoOpt.get();
                System.out.println("Editing: " + photo);

                // Запрашиваем новые данные с текущими значениями по умолчанию
                photo.setPhotoUrl(ConsoleHelper.ask("Photo URL (" + photo.getPhotoUrl() + ")"));
                photo.setCaption(ConsoleHelper.ask("Caption (" + photo.getCaption() + ")"));

                // Обработка порядка отображения
                String displayOrderStr = ConsoleHelper.ask("Display Order (" + photo.getDisplayOrder() + ")");
                if (!displayOrderStr.trim().isEmpty()) {
                    try {
                        photo.setDisplayOrder(Integer.parseInt(displayOrderStr));
                    } catch (NumberFormatException e) {
                        throw new ValidationException("Invalid display order format. Please enter a valid integer.");
                    }
                }

                // Обработка Object ID
                String objectIdStr = ConsoleHelper.ask("Object ID (" + photo.getObjectId() + ")");
                if (!objectIdStr.trim().isEmpty()) {
                    try {
                        photo.setObjectId(Integer.parseInt(objectIdStr));
                    } catch (NumberFormatException e) {
                        throw new ValidationException("Invalid Object ID format. Please enter a valid number.");
                    }
                }

                service.update(photo);
                System.out.println("✅ Photo updated: " + photo);
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
                System.out.println("\n=== DELETE PHOTO ===");
                int id = ConsoleHelper.askInt("Enter photo ID to delete");

                // Проверяем существование фото перед удалением
                Optional<Photo> photoOpt = service.getById(id);
                if (photoOpt.isEmpty()) {
                    System.out.println("❌ Photo not found with ID: " + id);

                    String retry = ConsoleHelper.ask("Try different ID? (y/n)");
                    if (!retry.equalsIgnoreCase("y")) {
                        break;
                    }
                    continue;
                }

                Photo photo = photoOpt.get();
                System.out.println("About to delete: " + photo);

                String confirm = ConsoleHelper.ask("Are you sure you want to delete this photo? (y/n)");
                if (confirm.equalsIgnoreCase("y")) {
                    service.delete(id);
                    System.out.println("✅ Photo deleted successfully");
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