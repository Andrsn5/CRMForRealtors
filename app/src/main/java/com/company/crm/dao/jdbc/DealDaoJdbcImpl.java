package com.company.crm.dao.jdbc;

import com.company.crm.dao.interfaces.DealDao;
import com.company.crm.model.Deal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DealDaoJdbcImpl implements DealDao {

    @Override
    public List<Deal> findAll() {
        String sql = "SELECT * FROM deals ORDER BY id_deal";
        List<Deal> list = new ArrayList<>();

        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading deals", e);
        }
        return list;
    }

    @Override
    public Optional<Deal> findById(int id) {
        String sql = "SELECT * FROM deals WHERE id_deal=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding deal", e);
        }
        return Optional.empty();
    }

    @Override
    public Deal save(Deal deal) {
        String sql = """
            INSERT INTO deals(deal_number, id_task, deal_amount, deal_date, commission, status)
            VALUES (?, ?, ?, ?, ?, ?) RETURNING id_deal
            """;
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, deal.getDealNumber());
            ps.setObject(2, deal.getTaskId());
            ps.setBigDecimal(3, deal.getDealAmount());
            ps.setDate(4, deal.getDealDate() != null ? Date.valueOf(deal.getDealDate()) : null);
            ps.setBigDecimal(5, deal.getCommission());
            ps.setString(6, deal.getStatus());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) deal.setId(rs.getInt("id_deal"));
            return deal;
        } catch (SQLException ex) {
            throw new RuntimeException("Error saving deal", ex);
        }
    }

    @Override
    public void update(Deal deal) {
        String sql = """
            UPDATE deals SET deal_number=?, id_task=?, deal_amount=?, deal_date=?, commission=?, status=?
            WHERE id_deal=?
            """;
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, deal.getDealNumber());
            ps.setObject(2, deal.getTaskId());
            ps.setBigDecimal(3, deal.getDealAmount());
            ps.setDate(4, deal.getDealDate() != null ? Date.valueOf(deal.getDealDate()) : null);
            ps.setBigDecimal(5, deal.getCommission());
            ps.setString(6, deal.getStatus());
            ps.setInt(7, deal.getId());
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error updating deal", ex);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM deals WHERE id_deal=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error deleting deal", ex);
        }
    }

    private Deal mapRow(ResultSet rs) throws SQLException {
        Deal deal = new Deal();
        deal.setId(rs.getInt("id_deal"));
        deal.setDealNumber(rs.getString("deal_number"));
        deal.setTaskId(rs.getInt("id_task"));
        deal.setDealAmount(rs.getBigDecimal("deal_amount"));
        Date dealDate = rs.getDate("deal_date");
        deal.setDealDate(dealDate != null ? dealDate.toLocalDate() : null);
        deal.setCommission(rs.getBigDecimal("commission"));
        deal.setStatus(rs.getString("status"));
        return deal;
    }
}