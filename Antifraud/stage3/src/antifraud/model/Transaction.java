package antifraud.model;

import lombok.Data;

import java.net.InetAddress;

@Data
public class Transaction {
    private Integer amount;
    private InetAddress ipAddress;
    private String cardSerial;
}
