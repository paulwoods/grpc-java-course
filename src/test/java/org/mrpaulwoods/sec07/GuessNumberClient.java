package org.mrpaulwoods.sec07;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mrpaulwoods.common.AbstractChannelTest;
import org.mrpaulwoods.common.GrpcServer;
import org.mrpaulwoods.models.sec07.GuessNumberServiceGrpc;
import org.mrpaulwoods.models.sec07.GuessRequest;
import org.mrpaulwoods.models.sec07.GuessResponse;

import java.util.concurrent.CountDownLatch;

@SuppressWarnings("NewClassNamingConvention")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GuessNumberClient extends AbstractChannelTest {

    private final GrpcServer server = GrpcServer.create(new GuessNumberService());
    private GuessNumberServiceGrpc.GuessNumberServiceStub stub;

    @BeforeAll
    public void setup() {
        this.server.start();
        this.stub = GuessNumberServiceGrpc.newStub(channel);
    }

    @AfterAll
    public void stop() {
        this.server.stop();
    }

    @Test
    public void play() {
        var responseObserver = new GuessNumberResponseHandler();
        var requestObserver = this.stub.makeGuess(responseObserver);
        responseObserver.setRequestObserver(requestObserver);
        responseObserver.await();
    }

    public static class GuessNumberResponseHandler implements StreamObserver<GuessResponse> {

        public static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GuessNumberResponseHandler.class);

        private StreamObserver<GuessRequest> requestObserver;
        private final CountDownLatch latch = new CountDownLatch(1);
        private int guess = 50;
        private int lowerLimit = 1;
        private int upperLimit = 100;

        @Override
        public void onNext(GuessResponse guessResponse) {
            log.info("CLIENT - received guess response: {}", guessResponse);
            switch (guessResponse.getResult()) {
                case CORRECT -> correct(guessResponse);
                case TOO_LOW -> tooLow(guessResponse);
                case TOO_HIGH -> tooHigh(guessResponse);
            }
        }

        private void tooLow(GuessResponse guessResponse) {
            lowerLimit = guess + 1;
            guess = lowerLimit + (upperLimit - lowerLimit) / 2;
            log.info("CLIENT - guessing {}", guess);
            requestObserver.onNext(GuessRequest.newBuilder().setGuess(guess).build());
        }

        private void tooHigh(GuessResponse guessResponse) {
            upperLimit = guess - 1;
            guess = lowerLimit + (upperLimit - lowerLimit) / 2;
            log.info("CLIENT - guessing {}", guess);
            requestObserver.onNext(GuessRequest.newBuilder().setGuess(guess).build());
        }

        private void correct(GuessResponse guessResponse) {
            log.info("CLIENT - correct - guess was {}", guess);
            requestObserver.onCompleted();
            latch.countDown();
        }

        @Override
        public void onError(Throwable throwable) {
            log.info("CLIENT - onError: {}", throwable.getMessage());
            latch.countDown();
        }

        @Override
        public void onCompleted() {
            log.info("CLIENT - onCompleted");
        }

        public void await() {
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public void setRequestObserver(StreamObserver<GuessRequest> requestObserver) {
            this.requestObserver = requestObserver;

            // send the first guess
            this.requestObserver.onNext(GuessRequest.newBuilder().setGuess(guess).build());
        }
    }

}
