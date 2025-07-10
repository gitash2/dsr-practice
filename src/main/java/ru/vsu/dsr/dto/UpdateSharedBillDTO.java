package ru.vsu.dsr.dto;

import java.util.List;

public record UpdateSharedBillDTO(
        Long sharedBillId,
        List<Long> updatedUsers
) {
}
