package in.zeta.payments.management.system.repository;

import in.zeta.payments.management.system.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);
}
