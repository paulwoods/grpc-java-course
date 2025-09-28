package org.mrpaulwoods.sec11;

import com.google.common.util.concurrent.Uninterruptibles;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mrpaulwoods.common.AbstractChannelTest;
import org.mrpaulwoods.common.GrpcServer;
import org.mrpaulwoods.models.sec11.BalanceCheckRequest;
import org.mrpaulwoods.models.sec11.BankServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Lec06KeepAliveDemoTest extends AbstractChannelTest {

    private static final Logger log = LoggerFactory.getLogger(Lec06KeepAliveDemoTest.class);
    private final GrpcServer grpcServer = GrpcServer.create(new DeadlineBankService());
    private BankServiceGrpc.BankServiceBlockingStub bankBlockingStub;

    @BeforeAll
    public void setup() {
        this.grpcServer.start();
        this.bankBlockingStub = BankServiceGrpc.newBlockingStub(channel);
    }

    @AfterAll
    public void stop() {
        this.grpcServer.stop();
    }

    /*
    GrpcServer.java:
            var builder = ServerBuilder.forPort(port);

            to

            var builder = ServerBuilder.forPort(port)
                .keepAliveTime(10, TimeUnit.SECONDS)
                .keepAliveTimeout(1, TimeUnit.SECONDS)
                .maxConnectionIdle(25, TimeUnit.SECONDS);

    logback.xml

    <root level="INFO">
        to
    <root level="DEBUG">


        watch the logs. you will see the PING and GO_AWAY messages
     */

    @Test
    public void lazyChannelDemo() {

        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();

        var response = this.bankBlockingStub
                .getAccountBalance(request);

        log.info("{}", response);

        Uninterruptibles.sleepUninterruptibly(30, TimeUnit.SECONDS);
    }

}
