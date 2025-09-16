package org.mrpaulwoods.sec06;

import io.grpc.ManagedChannelBuilder;
import org.mrpaulwoods.models.sec06.BalanceCheckRequest;
import org.mrpaulwoods.models.sec06.BankServiceGrpc;
import org.slf4j.Logger;

public class GrpcClient {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(GrpcClient.class);

    public static void main(String[] args) {

        var channel = ManagedChannelBuilder
                .forAddress("localhost", 6565)
                .usePlaintext()
                .build();

        var stub = BankServiceGrpc.newBlockingStub(channel);

        var balance = stub.getAccountBalance(BalanceCheckRequest.newBuilder().setAccountNumber(2).build());

        log.info("balance: {}", balance);

    }

}
