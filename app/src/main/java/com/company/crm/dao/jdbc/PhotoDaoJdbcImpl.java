package com.company.crm.dao.jdbc;

import com.company.crm.dao.interfaces.PhotoDao;
import com.company.crm.model.Photo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PhotoDaoJdbcImpl implements PhotoDao {

    @Override
    public List<Photo> findAll() {
        String sql = "SELECT * FROM photos ORDER BY id_photo";
        List<Photo> list = new ArrayList<>();

        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading photos", e);
        }
        return list;
    }

    @Override
    public Optional<Photo> findById(int id) {
        String sql = "SELECT * FROM photos WHERE id_photo=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding photo", e);
        }
        return Optional.empty();
    }

    @Override
    public Photo save(Photo photo) {
        String sql = """
            INSERT INTO photos(photo_url, display_order, caption, id_object)
            VALUES (?, ?, ?, ?) RETURNING id_photo
            """;
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, photo.getPhotoUrl());
            ps.setObject(2, photo.getDisplayOrder());
            ps.setString(3, photo.getCaption());
            ps.setObject(4, photo.getObjectId());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) photo.setId(rs.getInt("id_photo"));
            return photo;
        } catch (SQLException ex) {
            throw new RuntimeException("Error saving photo", ex);
        }
    }

    @Override
    public void update(Photo photo) {
        String sql = """
            UPDATE photos SET photo_url=?, display_order=?, caption=?, id_object=?
            WHERE id_photo=?
            """;
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, photo.getPhotoUrl());
            ps.setObject(2, photo.getDisplayOrder());
            ps.setString(3, photo.getCaption());
            ps.setObject(4, photo.getObjectId());
            ps.setInt(5, photo.getId());
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error updating photo", ex);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM photos WHERE id_photo=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error deleting photo", ex);
        }
    }

    private Photo mapRow(ResultSet rs) throws SQLException {
        Photo photo = new Photo();
        photo.setId(rs.getInt("id_photo"));
        photo.setPhotoUrl(rs.getString("photo_url"));
        photo.setDisplayOrder(rs.getInt("display_order"));
        photo.setCaption(rs.getString("caption"));
        photo.setObjectId(rs.getInt("id_object"));
        return photo;
    }
}