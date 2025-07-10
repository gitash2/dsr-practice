package ru.vsu.dsr.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.vsu.dsr.dto.CreateSharedBillDTO;
import ru.vsu.dsr.dto.UpdateSharedBillDTO;
import ru.vsu.dsr.dto.debt.DebtDTO;
import ru.vsu.dsr.dto.sharedBill.*;
import ru.vsu.dsr.model.SharedBill;
import ru.vsu.dsr.service.DebtService;
import ru.vsu.dsr.service.ExpenseService;
import ru.vsu.dsr.service.SharedBillService;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequestMapping("/shared-bills")
@RequiredArgsConstructor
public class SharedBillController {
    private final SharedBillService sharedBillService;
    private final ExpenseService expenseService;
    private final DebtService debtService;

    @PostMapping
    public ResponseEntity<SharedBillDTO> createSharedBill(@AuthenticationPrincipal UserDetails user, @RequestBody CreateSharedBillDTO dto) {
        return ok(sharedBillService.createSharedBill(user, dto));
    }

    @PatchMapping
    public ResponseEntity<SharedBill> updateSharedBill(@AuthenticationPrincipal UserDetails user, @RequestBody UpdateSharedBillDTO dto) {
        return ok(sharedBillService.updateSharedBill(dto));
    }

    @DeleteMapping
    public boolean deleteSharedBill(@AuthenticationPrincipal UserDetails user, @RequestParam Long sharedBillId) {
        return sharedBillService.deleteSharedBill(user, sharedBillId);
    }

    @GetMapping
    public List<SharedBillResponse> getSharedBills(@AuthenticationPrincipal UserDetails user) {
        return sharedBillService.getSharedBills(user);
    }

    @GetMapping("{id}")
    public ResponseEntity<SharedBillShortDTO> getSharedBill(@PathVariable Long id) {
        return ok(sharedBillService.getSharedBill(id));
    }

    @GetMapping("/{id}/expenses")
    public ResponseEntity<List<ExpenseOutput>> getExpenses(@AuthenticationPrincipal UserDetails user, @PathVariable Long id) {
        return ok(sharedBillService.getAllExpensesInABill(user, id));
    }

    @PostMapping("/{id}/expense")
    public ResponseEntity<ExpenseDTO> createExpense(@AuthenticationPrincipal UserDetails user, @PathVariable Long id, @RequestBody ExpenseDTO expenseDTO) {
        return ok(expenseService.createExpense(user, id, expenseDTO));
    }

    @GetMapping("/{id}/debts")
    public ResponseEntity<?> getDebts(@PathVariable Long id) {
        return ok(sharedBillService.getAllDebtsInABill(id));
    }

    @PostMapping("/{id}/pay")
    public void payDebt(@RequestBody DebtDTO dto, @PathVariable Long id) {
        sharedBillService.payDebt(dto, id);
    }

}
