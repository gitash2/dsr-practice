package ru.vsu.dsr.dto.sharedBill;

import java.math.BigDecimal;
import java.util.List;

public record ExpenseOutput(
        String description,
        BigDecimal amount,
        String payer,
        List<String> debtors
) {}

