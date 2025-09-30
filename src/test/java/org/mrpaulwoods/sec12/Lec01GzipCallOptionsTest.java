package org.mrpaulwoods.sec12;

import org.junit.jupiter.api.Test;
import org.mrpaulwoods.models.sec12.BalanceCheckRequest;
import org.slf4j.Logger;

public class Lec01GzipCallOptionsTest extends AbstractTest {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec01GzipCallOptionsTest.class);

    /*
    logback.zml
        <root level="DEBUG">

    look at logs. search for "OUTBOUND HEADERS"
    if you see "grpc-accept-encoding: gzip" this means that it can accept gzip
    or, if you see "grpc-encoding: identity", then the data is not compressed.

        19:57:23.280 DEBUG [-worker-ELG-3-2] g.n.s.i.g.n.NettyClientHandler : [id: 0x53f3d3bd, L:/127.0.0.1:36270 -
        R:localhost/127.0.0.1:6565] OUTBOUND HEADERS: streamId=3 headers=GrpcHttp2OutboundHeaders[:authority:
        localhost:6565, :path: /sec12.BankService/GetAccountBalance, :method: POST, :scheme: http,
        content-type: application/grpc, te: trailers, user-agent: grpc-java-netty/1.75.0,
            grpc-accept-encoding: gzip
        ] padding=0 endStream=false

        19:57:26.311 DEBUG [-worker-ELG-3-3] g.n.s.i.g.n.NettyServerHandler : [id: 0xba21e8c6, L:/127.0.0.1:6565 -
        R:/127.0.0.1:36270] OUTBOUND HEADERS: streamId=3 headers=GrpcHttp2OutboundHeaders[:status: 200,
        content-type: application/grpc,
            grpc-encoding: identity,
        grpc-accept-encoding: gzip] padding=0 endStream=false


    if you see "grpc-encoding: gzip" means that it is sending gzip

        20:04:20.461 DEBUG [-worker-ELG-3-2] g.n.s.i.g.n.NettyClientHandler : [id: 0x36b6f1a5, L:/127.0.0.1:50364 -
        R:localhost/127.0.0.1:6565] OUTBOUND HEADERS: streamId=3 headers=GrpcHttp2OutboundHeaders[:authority:
        localhost:6565, :path: /sec12.BankService/GetAccountBalance, :method: POST, :scheme: http,
        content-type: application/grpc, te: trailers, user-agent: grpc-java-netty/1.75.0,
            grpc-encoding: gzip,
            grpc-accept-encoding: gzip
        ] padding=0 endStream=false

        20:09:19.290 DEBUG [-worker-ELG-3-3] g.n.s.i.g.n.NettyServerHandler : [id: 0xcd9e3217, L:/127.0.0.1:6565 -
        R:/127.0.0.1:35264] OUTBOUND HEADERS: streamId=3 headers=GrpcHttp2OutboundHeaders[:status: 200,
        content-type: application/grpc,
        grpc-encoding: gzip,
        grpc-accept-encoding: gzip
        ] padding=0 endStream=false

     */

    @Test
    public void gzipDemo() {
        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();

        var response = this.bankBlockingStub
                .withCompression("gzip") // messages from client to server are compressed
                .getAccountBalance(request);
        log.info("{}", response);

    }
}
