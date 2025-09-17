package org.mrpaulwoods.sec06;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.mrpaulwoods.models.sec06.AccountBalance;
import org.mrpaulwoods.models.sec06.AllAccountsResponse;
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

    @Override
    public void getAllAccounts(Empty request, StreamObserver<AllAccountsResponse> responseObserver) {
        var accounts = AccountRepository.getAllAccounts()
                .entrySet()
                .stream()
                .map(e -> AccountBalance.newBuilder()
                        .setAccountNumber(e.getKey())
                        .setBalance(e.getValue())
                        .build())
                .toList();

        var response = AllAccountsResponse.newBuilder()
                .addAllAccounts(accounts)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
