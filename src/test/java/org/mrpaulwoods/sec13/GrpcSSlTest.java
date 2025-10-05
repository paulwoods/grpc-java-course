package org.mrpaulwoods.sec13;

import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.junit.jupiter.api.Test;
import org.mrpaulwoods.models.sec13.BalanceCheckRequest;
import org.mrpaulwoods.models.sec13.BankServiceGrpc;
import org.slf4j.Logger;

public class GrpcSSlTest extends AbstractTest {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(GrpcSSlTest.class);

    @Test
    public void clientWithSSLTest() {

        var channel = NettyChannelBuilder   // use NettyChannelBuilder instead of ManagedChannelBuilder
                .forAddress("localhost", 6565)
                // .usePlaintext() - can't use plaintext with SSL
                .sslContext(clientSslContext())
                .build();

        var stub = BankServiceGrpc.newBlockingStub(channel);

        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();

        var response = stub.getAccountBalance(request);

        log.info("{}", response);

        channel.shutdown();
    }
}
