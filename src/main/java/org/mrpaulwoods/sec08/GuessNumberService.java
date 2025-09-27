package org.mrpaulwoods.sec08;

import io.grpc.stub.StreamObserver;
import org.mrpaulwoods.models.sec08.GuessNumberGrpc;
import org.mrpaulwoods.models.sec08.GuessRequest;
import org.mrpaulwoods.models.sec08.GuessResponse;
import org.mrpaulwoods.models.sec08.Result;
import org.slf4j.Logger;

import java.util.concurrent.ThreadLocalRandom;

public class GuessNumberService extends GuessNumberGrpc.GuessNumberImplBase {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(GuessNumberService.class);

    @Override
    public StreamObserver<GuessRequest> makeGuess(StreamObserver<GuessResponse> responseObserver) {
        return new GuessRequestHandler(responseObserver);
    }

    private static class GuessRequestHandler implements StreamObserver<GuessRequest> {
        private final StreamObserver<GuessResponse> responseObserver;
        private final int secret;
        private int attempt;

        public GuessRequestHandler(StreamObserver<GuessResponse> responseObserver) {
            this.responseObserver = responseObserver;
            this.secret = ThreadLocalRandom.current().nextInt(1, 101);
            this.attempt = 0;
        }

        @Override
        public void onNext(GuessRequest guessRequest) {
            if (guessRequest.getGuess() > secret) {
                this.send(Result.TOO_HIGH);
            } else if (guessRequest.getGuess() < secret) {
                this.send(Result.TOO_LOW);
            } else {
                log.info("client guess {} is correct", guessRequest.getGuess());
                this.send(Result.CORRECT);
                this.responseObserver.onCompleted();
            }
        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onCompleted() {
            this.responseObserver.onCompleted();
        }

        private void send(Result result) {
            attempt++;
            var response = GuessResponse.newBuilder()
                    .setAttempt(attempt)
                    .setResult(result)
                    .build();
            this.responseObserver.onNext(response);
        }

    }

}
