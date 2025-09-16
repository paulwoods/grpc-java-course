package org.mrpaulwoods.sec06;

import io.grpc.stub.StreamObserver;
import org.mrpaulwoods.models.sec06.AccountBalance;
import org.mrpaulwoods.models.sec06.BalanceCheckRequest;
import org.mrpaulwoods.models.sec06.BankServiceGrpc;
import org.mrpaulwoods.sec06.repository.AccountRepository;

public class BankService extends BankServiceGrpc.BankServiceImplBase {

    @Override
    public void getAccountBalance(BalanceCheckRequest request, StreamObserver<AccountBalance> responseObserver) {

        int accountNumber = request.getAccountNumber();
        int balance = AccountRepository.getBalance(accountNumber);

        AccountBalance ab = AccountBalance.newBuilder()
                .setAccountNumber(accountNumber)
                .setBalance(balance)
                .build();

        responseObserver.onNext(ab);
        responseObserver.onCompleted();
    }

}
