package org.mrpaulwoods.common;

import org.mrpaulwoods.sec10.BankService;

/*
    a simple class to start the server with specific services for demo purposes.
 */
public class Demo {

    static void main(String[] ignoredArgs) {

//        GrpcServer.create(new BankService(), new TransferService())
//        GrpcServer.create(new FlowControlService())
        GrpcServer.create(new BankService())
                .start()
                .await();
    }

}
