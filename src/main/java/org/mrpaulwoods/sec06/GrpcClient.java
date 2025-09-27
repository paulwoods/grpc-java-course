package org.mrpaulwoods.sec06;

import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.mrpaulwoods.models.sec06.AccountBalance;
import org.mrpaulwoods.models.sec06.BalanceCheckRequest;
import org.mrpaulwoods.models.sec06.BankServiceGrpc;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GrpcClient {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(GrpcClient.class);

    static void main(String[] ignoredArgs) throws ExecutionException, InterruptedException, TimeoutException {

        var channel = ManagedChannelBuilder
                .forAddress("localhost", 6565)
                .usePlaintext()
                .build();

        // blocking stub
        var stub1 = BankServiceGrpc.newBlockingStub(channel);

        var balance1 = stub1.getAccountBalance(BalanceCheckRequest.newBuilder().setAccountNumber(1).build());

        log.info("balance1: {}", balance1);

        // async stub
        var stub2 = BankServiceGrpc.newStub(channel);

        stub2.getAccountBalance(BalanceCheckRequest.newBuilder().setAccountNumber(2).build(),
                new StreamObserver<>() {
                    @Override
                    public void onNext(AccountBalance balance2) {
                        log.info("balance2: {}", balance2);
                    }

                    @Override
                    public void onError(Throwable ex) {
                        log.error(ex.getMessage(), ex);
                    }

                    @Override
                    public void onCompleted() {
                        log.info("completed");
                    }
                });

        // future stub
        var stub3 = BankServiceGrpc.newFutureStub(channel);

        ListenableFuture<AccountBalance> accountBalance = stub3.getAccountBalance(
                BalanceCheckRequest.newBuilder().setAccountNumber(3).build());
        AccountBalance balance3 = accountBalance.get(1, TimeUnit.SECONDS);

        log.info("balance3: {}", balance3);
        try {
            Thread.sleep(Duration.ofSeconds(1));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
