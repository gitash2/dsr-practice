package ru.vsu.dsr.dto.debt;

import java.math.BigDecimal;

public record DebtDTO(
        String fromUser,
        String toUser,
        BigDecimal amount
) {
}
