package org.mrpaulwoods.sec12;

import io.grpc.CallCredentials;
import io.grpc.ClientInterceptor;
import io.grpc.Metadata;
import org.junit.jupiter.api.RepeatedTest;
import org.mrpaulwoods.common.GrpcServer;
import org.mrpaulwoods.models.sec12.BalanceCheckRequest;
import org.mrpaulwoods.sec12.interceptors.UserRoleInterceptor;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.Executor;

public class Lec07UserRoleContextTest extends AbstractInterceptorTest {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec07UserRoleContextTest.class);

    @Override
    protected List<ClientInterceptor> getClientInterceptors() {
        return List.of();
    }

    @Override
    protected GrpcServer createServer() {
        return GrpcServer.create(6565, builder ->
                builder.addService(new UserRoleBankService())
                        .intercept(new UserRoleInterceptor())
        );
    }

    @RepeatedTest(5)
    public void unaryUserCredentialsDemo() {

        for (int i = 1; i <= 4; i++) {

            var request = BalanceCheckRequest.newBuilder()
                    .setAccountNumber(i)
                    .build();

            var response = bankBlockingStub
                    .withCallCredentials(new UserSessionToken("user-token-" + i))
                    .getAccountBalance(request);

            log.info("{}", response);
        }

    }

    private static class UserSessionToken extends CallCredentials {
        private static final String TOKEN_FORMAT = "%s %s";
        private final String jwt;

        public UserSessionToken(String jwt) {
            this.jwt = jwt;
        }

        @Override
        public void applyRequestMetadata(RequestInfo requestInfo, Executor executor, MetadataApplier metadataApplier) {
            executor.execute(() -> {
                var metadata = new Metadata();
                metadata.put(Constants.USER_TOKEN_KEY, TOKEN_FORMAT.formatted(Constants.BEARER, jwt));
                metadataApplier.apply(metadata);
            });
        }

    }

}
