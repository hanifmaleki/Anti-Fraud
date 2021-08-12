package antifraud.model;

import lombok.Data;

@Data
public class TransactionQueryRequest {
    private Transaction transaction;
    private int countryCount;
    private int ipCount;
}
