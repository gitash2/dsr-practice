package ru.vsu.dsr.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.vsu.dsr.dto.DebtPaymentDTO;
import ru.vsu.dsr.service.DebtService;

import java.nio.file.attribute.UserPrincipal;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/debts")
@RequiredArgsConstructor
public class DebtController {

    private final DebtService debtPaymentService;

    @PostMapping("/{billId}")
    public ResponseEntity<Void> payDebt(
            @PathVariable Long billId,
            @RequestBody DebtPaymentDTO request,
            @AuthenticationPrincipal UserDetails user
    ) {
        debtPaymentService.recordPayment(billId, user.getUsername(), request);
        return ResponseEntity.ok().build();
    }
}

