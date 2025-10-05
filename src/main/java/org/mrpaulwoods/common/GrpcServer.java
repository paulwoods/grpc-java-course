package org.mrpaulwoods.common;

import io.grpc.*;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class GrpcServer {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(GrpcServer.class);

    private final Server server;

    private GrpcServer(Server server) {
        this.server = server;
    }

    public static GrpcServer create(int port, BindableService... services) {
        return create(port, builder ->
                Arrays.asList(services).forEach(builder::addService)
        );
    }

    public static GrpcServer create(int port, Consumer<NettyServerBuilder> consumer) {
        var builder = ServerBuilder.forPort(port);
        consumer.accept((NettyServerBuilder) builder);
        return new GrpcServer(builder.build());
    }

    public static GrpcServer create(BindableService... services) {
        return create(6565, services);
    }

    public GrpcServer start() {
        List<String> list = server.getServices()
                .stream()
                .map(ServerServiceDefinition::getServiceDescriptor)
                .map(ServiceDescriptor::getName)
                .toList();

        try {
            server.start();
            log.info("server started. listening on port {}. services: {}", server.getPort(), list);
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void await() {
        try {
            server.awaitTermination();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        server.shutdownNow();
        log.info("server stopped");
    }


}
