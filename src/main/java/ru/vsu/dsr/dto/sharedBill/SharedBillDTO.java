package ru.vsu.dsr.dto.sharedBill;

import ru.vsu.dsr.dto.debt.DebtDTO;

import java.time.LocalDateTime;
import java.util.List;

public record SharedBillDTO(
        Long id,
        String name,
        LocalDateTime createdAt,
        String createdBy,
        List<String> users,
        List<DebtDTO> debts
) {
}
