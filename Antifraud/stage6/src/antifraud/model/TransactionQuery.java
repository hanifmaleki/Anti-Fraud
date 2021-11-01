package antifraud.model;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
public class TransactionQuery {
    @NotNull
    private Transaction transaction;

    @NotNull
    private Integer countryCount;

    @NotNull
    private Integer ipCount;
}
