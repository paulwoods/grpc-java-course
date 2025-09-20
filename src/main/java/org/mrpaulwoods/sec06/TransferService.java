package org.mrpaulwoods.sec06;

import io.grpc.stub.StreamObserver;
import org.mrpaulwoods.models.sec06.TransferRequest;
import org.mrpaulwoods.models.sec06.TransferResponse;
import org.mrpaulwoods.models.sec06.TransferServiceGrpc;
import org.mrpaulwoods.sec06.requesthandlers.TransferRequestHandler;

public class TransferService extends TransferServiceGrpc.TransferServiceImplBase {

    @Override
    public StreamObserver<TransferRequest> transfer(StreamObserver<TransferResponse> responseObserver) {
        return new TransferRequestHandler(responseObserver);
    }

}
