package antifraud.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Setter
@Getter
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
