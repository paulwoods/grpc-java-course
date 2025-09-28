package org.mrpaulwoods.sec11;

import com.google.common.util.concurrent.Uninterruptibles;
import io.grpc.Deadline;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mrpaulwoods.common.ResponseObserver;
import org.mrpaulwoods.models.sec11.Money;
import org.mrpaulwoods.models.sec11.WithdrawRequest;

import java.util.concurrent.TimeUnit;

public class Lec02ServerStreamingDeadlineTest extends AbstractTest {

//    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec02ServerStreamingDeadlineTest.class);

    @Test
    public void blockingDeadlineTest() {

        var ex = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            var request = WithdrawRequest.newBuilder()
                    .setAccountNumber(1)
                    .setAmount(50)
                    .build();

            var iterator = this.bankBlockingStub
                    .withDeadline(Deadline.after(2, TimeUnit.SECONDS))
                    .withdraw(request);

            while (iterator.hasNext()) {
                iterator.next();
            }
        });

        Assertions.assertEquals(Status.Code.DEADLINE_EXCEEDED, ex.getStatus().getCode());
        Uninterruptibles.sleepUninterruptibly(10, TimeUnit.SECONDS);
    }

    @Test
    public void blockingAsyncDeadlineTest() {
        var observer = ResponseObserver.<Money>create();

        var request = WithdrawRequest.newBuilder()
                .setAccountNumber(1)
                .setAmount(50)
                .build();

        bankStub
                .withDeadline(Deadline.after(2, TimeUnit.SECONDS))
                .withdraw(request, observer);

        observer.await();

        Assertions.assertEquals(2, observer.getItems().size());
        Assertions.assertEquals(Status.Code.DEADLINE_EXCEEDED,
                Status.fromThrowable(observer.getThrowable()).getCode());
    }
}
