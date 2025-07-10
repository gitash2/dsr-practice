package ru.vsu.dsr.dto;

import java.math.BigDecimal;

public record DebtPaymentDTO(
        Long expenseId,
        BigDecimal amount,
        Long toUserId

) {
}
