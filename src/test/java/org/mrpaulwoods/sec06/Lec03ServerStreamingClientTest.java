package org.mrpaulwoods.sec06;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mrpaulwoods.models.sec06.Money;
import org.mrpaulwoods.models.sec06.WithdrawRequest;
import org.slf4j.Logger;

import java.util.Iterator;

public class Lec03ServerStreamingClientTest extends AbstractTest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec03ServerStreamingClientTest.class);

    @Test
    public void blockingClientWithdrawTest() {
        var request = WithdrawRequest.newBuilder()
                .setAccountNumber(2)
                .setAmount(20)
                .build();

        Iterator<Money> iterator = this.blockingStub.withdraw(request);
        int count = 0;
        while(iterator.hasNext()) {
            log.info("received money: {}", iterator.next());
            count++;
        }
        Assertions.assertEquals(2, count);
    }

}
