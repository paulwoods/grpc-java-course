package org.mrpaulwoods.sec12.interceptors;

import io.grpc.*;
import org.mrpaulwoods.sec12.Constants;
import org.slf4j.Logger;

import java.util.Objects;

public class ApiKeyValidationInterceptor implements ServerInterceptor {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(ApiKeyValidationInterceptor.class);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {

        // if you want to do method based interceptors, you can use the fullMethodName
        String fullMethodName = serverCall.getMethodDescriptor().getFullMethodName();
        log.info("### {}", fullMethodName);
        // fullMethodName = sec12.BankService/GetAccountBalance

        var apiKey = metadata.get(Constants.API_KEY);
        if (isValid(apiKey)) {
            return serverCallHandler.startCall(serverCall, metadata);
        }
        serverCall.close(
                Status.UNAUTHENTICATED.withDescription("client must provide valid api key"),
                metadata
        );

        return new ServerCall.Listener<>() {
        };
    }

    private boolean isValid(String apiKey) {
        return Objects.nonNull(apiKey) && apiKey.equals("bank-client-secret");
    }

}
