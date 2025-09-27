package org.mrpaulwoods.sec07;

import io.grpc.stub.StreamObserver;
import org.mrpaulwoods.models.sec07.FlowControlServiceGrpc;
import org.mrpaulwoods.models.sec07.Output;
import org.mrpaulwoods.models.sec07.RequestSize;
import org.slf4j.Logger;

import java.util.stream.IntStream;

public class FlowControlService extends FlowControlServiceGrpc.FlowControlServiceImplBase {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(FlowControlService.class);

    @Override
    public StreamObserver<RequestSize> getMessages(StreamObserver<Output> responseObserver) {
        return new RequestHandler(responseObserver);

    }

    private static class RequestHandler implements StreamObserver<RequestSize> {

        private final StreamObserver<Output> responseObserver;
        private Integer emitted; // the number of messages emitted so far.

        public RequestHandler(StreamObserver<Output> responseObserver) {
            this.responseObserver = responseObserver;
            this.emitted = 0;
        }

        @Override
        public void onNext(RequestSize requestSize) {

            IntStream.rangeClosed(emitted + 1, 100)
                    .limit(requestSize.getSize())
                    .forEach(i -> {
                        log.info("emitting {}", i);
                        responseObserver.onNext(Output.newBuilder()
                                .setValue(i)
                                .build());
                    });

            emitted += requestSize.getSize();

            if (emitted >= 100) {
                log.info("completed");
                responseObserver.onCompleted();
            }

        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onCompleted() {
            this.responseObserver.onCompleted();
        }

    }

}
