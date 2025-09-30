package org.mrpaulwoods.sec12;

import io.grpc.ClientInterceptor;
import org.junit.jupiter.api.Test;
import org.mrpaulwoods.models.sec12.BalanceCheckRequest;
import org.mrpaulwoods.sec12.interceptors.GZipInterceptor;

import java.util.List;

public class Lec04GZipInterceptorTest extends AbstractInterceptorTest {

    @Override
    protected List<ClientInterceptor> getClientInterceptors() {
        return List.of(new GZipInterceptor());
    }

    @Test
    public void blockingDeadlineTest() {

        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();

        bankBlockingStub
                .getAccountBalance(request);

        /*

        // debug logs will have "grpc-encoding: gzip"

        21:05:21.317 DEBUG [-worker-ELG-3-2] g.n.s.i.g.n.NettyClientHandler : [id: 0x52558d15, L:/127.0.0.1:44500 -
        R:localhost/127.0.0.1:6565] OUTBOUND HEADERS: streamId=3 headers=GrpcHttp2OutboundHeaders[
        :authority: localhost:6565, :path: /sec12.BankService/GetAccountBalance, :method: POST, :scheme: http,
        content-type: application/grpc, te: trailers, user-agent: grpc-java-netty/1.75.0,
        grpc-encoding: gzip,
        grpc-accept-encoding: gzip]
        padding=0 endStream=false

        // example without gzip interceptor
        21:08:26.206 DEBUG [-worker-ELG-3-2] g.n.s.i.g.n.NettyClientHandler : [id: 0xc1948f14, L:/127.0.0.1:47640 -
        R:localhost/127.0.0.1:6565] OUTBOUND HEADERS: streamId=3 headers=GrpcHttp2OutboundHeaders[
        :authority: localhost:6565, :path: /sec12.BankService/GetAccountBalance, :method: POST, :scheme:
         http, content-type: application/grpc, te: trailers, user-agent: grpc-java-netty/1.75.0,
         grpc-accept-encoding: gzip
         ] padding=0 endStream=false

        */
    }

}
