package org.mrpaulwoods.sec06;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mrpaulwoods.models.sec06.BalanceCheckRequest;
import org.slf4j.Logger;

public class Lec01UnaryBlockingClientTest extends AbstractTest{

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec01UnaryBlockingClientTest.class);

    @Test
    public void getBalanceTest() {
        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();

        var balance = this.blockingStub.getAccountBalance(request);

        log.info("unary balance received: {}", balance);

        Assertions.assertEquals(100, balance.getBalance());
    }
}
