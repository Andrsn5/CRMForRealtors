package com.company.crm.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class ValidationUtils {

    // Паттерны для валидации
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[\\d\\s\\-\\(\\)\\+]+$");
    private static final Pattern URL_PATTERN = Pattern.compile("^(https?|ftp)://[^\\s/$.?#].[^\\s]*$");

    // Валидация обязательных полей
    public static void validateRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " is required");
        }
    }

    public static void validateRequired(Object value, String fieldName) {
        if (value == null) {
            throw new ValidationException(fieldName + " is required");
        }
    }

    // Валидация email
    public static void validateEmail(String email) {
        if (email != null && !email.trim().isEmpty()) {
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                throw new ValidationException("Invalid email format: " + email);
            }
        }
    }

    // Валидация телефона
    public static void validatePhone(String phone) {
        if (phone != null && !phone.trim().isEmpty()) {
            if (!PHONE_PATTERN.matcher(phone).matches()) {
                throw new ValidationException("Invalid phone format: " + phone);
            }
        }
    }

    // Валидация URL
    public static void validateUrl(String url) {
        if (url != null && !url.trim().isEmpty()) {
            if (!URL_PATTERN.matcher(url).matches()) {
                throw new ValidationException("Invalid URL format: " + url);
            }
        }
    }

    // Валидация положительного числа
    public static void validatePositive(BigDecimal value, String fieldName) {
        if (value != null && value.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException(fieldName + " must be positive");
        }
    }

    // Валидация минимального значения
    public static void validateMinValue(BigDecimal value, BigDecimal min, String fieldName) {
        if (value != null && value.compareTo(min) < 0) {
            throw new ValidationException(fieldName + " must be at least " + min);
        }
    }

    // Валидация даты (не в прошлом)
    public static void validateFutureDate(LocalDate date, String fieldName) {
        if (date != null && date.isBefore(LocalDate.now())) {
            throw new ValidationException(fieldName + " cannot be in the past");
        }
    }

    public static void validateFutureDateTime(LocalDateTime dateTime, String fieldName) {
        if (dateTime != null && dateTime.isBefore(LocalDateTime.now())) {
            throw new ValidationException(fieldName + " cannot be in the past");
        }
    }

    // Валидация длины строки
    public static void validateMaxLength(String value, int maxLength, String fieldName) {
        if (value != null && value.length() > maxLength) {
            throw new ValidationException(fieldName + " exceeds maximum length of " + maxLength + " characters");
        }
    }

    // Валидация диапазона чисел
    public static void validateRange(int value, int min, int max, String fieldName) {
        if (value < min || value > max) {
            throw new ValidationException(fieldName + " must be between " + min + " and " + max);
        }
    }
}