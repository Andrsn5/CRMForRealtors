package com.company.crm.controller;

import com.company.crm.model.Photo;
import com.company.crm.service.interfaces.PhotoService;
import com.company.crm.util.ConsoleHelper;

import java.util.List;

public class PhotoController {
    private final PhotoService service;

    public PhotoController(PhotoService service) {
        this.service = service;
        seedData();
    }

    private void seedData() {
        if (service.getAll().isEmpty()) {
            for (int i = 1; i <= 10; i++) {
                Photo photo = new Photo();
                photo.setPhotoUrl("https://example.com/photo" + i + ".jpg");
                photo.setDisplayOrder(i);
                photo.setCaption("Property photo " + i);
                photo.setObjectId((i % 5) + 1); // Link to existing objects
                service.create(photo);
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
                case 0 -> { return; }
                default -> System.out.println("❌ Invalid option");
            }
        }
    }

    private void list() {
        List<Photo> all = service.getAll();
        if (all.isEmpty()) {
            System.out.println("No photos found");
        } else {
            all.forEach(System.out::println);
        }
    }

    private void create() {
        String photoUrl = ConsoleHelper.ask("Photo URL");
        String displayOrderStr = ConsoleHelper.ask("Display Order");
        String caption = ConsoleHelper.ask("Caption");
        String objectIdStr = ConsoleHelper.ask("Object ID");

        Photo photo = new Photo();
        photo.setPhotoUrl(photoUrl);
        photo.setCaption(caption);

        try {
            photo.setDisplayOrder(Integer.parseInt(displayOrderStr));
            photo.setObjectId(Integer.parseInt(objectIdStr));
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid number format");
            return;
        }

        service.create(photo);
        System.out.println("✅ Photo added: " + photo);
        ConsoleHelper.pause();
    }

    private void edit() {
        int id = ConsoleHelper.askInt("Enter photo ID");
        service.getById(id).ifPresentOrElse(photo -> {
            photo.setCaption(ConsoleHelper.ask("Caption (" + photo.getCaption() + ")"));
            photo.setDisplayOrder(Integer.parseInt(ConsoleHelper.ask("Display Order (" + photo.getDisplayOrder() + ")")));
            service.update(photo);
            System.out.println("✅ Photo updated: " + photo);
        }, () -> System.out.println("❌ Photo not found."));
        ConsoleHelper.pause();
    }

    private void delete() {
        int id = ConsoleHelper.askInt("Enter photo ID to delete");
        service.delete(id);
        System.out.println("✅ Photo deleted");
        ConsoleHelper.pause();
    }
}