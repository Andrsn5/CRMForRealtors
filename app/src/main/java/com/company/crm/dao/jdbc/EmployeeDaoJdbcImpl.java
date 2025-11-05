package com.company.crm.dao.jdbc;
import com.company.crm.dao.interfaces.EmployeeDao;
import com.company.crm.model.Employee;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class EmployeeDaoJdbcImpl implements EmployeeDao {

    @Override
    public List<Employee> findAll() {
        String sql = "SELECT * FROM employees ORDER BY id_employee";
        List<Employee> list = new ArrayList<>();

        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading employees", e);
        }
        return list;
    }

    @Override
    public Optional<Employee> findById(int id) {
        String sql = "SELECT * FROM employees WHERE id_employee=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding employee", e);
        }
        return Optional.empty();
    }

    @Override
    public Employee save(Employee e) {
        String sql = """
            INSERT INTO employees(first_name,last_name,email,phone,position,hire_date,is_active)
            VALUES (?,?,?,?,?,?,?) RETURNING id_employee
            """;
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, e.getFirstName());
            ps.setString(2, e.getLastName());
            ps.setString(3, e.getEmail());
            ps.setString(4, e.getPhone());
            ps.setString(5, e.getPosition());
            ps.setObject(6, e.getHireDate());
            ps.setBoolean(7, e.isActive());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) e.setId(rs.getInt("id_employee"));
            return e;
        } catch (SQLException ex) {
            throw new RuntimeException("Error saving employee", ex);
        }
    }

    @Override
    public void update(Employee e) {
        String sql = """
            UPDATE employees SET first_name=?, last_name=?, email=?, phone=?, position=?, hire_date=?, is_active=?
            WHERE id_employee=?
            """;
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, e.getFirstName());
            ps.setString(2, e.getLastName());
            ps.setString(3, e.getEmail());
            ps.setString(4, e.getPhone());
            ps.setString(5, e.getPosition());
            ps.setObject(6, e.getHireDate());
            ps.setBoolean(7, e.isActive());
            ps.setInt(8, e.getId());
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error updating employee", ex);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM employees WHERE id_employee=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error deleting employee", ex);
        }
    }

    private Employee mapRow(ResultSet rs) throws SQLException {
        Employee e = new Employee();
        e.setId(rs.getInt("id_employee"));
        e.setFirstName(rs.getString("first_name"));
        e.setLastName(rs.getString("last_name"));
        e.setEmail(rs.getString("email"));
        e.setPhone(rs.getString("phone"));
        e.setPosition(rs.getString("position"));
        Date hire = rs.getDate("hire_date");
        e.setHireDate(hire != null ? hire.toLocalDate() : null);
        e.setActive(rs.getBoolean("is_active"));
        return e;
    }
}
