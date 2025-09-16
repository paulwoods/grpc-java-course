package org.mrpaulwoods.sec06;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.mrpaulwoods.models.sec06.AccountBalance;
import org.mrpaulwoods.models.sec06.BalanceCheckRequest;
import org.mrpaulwoods.models.sec06.BankServiceGrpc;
import org.slf4j.Logger;

import java.time.Duration;

public class GrpcClient {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(GrpcClient.class);

    public static void main(String[] args) {

        var channel = ManagedChannelBuilder
                .forAddress("localhost", 6565)
                .usePlaintext()
                .build();

        var stub1 = BankServiceGrpc.newBlockingStub(channel);

        var balance1 = stub1.getAccountBalance(BalanceCheckRequest.newBuilder().setAccountNumber(1).build());

        log.info("balance1: {}", balance1);

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


        try {
            Thread.sleep(Duration.ofSeconds(1));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
