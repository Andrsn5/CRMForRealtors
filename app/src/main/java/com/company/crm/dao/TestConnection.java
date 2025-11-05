package com.company.crm.dao;

import com.company.crm.dao.jdbc.ConnectionFactory;

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            if (connection != null) {
                System.out.println("✅ Подключение к БД успешно!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}