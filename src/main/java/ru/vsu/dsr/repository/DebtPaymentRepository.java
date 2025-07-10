package ru.vsu.dsr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.vsu.dsr.model.DebtPayment;

import java.util.List;

public interface DebtPaymentRepository extends JpaRepository<DebtPayment, Long> {
    @Query(
            """
            select d from DebtPayment d
            where d.sharedBill.id = :sharedBillId
            """
    )
    List<DebtPayment> findBySharedBillId(Long sharedBillId);
}
