package ru.vsu.dsr.dto;

import java.util.List;

public record CreateSharedBillDTO(
        String name,
        List<Long> users
) {
}
