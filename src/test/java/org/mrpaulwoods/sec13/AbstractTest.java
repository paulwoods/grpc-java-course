package org.mrpaulwoods.sec13;

import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.mrpaulwoods.common.GrpcServer;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.nio.file.Path;
import java.security.KeyStore;
import java.util.concurrent.Callable;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractTest {

    private static final Path KEY_STORE = Path.of("src/test/resources/certs/grpc.keystore.jks");
    private static final Path TRUST_STORE = Path.of("src/test/resources/certs/grpc.truststore.jks");
    private static final char[] PASSWORD = "changeit".toCharArray();

    private final GrpcServer grpcServer = GrpcServer.create(6565, b ->
        b.addService(new BankService())
                .sslContext(serverSslContext())
    );

    @BeforeAll
    public void setup() {
        this.grpcServer.start();
    }

    @AfterAll
    public void teardown() {
        this.grpcServer.stop();
    }

    protected SslContext serverSslContext() {
        return handleException(() ->
                GrpcSslContexts.configure(SslContextBuilder.forServer(getKeyManagerFactory())).build()
        );
    }

    protected SslContext clientSslContext() {
        return handleException(() ->
                GrpcSslContexts.configure(SslContextBuilder.forClient().trustManager(getTrustManagerFactory())).build()
        );
    }

    protected KeyManagerFactory getKeyManagerFactory() {
        return handleException(() -> {
            var kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            var keyStore = KeyStore.getInstance(KEY_STORE.toFile(), PASSWORD);
            kmf.init(keyStore, PASSWORD);
            return kmf;
        });
    }

    protected TrustManagerFactory getTrustManagerFactory() {
        return handleException(() -> {
            var tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            var trustStore = KeyStore.getInstance(TRUST_STORE.toFile(), PASSWORD);
            tmf.init(trustStore);
            return tmf;
        });
    }

    private <T> T handleException(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
