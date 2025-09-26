package org.mrpaulwoods.common;

import org.mrpaulwoods.sec09.BankService;

/*
    a simple class to start the server with specific services for demo purposes.
 */
public class Demo {

    public static void main(String[] args) {

//        GrpcServer.create(new BankService(), new TransferService())
//        GrpcServer.create(new FlowControlService())
        GrpcServer.create(new BankService())
                .start()
                .await();
    }

}
