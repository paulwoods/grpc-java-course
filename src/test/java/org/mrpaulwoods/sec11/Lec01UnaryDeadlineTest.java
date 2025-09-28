package org.mrpaulwoods.sec11;

import io.grpc.Deadline;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mrpaulwoods.common.ResponseObserver;
import org.mrpaulwoods.models.sec11.AccountBalance;
import org.mrpaulwoods.models.sec11.BalanceCheckRequest;

import java.util.concurrent.TimeUnit;

public class Lec01UnaryDeadlineTest extends AbstractTest {

//    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec01UnaryDeadlineTest.class);

    @Test
    public void blockingDeadlineTest() {

        var ex = Assertions.assertThrows(StatusRuntimeException.class, () -> {

            var request = BalanceCheckRequest.newBuilder()
                    .setAccountNumber(1)
                    .build();

            bankBlockingStub
                    .withDeadline(Deadline.after(2, TimeUnit.SECONDS))
                    .getAccountBalance(request);

        });

        Assertions.assertEquals(Status.Code.DEADLINE_EXCEEDED, ex.getStatus().getCode());
    }

    @Test
    public void blockingAsyncDeadlineTest() {
        var observer = ResponseObserver.<AccountBalance>create();

        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();

        bankStub
                .withDeadline(Deadline.after(2, TimeUnit.SECONDS))
                .getAccountBalance(request, observer);

        observer.await();

        Assertions.assertTrue(observer.getItems().isEmpty());
        Assertions.assertEquals(Status.Code.DEADLINE_EXCEEDED,
                Status.fromThrowable(observer.getThrowable()).getCode());
    }

}
