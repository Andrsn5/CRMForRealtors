package com.company.crm.dao.jdbc;

import com.company.crm.dao.interfaces.ObjectDao;
import com.company.crm.model.Object;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ObjectDaoJdbcImpl implements ObjectDao {

    @Override
    public List<Object> findAll() {
        String sql = "SELECT * FROM objects ORDER BY id_object";
        List<Object> list = new ArrayList<>();

        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading objects", e);
        }
        return list;
    }

    @Override
    public Optional<Object> findById(int id) {
        String sql = "SELECT * FROM objects WHERE id_object=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding object", e);
        }
        return Optional.empty();
    }

    @Override
    public Object save(Object obj) {
        String sql = """
            INSERT INTO objects(title, description, object_type, deal_type, price, address, area, rooms, bathrooms, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_object
            """;
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, obj.getTitle());
            ps.setString(2, obj.getDescription());
            ps.setString(3, obj.getObjectType());
            ps.setString(4, obj.getDealType());
            ps.setBigDecimal(5, obj.getPrice());
            ps.setString(6, obj.getAddress());
            ps.setBigDecimal(7, obj.getArea());
            ps.setObject(8, obj.getRooms());
            ps.setObject(9, obj.getBathrooms());
            ps.setString(10, obj.getStatus());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) obj.setId(rs.getInt("id_object"));
            return obj;
        } catch (SQLException ex) {
            throw new RuntimeException("Error saving object", ex);
        }
    }

    @Override
    public void update(Object obj) {
        String sql = """
            UPDATE objects SET title=?, description=?, object_type=?, deal_type=?, price=?, address=?, 
            area=?, rooms=?, bathrooms=?, status=? WHERE id_object=?
            """;
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, obj.getTitle());
            ps.setString(2, obj.getDescription());
            ps.setString(3, obj.getObjectType());
            ps.setString(4, obj.getDealType());
            ps.setBigDecimal(5, obj.getPrice());
            ps.setString(6, obj.getAddress());
            ps.setBigDecimal(7, obj.getArea());
            ps.setObject(8, obj.getRooms());
            ps.setObject(9, obj.getBathrooms());
            ps.setString(10, obj.getStatus());
            ps.setInt(11, obj.getId());
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error updating object", ex);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM objects WHERE id_object=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error deleting object", ex);
        }
    }

    private Object mapRow(ResultSet rs) throws SQLException {
        Object obj = new Object();
        obj.setId(rs.getInt("id_object"));
        obj.setTitle(rs.getString("title"));
        obj.setDescription(rs.getString("description"));
        obj.setObjectType(rs.getString("object_type"));
        obj.setDealType(rs.getString("deal_type"));
        obj.setPrice(rs.getBigDecimal("price"));
        obj.setAddress(rs.getString("address"));
        obj.setArea(rs.getBigDecimal("area"));
        obj.setRooms(rs.getInt("rooms"));
        obj.setBathrooms(rs.getInt("bathrooms"));
        obj.setStatus(rs.getString("status"));
        return obj;
    }
}