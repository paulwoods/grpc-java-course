package org.mrpaulwoods.sec12.interceptors;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

public class GzipResponseInterceptor implements ServerInterceptor {

    /*
    in the logs you'll see the header "grpc-encoding: gzip"

    21:16:14.357 DEBUG [-worker-ELG-3-3] g.n.s.i.g.n.NettyServerHandler : [id: 0x2f27742b, L:/127.0.0.1:6565 -
    R:/127.0.0.1:34724] OUTBOUND HEADERS: streamId=3 headers=GrpcHttp2OutboundHeaders[
    :status: 200, content-type: application/grpc,
    grpc-encoding: gzip,
    grpc-accept-encoding: gzip
    ] padding=0 endStream=false
     */
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> serverCall,
            Metadata metadata,
            ServerCallHandler<ReqT, RespT> serverCallHandler
    ) {
        serverCall.setCompression("gzip");
        return serverCallHandler.startCall(serverCall, metadata);
    }

}
