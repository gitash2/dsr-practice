package ru.vsu.dsr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.dsr.dto.sharedBill.ExpenseDTO;
import ru.vsu.dsr.model.Expense;
import ru.vsu.dsr.model.ExpenseShare;
import ru.vsu.dsr.model.SharedBill;
import ru.vsu.dsr.model.User;
import ru.vsu.dsr.repository.ExpenseRepository;
import ru.vsu.dsr.repository.ExpenseShareRepository;
import ru.vsu.dsr.repository.SharedBillRepository;
import ru.vsu.dsr.repository.UserRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final SharedBillRepository sharedBillRepository;
    private final ExpenseShareRepository expenseShareRepository;

    @Transactional
    public ExpenseDTO createExpense(UserDetails user, Long id, ExpenseDTO expenseDTO) {
        User curUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        SharedBill sharedBill = sharedBillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shared bill not found"));

        Expense expense = new Expense();
        expense.setDescription(expenseDTO.description());
        expense.setAmount(expenseDTO.amount());
        expense.setCreatedAt(LocalDateTime.now());
        expense.setSharedBill(sharedBill);
        expense.setPaidBy(curUser);

        expense = expenseRepository.save(expense);

        Expense finalExpense = expense;

        BigDecimal share = expenseDTO.amount().divide(BigDecimal.valueOf(expenseDTO.debtors().size()), 2, RoundingMode.HALF_UP);
        List<ExpenseShare> expenseShares = expenseDTO.debtors().stream().map(it -> {
            User debtor = userRepository.findById(it).orElseThrow();
            ExpenseShare s = new ExpenseShare();
            s.setExpense(finalExpense);
            s.setUser(debtor);
            s.setShareAmount(share);
            return s;
        }).collect(Collectors.toCollection(ArrayList::new));

        expense.setShares(expenseShares);
        expenseRepository.save(expense);
        expenseShareRepository.saveAll(expenseShares);

        return new ExpenseDTO(
                expense.getDescription(),
                expense.getAmount(),
                expenseDTO.debtors()
        );
    }


    public void deleteExpense(Expense expense) {
        expenseRepository.delete(expense);
    }

    public Expense getExpenseById(Long expenseId) {
        return expenseRepository.findById(expenseId).orElse(null);
    }
}
