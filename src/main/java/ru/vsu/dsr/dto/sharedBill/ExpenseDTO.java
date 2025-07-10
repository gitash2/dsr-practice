package ru.vsu.dsr.dto.sharedBill;

import java.math.BigDecimal;
import java.util.List;

public record ExpenseDTO(
        String description,
        BigDecimal amount,
        List<Long> debtors
) {
}
