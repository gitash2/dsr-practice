package ru.vsu.dsr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.vsu.dsr.dto.DebtPaymentDTO;
import ru.vsu.dsr.dto.sharedBill.ExpenseShareDTO;
import ru.vsu.dsr.dto.sharedBill.ExpenseShareResponse;
import ru.vsu.dsr.model.DebtPayment;
import ru.vsu.dsr.model.ExpenseShare;
import ru.vsu.dsr.model.SharedBill;
import ru.vsu.dsr.model.User;
import ru.vsu.dsr.model.debt.Debt;
import ru.vsu.dsr.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DebtService {
    private final DebtRepository debtRepository;
    private final UserRepository userRepository;
    private final SharedBillRepository sharedBillRepository;
    private final SharedBillUserRepository sharedBillUserRepository;
    private final DebtPaymentRepository debtPaymentRepository;
    private final ExpenseShareRepository expenseShareRepository;

    public boolean payDebt(UserDetails user, DebtPaymentDTO dto) {
        User userEntity = userRepository.findByUsername(user.getUsername()).orElseThrow();
        Long userId = userEntity.getId();
        Debt curDebt = debtRepository.findDebtByUserIdAndExpenseId(userId, dto.expenseId());
        if (curDebt == null|| curDebt.getAmount().compareTo(dto.amount()) < 0) {
            return false;
        } else {
            curDebt.setAmount(curDebt.getAmount().subtract(dto.amount()));
            debtRepository.save(curDebt);
            return true;
        }
    }

    public void recordPayment(Long sharedBillId, String username, DebtPaymentDTO request) {
        User fromUser = userRepository.findById(request.toUserId())
                .orElseThrow();

        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }

        SharedBill bill = sharedBillRepository.findById(sharedBillId)
                .orElseThrow();

        if (!sharedBillUserRepository.existsBySharedBillIdAndUserId(sharedBillId, fromUser.getId())) {
            throw new AccessDeniedException("You are not a participant of this bill.");
        }

        if (!sharedBillUserRepository.existsBySharedBillIdAndUserId(sharedBillId, request.toUserId())) {
            throw new IllegalArgumentException("Recipient is not a participant of this bill.");
        }

        if (fromUser.getId().equals(request.toUserId())) {
            throw new IllegalArgumentException("You cannot pay yourself.");
        }

        User toUser = userRepository.findById(request.toUserId())
                .orElseThrow();

        DebtPayment payment = DebtPayment.builder()
                .sharedBill(bill)
                .fromUser(fromUser)
                .toUser(toUser)
                .amount(request.amount())
                .paidAt(LocalDateTime.now())
                .build();

        debtPaymentRepository.save(payment);
    }

    public List<ExpenseShareDTO> getDebts(Long id) {
        /*List<ExpenseShare> expenseShares = expenseShareRepository.getExpenseSharesBySharedBillId(id);
        return expenseShares.stream().map(it -> new ExpenseShareResponse(

        ));*/
        return null;
    }
}
