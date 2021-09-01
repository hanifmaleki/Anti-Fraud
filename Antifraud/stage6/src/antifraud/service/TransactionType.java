package antifraud.service;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//TODO Add validation
public class TransactionType {
    @NotBlank
    private String name;
    @Positive
    private Integer maxAllowed; //TODO BigDecimal
    @Positive
    private Integer maxManuall; //TODO BigDecimal

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionType type = (TransactionType) o;

        return name.equals(type.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
