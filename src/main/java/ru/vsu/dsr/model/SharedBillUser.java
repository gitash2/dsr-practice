package ru.vsu.dsr.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shared_bill_users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"shared_bill_id", "user_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedBillUser {

    @EmbeddedId
    private SharedBillUserId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("sharedBillId")
    @JoinColumn(name = "shared_bill_id", nullable = false)
    private SharedBill sharedBill;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

