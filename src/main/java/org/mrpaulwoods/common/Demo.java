package org.mrpaulwoods.common;

import org.mrpaulwoods.sec12.interceptors.ApiKeyValidationInterceptor;

/*
    a simple class to start the server with specific services for demo purposes.
 */
public class Demo {

    static void main(String[] ignoredArgs) {

//        GrpcServer.create(new DeadlineBankService(), new TransferService())
//        GrpcServer.create(new FlowControlService())
//        GrpcServer.create(new BankService())
//                .start()
//                .await();

        GrpcServer.create(6565, builder ->
                        builder.addService(new org.mrpaulwoods.sec12.BankService())
                                .intercept(new ApiKeyValidationInterceptor())
                )
                .start()
                .await();
    }

}
