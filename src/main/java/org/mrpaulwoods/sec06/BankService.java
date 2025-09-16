package org.mrpaulwoods.sec06;

import io.grpc.stub.StreamObserver;
import org.mrpaulwoods.models.sec06.AccountBalance;
import org.mrpaulwoods.models.sec06.BalanceCheckRequest;
import org.mrpaulwoods.models.sec06.BankServiceGrpc;

public class BankService extends BankServiceGrpc.BankServiceImplBase {

    @Override
    public void getAccountBalance(BalanceCheckRequest request, StreamObserver<AccountBalance> responseObserver) {

        int accountNumber = request.getAccountNumber();

        AccountBalance ab = AccountBalance.newBuilder()
                .setAccountNumber(accountNumber)
                .setBalance(accountNumber * 10)
                .build();

        responseObserver.onNext(ab);
        responseObserver.onCompleted();
    }

}
