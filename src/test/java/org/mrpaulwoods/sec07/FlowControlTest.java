package org.mrpaulwoods.sec07;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mrpaulwoods.common.AbstractChannelTest;
import org.mrpaulwoods.common.GrpcServer;
import org.mrpaulwoods.models.sec07.FlowControlServiceGrpc;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FlowControlTest extends AbstractChannelTest {
    private final GrpcServer server = GrpcServer.create(new FlowControlService());
    private FlowControlServiceGrpc.FlowControlServiceStub stub;

    @BeforeAll
    public void setup() {
        this.server.start();
        this.stub = FlowControlServiceGrpc.newStub(channel);
    }

    @AfterAll
    public void stop() {
        this.server.stop();
    }

    @Test
    public void flowControlDemo() {
        var responseObserver = new ResponseHandler();
        var requestObserver = this.stub.getMessages(responseObserver);
        responseObserver.setRequestObserver(requestObserver);
        responseObserver.start();
        responseObserver.await();

    }


}
