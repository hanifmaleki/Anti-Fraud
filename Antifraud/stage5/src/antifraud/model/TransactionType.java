package antifraud.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
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
}
