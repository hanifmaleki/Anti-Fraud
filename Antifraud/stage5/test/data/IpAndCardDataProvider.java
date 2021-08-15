package data;

import static data.TestDataProvider.BASE_ADDRESS;

public class IpAndCardDataProvider {

    public static final String IP_ADDRESS = BASE_ADDRESS + "/stolencard";
    public static final String STOLEN_ADDRESS = BASE_ADDRESS + "/suspicious-ip";


    // Cards
    public final String stolenCard1;
    public final String stolenCard2;
    public final String okCard;

    // IPs
    public final String suspiciousIp1;
    public final String suspiciousIp2;
    public final String okIp;

    public IpAndCardDataProvider() {

        stolenCard1 = "2223-0031-2200-3222";
        stolenCard2 = "5200-8282-8282-8210";
        okCard = "5105-1051-0510-5100";

        suspiciousIp1 = "192.168.0.12";
        suspiciousIp2 = "198.18.0.6";
        okIp = "172.16.0.9";
    }
}
