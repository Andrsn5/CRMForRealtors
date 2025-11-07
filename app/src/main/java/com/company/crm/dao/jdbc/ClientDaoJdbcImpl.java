package com.company.crm.dao.jdbc;

import com.company.crm.dao.interfaces.ClientDao;
import com.company.crm.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientDaoJdbcImpl implements ClientDao {

    @Override
    public List<Client> findAll() {
        String sql = "SELECT * FROM clients ORDER BY id_client";
        List<Client> list = new ArrayList<>();

        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading clients", e);
        }
        return list;
    }

    @Override
    public Optional<Client> findById(int id) {
        String sql = "SELECT * FROM clients WHERE id_client=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding client", e);
        }
        return Optional.empty();
    }

    @Override
    public Client save(Client client) {
        String sql = """
            INSERT INTO clients(first_name, last_name, email, phone, client_type, budget, notes)
            VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id_client
            """;
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, client.getFirstName());
            ps.setString(2, client.getLastName());
            ps.setString(3, client.getEmail());
            ps.setString(4, client.getPhone());
            ps.setString(5, client.getClientType());
            ps.setBigDecimal(6, client.getBudget());
            ps.setString(7, client.getNotes());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) client.setId(rs.getInt("id_client"));
            return client;
        } catch (SQLException ex) {
            throw new RuntimeException("Error saving client", ex);
        }
    }

    @Override
    public void update(Client client) {
        String sql = """
            UPDATE clients SET first_name=?, last_name=?, email=?, phone=?, client_type=?, budget=?, notes=?
            WHERE id_client=?
            """;
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, client.getFirstName());
            ps.setString(2, client.getLastName());
            ps.setString(3, client.getEmail());
            ps.setString(4, client.getPhone());
            ps.setString(5, client.getClientType());
            ps.setBigDecimal(6, client.getBudget());
            ps.setString(7, client.getNotes());
            ps.setInt(8, client.getId());
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error updating client", ex);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM clients WHERE id_client=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error deleting client", ex);
        }
    }

    private Client mapRow(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getInt("id_client"));
        client.setFirstName(rs.getString("first_name"));
        client.setLastName(rs.getString("last_name"));
        client.setEmail(rs.getString("email"));
        client.setPhone(rs.getString("phone"));
        client.setClientType(rs.getString("client_type"));
        client.setBudget(rs.getBigDecimal("budget"));
        client.setNotes(rs.getString("notes"));
        return client;
    }
}