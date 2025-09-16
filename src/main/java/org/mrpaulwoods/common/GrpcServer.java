package org.mrpaulwoods.common;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.mrpaulwoods.sec06.BankService;

public class GrpcServer {

    public static void main(String[] args) throws Exception {

        Server server = ServerBuilder.forPort(6565)
                .addService(new BankService())
                .build();

        server.start();

        server.awaitTermination();
    }

}
