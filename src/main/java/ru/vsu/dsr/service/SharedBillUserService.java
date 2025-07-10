package ru.vsu.dsr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vsu.dsr.model.SharedBill;
import ru.vsu.dsr.model.SharedBillUser;
import ru.vsu.dsr.model.SharedBillUserId;
import ru.vsu.dsr.model.User;
import ru.vsu.dsr.repository.SharedBillUserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SharedBillUserService {

    private final SharedBillUserRepository sharedBillUsersRepository;

    public void addUserToSharedBill(SharedBill bill, User user, String role) {
        SharedBillUserId id = new SharedBillUserId(bill.getId(), user.getId());

        if (sharedBillUsersRepository.existsById(id)) {
            throw new IllegalStateException("User already in the shared bill");
        }

        SharedBillUser sbUser = SharedBillUser.builder()
                .id(id)
                .sharedBill(bill)
                .user(user)
                .build();

        sharedBillUsersRepository.save(sbUser);
    }

    public List<SharedBillUser> getParticipants(Long sharedBillId) {
        return sharedBillUsersRepository.findBySharedBillId(sharedBillId);
    }

    public boolean isUserInBill(Long sharedBillId, Long userId) {
        return sharedBillUsersRepository.existsBySharedBillIdAndUserId(sharedBillId, userId);
    }


}
