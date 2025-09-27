package org.mrpaulwoods.sec10;

import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mrpaulwoods.common.ResponseObserver;
import org.mrpaulwoods.models.sec10.AccountBalance;
import org.mrpaulwoods.models.sec10.BalanceCheckRequest;
import org.mrpaulwoods.models.sec10.ValidationCode;

public class Lec01UnaryInputValidationTest extends AbstractTest {

    @Test
    public void blockingInputValidationTest() {
        var ex = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            var request = BalanceCheckRequest.newBuilder().setAccountNumber(11).build();
            this.bankBlockingStub.getAccountBalance(request);
        });
        Assertions.assertEquals(ValidationCode.INVALID_ACCOUNT, getValidationCode(ex));
    }

    @Test
    public void asyncInputValidationTest() {
        var observer = ResponseObserver.<AccountBalance>create();
        var request = BalanceCheckRequest.newBuilder().setAccountNumber(11).build();
        this.bankStub.getAccountBalance(request, observer);
        observer.await();

        Assertions.assertTrue(observer.getItems().isEmpty());
        Assertions.assertNotNull(observer.getThrowable());
        Assertions.assertEquals(ValidationCode.INVALID_ACCOUNT, getValidationCode(observer.getThrowable()));
    }

}
