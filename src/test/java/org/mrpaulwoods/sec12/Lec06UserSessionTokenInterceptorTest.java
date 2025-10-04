package org.mrpaulwoods.sec12;

import io.grpc.CallCredentials;
import io.grpc.ClientInterceptor;
import io.grpc.Metadata;
import org.junit.jupiter.api.Test;
import org.mrpaulwoods.common.GrpcServer;
import org.mrpaulwoods.common.ResponseObserver;
import org.mrpaulwoods.models.sec12.BalanceCheckRequest;
import org.mrpaulwoods.models.sec12.Money;
import org.mrpaulwoods.models.sec12.WithdrawRequest;
import org.mrpaulwoods.sec12.interceptors.UserTokenInterceptor;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.Executor;

public class Lec06UserSessionTokenInterceptorTest extends AbstractInterceptorTest {
    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec06UserSessionTokenInterceptorTest.class);

    @Override
    protected List<ClientInterceptor> getClientInterceptors() {
        return List.of();
    }

    @Override
    protected GrpcServer createServer() {
        return GrpcServer.create(6565, builder ->
                builder.addService(new BankService())
                        .intercept(new UserTokenInterceptor())
        );
    }

    @Test
    public void unaryUserCredentialsDemo() {

        for (int i = 1; i <= 5; i++) {

            var request = BalanceCheckRequest.newBuilder()
                    .setAccountNumber(i)
                    .build();

            var response = bankBlockingStub
                    .withCallCredentials(new UserSessionToken("user-token-" + i))
                    .getAccountBalance(request);

            log.info("{}", response);
        }

    }

    @Test
    public void streamingUserCredentialsDemo() {

        for (int i = 1; i <= 5; i++) {

            var observer = ResponseObserver.<Money>create();

            var request = WithdrawRequest.newBuilder()
                    .setAccountNumber(i)
                    .setAmount(30)
                    .build();

            bankStub
                    .withCallCredentials(new UserSessionToken("user-token-" + i))
                    .withdraw(request, observer);

            observer.await();

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
