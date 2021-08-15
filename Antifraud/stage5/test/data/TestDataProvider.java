package data;

public class TestDataProvider {

    public static final String BASE_ADDRESS = "/api/antifraud";


    public final TransactionTypeDataProvider trxType;
    public final TransactionDataProvider transaction;
    public final UserDataProvider user;
    public final IpAndCardDataProvider ipCard;

    public TestDataProvider() {
        this.trxType = new TransactionTypeDataProvider();
        this.transaction = new TransactionDataProvider(this);
        this.user = new UserDataProvider();
        this.ipCard = new IpAndCardDataProvider();

    }

}
