package org.mrpaulwoods.sec07;

import io.grpc.stub.StreamObserver;
import org.mrpaulwoods.models.sec07.GuessNumberServiceGrpc;
import org.mrpaulwoods.models.sec07.GuessRequest;
import org.mrpaulwoods.models.sec07.GuessResponse;
import org.mrpaulwoods.models.sec07.Result;
import org.slf4j.Logger;

import java.util.concurrent.ThreadLocalRandom;

public class GuessNumberService extends GuessNumberServiceGrpc.GuessNumberServiceImplBase {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(GuessNumberService.class);

    @Override
    public StreamObserver<GuessRequest> makeGuess(StreamObserver<GuessResponse> responseObserver) {
        return new GuessNumberRequestHandler(responseObserver);
    }

    public static class GuessNumberRequestHandler implements StreamObserver<GuessRequest> {

        private final StreamObserver<GuessResponse> responseObserver;
        private final int theNumber = ThreadLocalRandom.current().nextInt(1, 100);
        private int numGuesses = 0;

        public GuessNumberRequestHandler(StreamObserver<GuessResponse> responseObserver) {
            this.responseObserver = responseObserver;
            log.info("SERVER - The number is {}", theNumber);
        }

        @Override
        public void onNext(GuessRequest guessRequest) {
            log.info("SERVER - received guess {}", guessRequest.getGuess());

            Result result = calculateResult(guessRequest.getGuess());
            log.info("SERVER - the result is {}", result);

            GuessResponse response = GuessResponse.newBuilder()
                    .setResult(result)
                    .setAttempt(++numGuesses)
                    .build();

            responseObserver.onNext(response);

            if (result == Result.CORRECT) {
                log.info("SERVER - completing the stream");
                responseObserver.onCompleted();
            }

        }

        @Override
        public void onError(Throwable throwable) {
            log.info("SERVER - onError: {}", throwable.getMessage());
        }

        @Override
        public void onCompleted() {
            log.info("SERVER - onCompleted");

        }

        Result calculateResult(int guess) {
            if (theNumber == guess) {
                return Result.CORRECT;
            } else if (theNumber > guess) {
                return Result.TOO_LOW;
            } else {
                return Result.TOO_HIGH;
            }
        }

    }

}
