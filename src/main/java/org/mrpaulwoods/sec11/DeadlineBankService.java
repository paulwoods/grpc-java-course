package org.mrpaulwoods.sec11;

import com.google.common.util.concurrent.Uninterruptibles;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.mrpaulwoods.models.sec11.*;
import org.mrpaulwoods.sec11.repository.AccountRepository;

import java.util.concurrent.TimeUnit;

public class DeadlineBankService extends BankServiceGrpc.BankServiceImplBase {

    public static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DeadlineBankService.class);

    @Override
    public void getAccountBalance(BalanceCheckRequest request, StreamObserver<AccountBalance> responseObserver) {
        int accountNumber = request.getAccountNumber();
        int balance = AccountRepository.getBalance(accountNumber);
        AccountBalance ab = AccountBalance.newBuilder()
                .setAccountNumber(accountNumber)
                .setBalance(balance)
                .build();
        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
        responseObserver.onNext(ab);
        responseObserver.onCompleted();
    }

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<Money> responseObserver) {
        var accountNumber = request.getAccountNumber();
        var requestedAmount = request.getAmount();
        var accountBalance = AccountRepository.getBalance(accountNumber);

        if (requestedAmount > accountBalance) {
            responseObserver.onError(Status.FAILED_PRECONDITION.asRuntimeException());
            return;
        }


        for (int i = 0; i < (requestedAmount / 10) && !Context.current().isCancelled(); i++) {
            var money = Money.newBuilder().setAmount(10).build();
            responseObserver.onNext(money);
            log.info("money {} sent {}", i, money);
            AccountRepository.deductAmount(accountNumber, 10);
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
        }

        log.info("streaming completed");
        responseObserver.onCompleted();
    }

}
