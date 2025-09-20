package org.mrpaulwoods.common;

import org.mrpaulwoods.sec06.BankService;
import org.mrpaulwoods.sec06.TransferService;

/*
    a simple class to start the server with specific services for demo purposes.
 */
public class Demo {

    public static void main(String[] args) {

        GrpcServer.create(new BankService(), new TransferService())
                .start()
                .await();
    }

}
