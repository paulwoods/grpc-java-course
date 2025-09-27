package org.mrpaulwoods.sec09;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mrpaulwoods.common.ResponseObserver;
import org.mrpaulwoods.models.sec09.Money;
import org.mrpaulwoods.models.sec09.WithdrawRequest;

import java.util.stream.Stream;

public class Lec02ServerStreamingInputValidationTest extends AbstractTest {

    @ParameterizedTest
    @MethodSource("testData")
    public void blockingInputValidationTest(WithdrawRequest request, Status.Code code) {
        var ex = Assertions.assertThrows(StatusRuntimeException.class, () ->
                this.bankBlockingStub.withdraw(request).hasNext()
        );
        Assertions.assertEquals(code, ex.getStatus().getCode());
    }

    @ParameterizedTest
    @MethodSource("testData")
    public void asyncInputValidationTest(WithdrawRequest request, Status.Code code) {
        var observer = ResponseObserver.<Money>create();
        this.bankStub.withdraw(request, observer);
        observer.await();

        Assertions.assertTrue(observer.getItems().isEmpty());
        Assertions.assertNotNull(observer.getThrowable());
        Assertions.assertEquals(code, ((StatusRuntimeException) observer.getThrowable()).getStatus().getCode());
    }

    private Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(
                        WithdrawRequest.newBuilder().setAccountNumber(11).setAmount(10).build(),
                        Status.Code.INVALID_ARGUMENT
                ),
                Arguments.of(
                        WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(17).build(),
                        Status.Code.INVALID_ARGUMENT
                ),
                Arguments.of(
                        WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(120).build(),
                        Status.Code.FAILED_PRECONDITION
                )
        );

    }

}
