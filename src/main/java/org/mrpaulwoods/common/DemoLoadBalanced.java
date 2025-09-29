package org.mrpaulwoods.common;

import org.mrpaulwoods.sec06.BankService;

/*
    a simple class to start the server with specific services for demo purposes.
 */
public class DemoLoadBalanced {

    public static class BankInstance1 {
        static void main(String[] ignoredArgs) {
            GrpcServer.create(6565, new BankService())
                    .start()
                    .await();
        }
    }

    public static class BankInstance2 {
        static void main(String[] ignoredArgs) {
            GrpcServer.create(7575, new BankService())
                    .start()
                    .await();
        }
    }

}
