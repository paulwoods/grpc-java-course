package org.mrpaulwoods.sec08;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mrpaulwoods.common.AbstractChannelTest;
import org.mrpaulwoods.common.GrpcServer;
import org.mrpaulwoods.models.sec08.GuessNumberGrpc;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GuessANumberTest extends AbstractChannelTest  {

    private final GrpcServer server = GrpcServer.create(new GuessNumberService());
    private GuessNumberGrpc.GuessNumberStub stub;

    @BeforeAll
    public void setup() {
        this.server.start();
        this.stub = GuessNumberGrpc.newStub(channel);
    }

    @AfterAll
    public void stop() {
        this.server.stop();
    }

    @Test
    public void guessANumberGame() {
        var responseObserver = new GuessResponseHandler();
        var requestObserver = this.stub.makeGuess(responseObserver);
        responseObserver.setRequestObserver(requestObserver);
        responseObserver.start();
        responseObserver.await();
    }
}
