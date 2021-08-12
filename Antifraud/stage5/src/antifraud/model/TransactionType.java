package antifraud.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//TODO Add validation
public class TransactionType {
    private String name;
    private Integer maxAllowed; //TODO BigDecimal
    private Integer maxManuall; //TODO BigDecimal
}
