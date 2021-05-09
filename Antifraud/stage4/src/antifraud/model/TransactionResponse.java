package antifraud.model;

import lombok.Data;

@Data
public class TransactionResponse {
    private ResultEnum result;
    private String message;
}
