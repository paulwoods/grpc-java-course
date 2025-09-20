package org.mrpaulwoods.sec06;

import com.google.protobuf.Empty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mrpaulwoods.models.sec06.BalanceCheckRequest;
import org.slf4j.Logger;

public class Lec01UnaryBlockingClientTest extends AbstractTest {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec01UnaryBlockingClientTest.class);

    @Test
    public void getBalanceTest() {
        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();

        var balance = this.bankBlockingStub.getAccountBalance(request);

        log.info("unary balance received: {}", balance);

        Assertions.assertEquals(100, balance.getBalance());
    }

    @Test
    public void getAllAccountsTest() {
        var all = this.bankBlockingStub.getAllAccounts(Empty.getDefaultInstance());
        log.info("all received: {}", all);

        Assertions.assertEquals(10, all.getAccountsCount());
        Assertions.assertEquals(1, all.getAccounts(0).getAccountNumber());
        Assertions.assertEquals(100, all.getAccounts(0).getBalance());
        Assertions.assertEquals(10, all.getAccounts(9).getAccountNumber());
        Assertions.assertEquals(100, all.getAccounts(9).getBalance());
    }

}
