package antifraud.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Positive
    private Integer amount;
    @NotBlank
    private String ipAddress;
    @NotBlank
    private String cardSerial;
    @NotNull
    private String type;
}
