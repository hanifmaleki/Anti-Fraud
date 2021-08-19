package antifraud.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionQueryRequest {
    @NotNull
    private Transaction transaction;

    @NotNull
    private Integer countryCount;

    @NotNull
    private Integer ipCount;
}
