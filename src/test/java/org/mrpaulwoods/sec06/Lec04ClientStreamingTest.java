package org.mrpaulwoods.sec06;

import com.google.common.util.concurrent.Uninterruptibles;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mrpaulwoods.common.ResponseObserver;
import org.mrpaulwoods.models.sec06.AccountBalance;
import org.mrpaulwoods.models.sec06.DepositRequest;
import org.mrpaulwoods.models.sec06.Money;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Lec04ClientStreamingTest extends AbstractTest {

    @Test
    public void depositTest() {

        var responseObserver = ResponseObserver.<AccountBalance>create();
        var requestObserver = this.stub.deposit(responseObserver);

        // initial message - account number
        requestObserver.onNext(DepositRequest.newBuilder()
                .setAccountNumber(5)
                .build());

        // sending stream of deposits
        IntStream.rangeClosed(1, 10)
                .mapToObj(i -> Money.newBuilder().setAmount(10).build())
                .map(m -> DepositRequest.newBuilder().setMoney(m).build())
                .forEach(requestObserver::onNext);

        // sending complete
        requestObserver.onCompleted();

        // get the response
        responseObserver.await();

        Assertions.assertEquals(1, responseObserver.getItems().size());
        Assertions.assertEquals(200, responseObserver.getItems().getFirst().getBalance());
        Assertions.assertNull(responseObserver.getThrowable());

    }

    @Test
    public void cancelStreamTest() {

        var responseObserver = ResponseObserver.<AccountBalance>create();
        var requestObserver = this.stub.deposit(responseObserver);

        // initial message - account number
        requestObserver.onNext(DepositRequest.newBuilder()
                .setAccountNumber(5)
                .build());

        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);

        // cancel by sending error
        requestObserver.onError(new RuntimeException("user canceled"));

        // get the response
        responseObserver.await();

        Assertions.assertEquals(0, responseObserver.getItems().size());

        Assertions.assertEquals("CANCELLED: Cancelled by client with StreamObserver.onError()", responseObserver.getThrowable().getMessage());
        Assertions.assertEquals("user canceled", responseObserver.getThrowable().getCause().getMessage());
    }

}
