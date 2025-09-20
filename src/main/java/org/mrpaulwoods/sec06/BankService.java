package org.mrpaulwoods.sec06;

import com.google.common.util.concurrent.Uninterruptibles;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.mrpaulwoods.models.sec06.*;
import org.mrpaulwoods.sec06.repository.AccountRepository;

import java.util.concurrent.TimeUnit;

public class BankService extends BankServiceGrpc.BankServiceImplBase {

    public static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BankService.class);

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

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<Money> responseObserver) {
        var accountNumber = request.getAccountNumber();
        var requestedAmount = request.getAmount();
        var accountBalance = AccountRepository.getBalance(accountNumber);

        if (requestedAmount > accountBalance) {
            responseObserver.onCompleted();
            return;
        }

        for (int i = 0; i < (requestedAmount / 10); i++) {
            var money = Money.newBuilder().setAmount(10).build();
            responseObserver.onNext(money);
            log.info("money {} sent {}", i, money);
            AccountRepository.deductAmount(accountNumber, 10);
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
        }
        responseObserver.onCompleted();
    }

}
