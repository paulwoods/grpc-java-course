package org.mrpaulwoods.sec06;

import com.google.protobuf.Empty;
import org.junit.jupiter.api.Test;
import org.mrpaulwoods.common.ResponseObserver;
import org.mrpaulwoods.models.sec06.AccountBalance;
import org.mrpaulwoods.models.sec06.AllAccountsResponse;
import org.mrpaulwoods.models.sec06.BalanceCheckRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Lec02UnaryAsyncClientTest extends AbstractTest {

//    private static final Logger log = getLogger(Lec02UnaryAsyncClientTest.class);

    @Test
    public void getBalanceTest() {
        var request = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
        var observer = ResponseObserver.<AccountBalance>create();
        this.stub.getAccountBalance(request, observer);
        observer.await();

        assertEquals(1, observer.getItems().size());
        assertEquals(100, observer.getItems().getFirst().getBalance());
        assertNull(observer.getThrowable());
    }

    @Test
    public void allAccountsTest() {
        var observer = ResponseObserver.<AllAccountsResponse>create();
        this.stub.getAllAccounts(Empty.getDefaultInstance(), observer);
        observer.await();
        assertEquals(1, observer.getItems().size());
        assertEquals(10, observer.getItems().getFirst().getAccountsCount());
        assertNull(observer.getThrowable());
    }

}
