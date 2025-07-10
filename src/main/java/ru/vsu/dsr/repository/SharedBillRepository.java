package ru.vsu.dsr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import ru.vsu.dsr.model.SharedBill;

import java.util.List;

public interface SharedBillRepository extends JpaRepository<SharedBill, Long> {
    @Query(value = """
            select * from shared_bill sb
            where sb.id in (select u.shared_bill_id from shared_bill_users u where u.user_id = :userId)
""", nativeQuery = true)
    List<SharedBill> findAllByUserId(Long userId);
}
