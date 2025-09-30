package org.mrpaulwoods.sec12;

import io.grpc.ClientInterceptor;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mrpaulwoods.common.ResponseObserver;
import org.mrpaulwoods.models.sec12.AccountBalance;
import org.mrpaulwoods.models.sec12.BalanceCheckRequest;
import org.mrpaulwoods.sec12.interceptors.DeadlineInterceptor;

import java.time.Duration;
import java.util.List;

public class Lec03DeadlineTest extends AbstractInterceptorTest {

    @Override
    protected List<ClientInterceptor> getClientInterceptors() {
        return List.of(new DeadlineInterceptor(Duration.ofSeconds(2)));
    }

    @Test
    public void blockingDeadlineTest() {

        var ex = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            var request = BalanceCheckRequest.newBuilder()
                    .setAccountNumber(1)
                    .build();

            bankBlockingStub
                    .getAccountBalance(request);
        });

        Assertions.assertEquals(Status.DEADLINE_EXCEEDED.getCode(), ex.getStatus().getCode());
    }

    @Test
    public void asyncDeadlineTest() {
        var observer = ResponseObserver.<AccountBalance>create();

        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();

        bankStub
                .getAccountBalance(request, observer);

        observer.await();

        Assertions.assertTrue(observer.getItems().isEmpty());
        Assertions.assertEquals(Status.Code.DEADLINE_EXCEEDED,
                Status.fromThrowable(observer.getThrowable()).getCode());

        // you'll see "received error: DEADLINE_EXCEEDED" in the logs
    }


}
