package com.company.crm.dao.jdbc;

import com.company.crm.dao.interfaces.TaskDao;
import com.company.crm.model.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskDaoJdbcImpl implements TaskDao {

    @Override
    public List<Task> findAll() {
        String sql = "SELECT * FROM tasks ORDER BY id_task";
        List<Task> list = new ArrayList<>();

        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading tasks", e);
        }
        return list;
    }

    @Override
    public Optional<Task> findById(int id) {
        String sql = "SELECT * FROM tasks WHERE id_task=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding task", e);
        }
        return Optional.empty();
    }

    @Override
    public Task save(Task task) {
        String sql = """
            INSERT INTO tasks(title, description, due_date, priority, status, id_responsible, id_creator, 
            id_client, id_object, id_condition, id_deal, id_meeting)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_task
            """;
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setTimestamp(3, task.getDueDate() != null ? Timestamp.valueOf(task.getDueDate()) : null);
            ps.setString(4, task.getPriority());
            ps.setString(5, task.getStatus());
            ps.setObject(6, task.getResponsibleId());
            ps.setObject(7, task.getCreatorId());
            ps.setObject(8, task.getClientId());
            ps.setObject(9, task.getObjectId());
            ps.setObject(10, task.getConditionId());
            ps.setObject(11, task.getDealId());
            ps.setObject(12, task.getMeetingId());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) task.setId(rs.getInt("id_task"));
            return task;
        } catch (SQLException ex) {
            throw new RuntimeException("Error saving task", ex);
        }
    }

    @Override
    public void update(Task task) {
        String sql = """
            UPDATE tasks SET title=?, description=?, due_date=?, priority=?, status=?, id_responsible=?, 
            id_creator=?, id_client=?, id_object=?, id_condition=?, id_deal=?, id_meeting=?
            WHERE id_task=?
            """;
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setTimestamp(3, task.getDueDate() != null ? Timestamp.valueOf(task.getDueDate()) : null);
            ps.setString(4, task.getPriority());
            ps.setString(5, task.getStatus());
            ps.setObject(6, task.getResponsibleId());
            ps.setObject(7, task.getCreatorId());
            ps.setObject(8, task.getClientId());
            ps.setObject(9, task.getObjectId());
            ps.setObject(10, task.getConditionId());
            ps.setObject(11, task.getDealId());
            ps.setObject(12, task.getMeetingId());
            ps.setInt(13, task.getId());
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error updating task", ex);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM tasks WHERE id_task=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error deleting task", ex);
        }
    }

    private Task mapRow(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(rs.getInt("id_task"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        Timestamp dueDate = rs.getTimestamp("due_date");
        task.setDueDate(dueDate != null ? dueDate.toLocalDateTime() : null);
        task.setPriority(rs.getString("priority"));
        task.setStatus(rs.getString("status"));
        task.setResponsibleId(rs.getInt("id_responsible"));
        task.setCreatorId(rs.getInt("id_creator"));
        task.setClientId(rs.getInt("id_client"));
        task.setObjectId(rs.getInt("id_object"));
        task.setConditionId(rs.getInt("id_condition"));
        task.setDealId(rs.getInt("id_deal"));
        task.setMeetingId(rs.getInt("id_meeting"));
        return task;
    }
}