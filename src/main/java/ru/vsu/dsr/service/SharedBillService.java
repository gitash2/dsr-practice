package ru.vsu.dsr.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.dsr.dto.CreateSharedBillDTO;
import ru.vsu.dsr.dto.UpdateSharedBillDTO;
import ru.vsu.dsr.dto.UserShortDTO;
import ru.vsu.dsr.dto.debt.DebtDTO;
import ru.vsu.dsr.dto.debt.UserAmountDTO;
import ru.vsu.dsr.dto.sharedBill.*;
import ru.vsu.dsr.model.*;
import ru.vsu.dsr.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SharedBillService {
    private final SharedBillRepository sharedBillRepository;
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final DebtPaymentRepository debtPaymentRepository;

    @Transactional
    public SharedBillDTO createSharedBill(UserDetails userDetails, CreateSharedBillDTO dto) {
        User creator = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();

        SharedBill sharedBill = new SharedBill();
        sharedBill.setName(dto.name());
        sharedBill.setCreatedAt(LocalDateTime.now());
        sharedBill.setCreatedBy(creator);

        sharedBill = sharedBillRepository.save(sharedBill);

        List<Long> userIds = dto.users();
        List<User> users = userRepository.findAllById(userIds);

        SharedBill finalSharedBill = sharedBill;
        List<SharedBillUser> participants = users.stream()
                .map(user -> SharedBillUser.builder()
                        .id(new SharedBillUserId(finalSharedBill.getId(), user.getId()))
                        .sharedBill(finalSharedBill)
                        .user(user)
                        .build())
                .collect(Collectors.toList());

        SharedBillUser creatorParticipant = SharedBillUser.builder()
                .id(new SharedBillUserId(finalSharedBill.getId(), creator.getId()))
                .sharedBill(finalSharedBill)
                .user(creator)
                .build();

        participants.add(creatorParticipant);
        sharedBill.setParticipants(participants);
        SharedBill saved = sharedBillRepository.save(sharedBill);
        return new SharedBillDTO(
                saved.getId(),
                saved.getName(),
                saved.getCreatedAt(),
                saved.getCreatedBy().getUsername(),
                saved.getParticipants().stream().map(it -> it.getUser().getUsername()).toList(),
                null
        );
    }

    public SharedBill updateSharedBill(UpdateSharedBillDTO dto) {
        SharedBill sharedBill = sharedBillRepository.findById(dto.sharedBillId()).orElseThrow();

        List<User> users = userRepository.findAllById(dto.updatedUsers());

        sharedBill.getParticipants().clear();

        List<SharedBillUser> newParticipants = users.stream()
                .map(user -> SharedBillUser.builder()
                        .id(new SharedBillUserId(sharedBill.getId(), user.getId()))
                        .sharedBill(sharedBill)
                        .user(user)
                        .build())
                .toList();

        sharedBill.getParticipants().addAll(newParticipants);

        return sharedBillRepository.save(sharedBill);
    }


    public boolean deleteSharedBill(UserDetails user, Long sharedBillId) {
        User curUser = userRepository.findByUsername(user.getUsername()).orElseThrow();
        SharedBill sharedBill = sharedBillRepository.findById(sharedBillId).orElseThrow();
        if (!sharedBill.getCreatedBy().getId().equals(curUser.getId())) {
            return false;
        }
        sharedBillRepository.deleteById(sharedBillId);
        return true;
    }

    public List<SharedBillResponse> getSharedBills(UserDetails user) {
        User curUser = userRepository.findByUsername(user.getUsername()).orElseThrow();

        List<SharedBill> sharedBills = sharedBillRepository.findAllByUserId(curUser.getId());
        return sharedBills.stream().map(it -> new SharedBillResponse(it.getId(), it.getName())).toList();
    }

    public SharedBillShortDTO getSharedBill(Long sharedBillId) {
        SharedBill sharedBill = sharedBillRepository.findById(sharedBillId).orElseThrow();

        List<UserShortDTO> participantDTOs = sharedBill.getParticipants().stream()
                .map(link -> {
                    User user = link.getUser();
                    return new UserShortDTO(user.getId(), user.getUsername());
                })
                .toList();

        return new SharedBillShortDTO(
                sharedBill.getId(),
                sharedBill.getName(),
                sharedBill.getCreatedAt(),
                sharedBill.getCreatedBy().getUsername(),
                participantDTOs,
                null
        );
    }

    public List<ExpenseOutput> getAllExpensesInABill(UserDetails user, Long billId) {
        return expenseRepository.findAllBySharedBillId(billId)
                .stream()
                .map(expense -> new ExpenseOutput(
                        expense.getDescription(),
                        expense.getAmount(),
                        expense.getPaidBy().getUsername(),
                        expense.getShares()
                                .stream()
                                .map(share -> share.getUser().getUsername())
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    public List<DebtDTO> getAllDebtsInABill(Long sharedBillId) {
        SharedBill bill = sharedBillRepository.findById(sharedBillId)
                .orElseThrow(() -> new RuntimeException("Shared bill not found"));

        Map<Pair<User, User>, BigDecimal> debts = new HashMap<>();

        List<Expense> expenses = expenseRepository.findAllBySharedBillId(bill.getId());

        for (Expense expense : expenses) {
            User creditor = expense.getPaidBy();
            for (ExpenseShare share : expense.getShares()) {
                User debtor = share.getUser();
                if (!debtor.equals(creditor)) {
                    var key = Pair.of(debtor, creditor);
                    debts.put(key, debts.getOrDefault(key, BigDecimal.ZERO).add(share.getShareAmount()));
                }
            }
        }

        List<DebtPayment> payments = debtPaymentRepository.findBySharedBillId(sharedBillId);
        for (DebtPayment payment : payments) {
            var key = Pair.of(payment.getFromUser(), payment.getToUser());
            debts.put(key, debts.getOrDefault(key, BigDecimal.ZERO).subtract(payment.getAmount()));
        }

        return debts.entrySet().stream()
                .filter(entry -> entry.getValue().compareTo(BigDecimal.ZERO) > 0)
                .map(entry -> new DebtDTO(
                        entry.getKey().getLeft().getUsername(),
                        entry.getKey().getRight().getUsername(),
                        entry.getValue()
                ))
                .toList();
    }


    @Transactional
    public void payDebt(DebtDTO dto, Long billId) {
        User fromUser = userRepository.findByUsername(dto.fromUser())
                .orElseThrow(() -> new RuntimeException("From-user not found"));

        User toUser = userRepository.findByUsername(dto.toUser())
                .orElseThrow(() -> new RuntimeException("To-user not found"));

        SharedBill sharedBill = sharedBillRepository.findById(billId).orElseThrow();

        DebtPayment payment = new DebtPayment();
        payment.setSharedBill(sharedBill);
        payment.setFromUser(fromUser);
        payment.setToUser(toUser);
        payment.setAmount(dto.amount());
        payment.setPaidAt(LocalDateTime.now());

        debtPaymentRepository.save(payment);
    }

}
