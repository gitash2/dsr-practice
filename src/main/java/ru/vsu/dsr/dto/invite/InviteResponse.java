package ru.vsu.dsr.dto.invite;

public record InviteResponse(
        Long inviteId,
        Long sharedBillId,
        String sharedBillName,
        Long invitedUserId,
        String status
) {
}
