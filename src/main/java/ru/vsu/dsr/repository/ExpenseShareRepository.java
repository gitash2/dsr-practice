package ru.vsu.dsr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.vsu.dsr.model.Expense;
import ru.vsu.dsr.model.ExpenseShare;

import java.util.List;

public interface ExpenseShareRepository extends JpaRepository<ExpenseShare, Long> {

    @Query("""
    select e from ExpenseShare e
    where e.expense.sharedBill.id = :sharedBillId
    """)
    List<ExpenseShare> getExpenseSharesBySharedBillId(Long sharedBillId);
}
