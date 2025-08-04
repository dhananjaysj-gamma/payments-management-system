package in.zeta.payments.dao.impl;

import in.zeta.payments.config.DBConnection;
import in.zeta.payments.dao.PaymentDAO;
import in.zeta.payments.enums.PaymentStatus;
import in.zeta.payments.enums.PaymentType;
import in.zeta.payments.models.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {



    @Override
    public void save(Payment payment) {
        String sql = "INSERT INTO payments (amount, payment_type, payment_date, status, description, created_by, category_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDouble(1, payment.getAmount());
            stmt.setString(2, payment.getPaymentType().toString());
            stmt.setDate(3, Date.valueOf(payment.getPaymentDate()));
            stmt.setString(4, payment.getStatus().toString());
            stmt.setString(5, payment.getDescription());
            stmt.setInt(6, payment.getCreatedBy());
            stmt.setInt(7, payment.getCategoryID());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                payment.setPaymentId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Payment findById(int paymentId) {
        String sql = "SELECT * FROM payments WHERE payment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, paymentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapToPayment(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Payment> findAllByUserId(int userId) {
        String sql = "SELECT * FROM payments WHERE created_by = ?";
        List<Payment> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapToPayment(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Payment> findAll() {
        String sql = "SELECT * FROM payments";
        List<Payment> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapToPayment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void update(Payment payment) {
        String sql = "UPDATE payments SET amount = ?, payment_type = ?, payment_date = ?, status = ?, description = ?, " +
                "created_by = ?, category_id = ? WHERE payment_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, payment.getAmount());
            stmt.setString(2, payment.getPaymentType().toString());
            stmt.setDate(3, Date.valueOf(payment.getPaymentDate()));
            stmt.setString(4, payment.getStatus().toString());
            stmt.setString(5, payment.getDescription());
            stmt.setInt(6, payment.getCreatedBy());
            stmt.setInt(7, payment.getCategoryID());
            stmt.setInt(8, payment.getPaymentId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int paymentId) {
        String sql = "DELETE FROM payments WHERE payment_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, paymentId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Payment mapToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getInt("payment_id"));
        payment.setAmount(rs.getDouble("amount"));
        payment.setPaymentType(PaymentType.valueOf(rs.getString("payment_type")));
        payment.setPaymentDate(rs.getDate("payment_date").toLocalDate());
        payment.setStatus(PaymentStatus.valueOf(rs.getString("status")));
        payment.setDescription(rs.getString("description"));
        payment.setCreatedBy(rs.getInt("created_by"));
        payment.setCategoryID(rs.getInt("category_id"));
        return payment;
    }
}
