package org.mrpaulwoods.sec12;

import io.grpc.ClientInterceptor;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import org.junit.jupiter.api.Test;
import org.mrpaulwoods.common.GrpcServer;
import org.mrpaulwoods.models.sec12.BalanceCheckRequest;
import org.mrpaulwoods.sec12.interceptors.ApiKeyValidationInterceptor;
import org.slf4j.Logger;

import java.util.List;

public class Lec05ClientApiKeyInterceptorTest extends AbstractInterceptorTest {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec05ClientApiKeyInterceptorTest.class);

    @Override
    protected List<ClientInterceptor> getClientInterceptors() {
        return List.of(
                MetadataUtils.newAttachHeadersInterceptor(getApikey())
        );
    }

    @Override
    protected GrpcServer createServer() {
        return GrpcServer.create(6565, builder ->
                builder.addService(new BankService())
                        .intercept(new ApiKeyValidationInterceptor())
        );
    }

    private Metadata getApikey() {
        var metadata = new Metadata();
        metadata.put(Constants.API_KEY, "bank-client-secret");
        return metadata;
    }

    @Test
    public void clientApiKeyDemo() {

        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();

        var response = bankBlockingStub.getAccountBalance(request);
        log.info("{}", response);
    }

}
