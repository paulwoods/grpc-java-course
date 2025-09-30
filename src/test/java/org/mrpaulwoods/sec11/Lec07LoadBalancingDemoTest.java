package org.mrpaulwoods.sec11;

import com.google.common.util.concurrent.Uninterruptibles;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mrpaulwoods.common.ResponseObserver;
import org.mrpaulwoods.models.sec06.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec07LoadBalancingDemoTest {

    private static final Logger log = LoggerFactory.getLogger(Lec07LoadBalancingDemoTest.class);
    private BankServiceGrpc.BankServiceBlockingStub bankBlockingStub;
    private BankServiceGrpc.BankServiceStub bankStub;
    private ManagedChannel channel;

    /*
        run docker compose up on the nginx load balancing file
        run DemoLoadBalanced.bankService1
        run DemoLoadBalanced.bankService2
        run this test class.

     */

    @BeforeAll
    public void setup() {
        this.channel = ManagedChannelBuilder
                .forAddress("localhost", 8585)
                .usePlaintext()
                .build();

        this.bankBlockingStub = BankServiceGrpc.newBlockingStub(channel);
        this.bankStub = BankServiceGrpc.newStub(channel);
    }

    @AfterAll
    public void stop() {
        this.channel.shutdownNow();
    }

    @Test
    public void loadBalancingDemo() {

        for (int accountNumber = 1; accountNumber <= 10; ++accountNumber) {
            var request = BalanceCheckRequest.newBuilder()
                    .setAccountNumber(accountNumber)
                    .build();

            var response = this.bankBlockingStub.getAccountBalance(request);
            log.info("{}", response);
        }

    }

    @Test
    public void asyncLoadBalancingDemo() {


        var responseObserver = ResponseObserver.<AccountBalance>create();
        var requestObserver = this.bankStub.deposit(responseObserver);

        // initial message - account number
        requestObserver.onNext(DepositRequest.newBuilder()
                .setAccountNumber(5)
                .build());

        // sending stream of deposits
        IntStream.rangeClosed(1, 30)
                .mapToObj(_ -> Money.newBuilder().setAmount(10).build())
                .map(m -> DepositRequest.newBuilder().setMoney(m).build())
                .forEach(d -> {
                    Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
                    requestObserver.onNext(d);
                });

        // sending complete
        requestObserver.onCompleted();

        // get the response
        responseObserver.await();

    }

}
