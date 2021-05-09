package antifraud.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.InetAddress;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private Integer amount;
    private String ipAddress;
    private String cardSerial;
}
