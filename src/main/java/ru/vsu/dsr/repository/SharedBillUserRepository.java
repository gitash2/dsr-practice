package ru.vsu.dsr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.dsr.model.SharedBillUser;
import ru.vsu.dsr.model.SharedBillUserId;

import java.util.List;
import java.util.Optional;

public interface SharedBillUserRepository extends JpaRepository<SharedBillUser, SharedBillUserId> {

    List<SharedBillUser> findBySharedBillId(Long sharedBillId);

    boolean existsBySharedBillIdAndUserId(Long sharedBillId, Long userId);

    Optional<SharedBillUser> findBySharedBillIdAndUserId(Long sharedBillId, Long userId);
}
