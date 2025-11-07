package com.company.crm.dao.jdbc;

import com.company.crm.dao.interfaces.MeetingDao;
import com.company.crm.model.Meeting;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MeetingDaoJdbcImpl implements MeetingDao {

    @Override
    public List<Meeting> findAll() {
        String sql = "SELECT * FROM meetings ORDER BY id_meeting";
        List<Meeting> list = new ArrayList<>();

        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading meetings", e);
        }
        return list;
    }

    @Override
    public Optional<Meeting> findById(int id) {
        String sql = "SELECT * FROM meetings WHERE id_meeting=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding meeting", e);
        }
        return Optional.empty();
    }

    @Override
    public Meeting save(Meeting meeting) {
        String sql = """
            INSERT INTO meetings(title, meeting_date, location, notes, id_client, id_task, status)
            VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id_meeting
            """;
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, meeting.getTitle());
            ps.setTimestamp(2, meeting.getMeetingDate() != null ? Timestamp.valueOf(meeting.getMeetingDate()) : null);
            ps.setString(3, meeting.getLocation());
            ps.setString(4, meeting.getNotes());
            ps.setObject(5, meeting.getClientId());
            ps.setObject(6, meeting.getTaskId());
            ps.setString(7, meeting.getStatus());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) meeting.setId(rs.getInt("id_meeting"));
            return meeting;
        } catch (SQLException ex) {
            throw new RuntimeException("Error saving meeting", ex);
        }
    }

    @Override
    public void update(Meeting meeting) {
        String sql = """
            UPDATE meetings SET title=?, meeting_date=?, location=?, notes=?, id_client=?, id_task=?, status=?
            WHERE id_meeting=?
            """;
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, meeting.getTitle());
            ps.setTimestamp(2, meeting.getMeetingDate() != null ? Timestamp.valueOf(meeting.getMeetingDate()) : null);
            ps.setString(3, meeting.getLocation());
            ps.setString(4, meeting.getNotes());
            ps.setObject(5, meeting.getClientId());
            ps.setObject(6, meeting.getTaskId());
            ps.setString(7, meeting.getStatus());
            ps.setInt(8, meeting.getId());
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error updating meeting", ex);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM meetings WHERE id_meeting=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error deleting meeting", ex);
        }
    }

    private Meeting mapRow(ResultSet rs) throws SQLException {
        Meeting meeting = new Meeting();
        meeting.setId(rs.getInt("id_meeting"));
        meeting.setTitle(rs.getString("title"));
        Timestamp meetingDate = rs.getTimestamp("meeting_date");
        meeting.setMeetingDate(meetingDate != null ? meetingDate.toLocalDateTime() : null);
        meeting.setLocation(rs.getString("location"));
        meeting.setNotes(rs.getString("notes"));
        meeting.setClientId(rs.getInt("id_client"));
        meeting.setTaskId(rs.getInt("id_task"));
        meeting.setStatus(rs.getString("status"));
        return meeting;
    }
}