package ru.vsu.dsr.dto.sharedBill;

import java.math.BigDecimal;

public record ExpenseShareResponse(
        String fromUser,
        String toUser,
        BigDecimal amount
) {
}
