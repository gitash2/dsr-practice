package ru.vsu.dsr.model;


import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SharedBillUserId implements Serializable {

    private Long sharedBillId;
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SharedBillUserId that)) return false;
        return Objects.equals(sharedBillId, that.sharedBillId)
                && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sharedBillId, userId);
    }
}
