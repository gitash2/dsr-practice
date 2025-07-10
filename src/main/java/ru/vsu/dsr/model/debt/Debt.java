package ru.vsu.dsr.model.debt;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "debts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Debt {
    @EmbeddedId
    private DebtId id;
    private BigDecimal amount;
}
