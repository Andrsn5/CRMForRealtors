package com.company.crm.dao.jdbc;

import com.company.crm.dao.interfaces.AdditionalConditionDao;
import com.company.crm.model.AdditionalCondition;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdditionalConditionDaoJdbcImpl implements AdditionalConditionDao {

    @Override
    public List<AdditionalCondition> findAll() {
        String sql = "SELECT * FROM additional_conditions ORDER BY id_condition";
        List<AdditionalCondition> list = new ArrayList<>();

        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading conditions", e);
        }
        return list;
    }

    @Override
    public Optional<AdditionalCondition> findById(int id) {
        String sql = "SELECT * FROM additional_conditions WHERE id_condition=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding condition", e);
        }
        return Optional.empty();
    }

    @Override
    public AdditionalCondition save(AdditionalCondition condition) {
        String sql = """
            INSERT INTO additional_conditions(condition_type, description, deadline, required, status, priority, notes)
            VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id_condition
            """;
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, condition.getConditionType());
            ps.setString(2, condition.getDescription());
            ps.setDate(3, condition.getDeadline() != null ? Date.valueOf(condition.getDeadline()) : null);
            ps.setBoolean(4, condition.isRequired());
            ps.setString(5, condition.getStatus());
            ps.setString(6, condition.getPriority());
            ps.setString(7, condition.getNotes());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) condition.setId(rs.getInt("id_condition"));
            return condition;
        } catch (SQLException ex) {
            throw new RuntimeException("Error saving condition", ex);
        }
    }

    @Override
    public void update(AdditionalCondition condition) {
        String sql = """
            UPDATE additional_conditions SET condition_type=?, description=?, deadline=?, required=?, status=?, priority=?, notes=?
            WHERE id_condition=?
            """;
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, condition.getConditionType());
            ps.setString(2, condition.getDescription());
            ps.setDate(3, condition.getDeadline() != null ? Date.valueOf(condition.getDeadline()) : null);
            ps.setBoolean(4, condition.isRequired());
            ps.setString(5, condition.getStatus());
            ps.setString(6, condition.getPriority());
            ps.setString(7, condition.getNotes());
            ps.setInt(8, condition.getId());
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error updating condition", ex);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM additional_conditions WHERE id_condition=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error deleting condition", ex);
        }
    }

    private AdditionalCondition mapRow(ResultSet rs) throws SQLException {
        AdditionalCondition condition = new AdditionalCondition();
        condition.setId(rs.getInt("id_condition"));
        condition.setConditionType(rs.getString("condition_type"));
        condition.setDescription(rs.getString("description"));
        Date deadline = rs.getDate("deadline");
        condition.setDeadline(deadline != null ? deadline.toLocalDate() : null);
        condition.setRequired(rs.getBoolean("required"));
        condition.setStatus(rs.getString("status"));
        condition.setPriority(rs.getString("priority"));
        condition.setNotes(rs.getString("notes"));
        return condition;
    }
}