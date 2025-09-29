package org.mrpaulwoods.sec11;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mrpaulwoods.models.sec06.BalanceCheckRequest;
import org.mrpaulwoods.models.sec06.BankServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec07LoadBalancingDemoTest {

    private static final Logger log = LoggerFactory.getLogger(Lec07LoadBalancingDemoTest.class);
    private BankServiceGrpc.BankServiceBlockingStub bankBlockingStub;
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

}
