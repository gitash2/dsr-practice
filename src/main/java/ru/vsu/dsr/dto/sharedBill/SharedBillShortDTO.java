package ru.vsu.dsr.dto.sharedBill;

import ru.vsu.dsr.dto.UserShortDTO;
import ru.vsu.dsr.dto.debt.DebtDTO;

import java.time.LocalDateTime;
import java.util.List;

public record SharedBillShortDTO(
        Long id,
        String name,
        LocalDateTime createdAt,
        String createdBy,
        List<UserShortDTO> participants,
        List<DebtDTO> debts
) {
}
