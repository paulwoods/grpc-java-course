package org.mrpaulwoods.sec10;

import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mrpaulwoods.common.ResponseObserver;
import org.mrpaulwoods.models.sec10.Money;
import org.mrpaulwoods.models.sec10.ValidationCode;
import org.mrpaulwoods.models.sec10.WithdrawRequest;

import java.util.stream.Stream;

public class Lec02ServerStreamingInputValidationTest extends AbstractTest {

    @ParameterizedTest
    @MethodSource("testData")
    public void blockingInputValidationTest(WithdrawRequest request, ValidationCode code) {
        var ex = Assertions.assertThrows(StatusRuntimeException.class, () ->
                this.bankBlockingStub.withdraw(request).hasNext()
        );
        Assertions.assertEquals(code, getValidationCode(ex));
    }

    @ParameterizedTest
    @MethodSource("testData")
    public void asyncInputValidationTest(WithdrawRequest request, ValidationCode code) {
        var observer = ResponseObserver.<Money>create();
        this.bankStub.withdraw(request, observer);
        observer.await();

        Assertions.assertTrue(observer.getItems().isEmpty());
        Assertions.assertNotNull(observer.getThrowable());
        Assertions.assertEquals(code, getValidationCode(observer.getThrowable()));
    }

    private Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(
                        WithdrawRequest.newBuilder().setAccountNumber(11).setAmount(10).build(),
                        ValidationCode.INVALID_ACCOUNT
                ),
                Arguments.of(
                        WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(17).build(),
                        ValidationCode.INVALID_AMOUNT
                ),
                Arguments.of(
                        WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(120).build(),
                        ValidationCode.INSUFFICIENT_BALANCE
                )
        );

    }

}
