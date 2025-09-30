package org.mrpaulwoods.sec12;

import org.junit.jupiter.api.Test;
import org.mrpaulwoods.common.ResponseObserver;
import org.mrpaulwoods.models.sec12.Money;
import org.mrpaulwoods.models.sec12.WithdrawRequest;

import java.util.concurrent.Executors;

public class Lec02ExecutorCallOptionsTest extends AbstractTest {

    /**
     * look at the logs
     * The "received item" lines are from the client, and all have different virtual thread ids.
     */
    @Test
    public void executorDemo() {
        var observer = ResponseObserver.<Money>create();
        var request = WithdrawRequest.newBuilder()
                .setAccountNumber(1)
                .setAmount(30)
                .build();
        this.bankStub
                .withExecutor(Executors.newVirtualThreadPerTaskExecutor())
                .withdraw(request, observer);
        observer.await();
    }

}
