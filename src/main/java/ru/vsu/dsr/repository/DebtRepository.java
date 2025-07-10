package ru.vsu.dsr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.vsu.dsr.model.debt.Debt;

public interface DebtRepository extends JpaRepository<Debt, Long> {

    @Query(
            """
            select d from Debt d
            where d.id.userId = :userId and d.id.expenseId = :expenseId
            """
    )
    Debt findDebtByUserIdAndExpenseId(long userId, long expenseId);


}
