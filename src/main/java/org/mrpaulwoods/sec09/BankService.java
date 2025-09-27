package org.mrpaulwoods.sec09;

import com.google.common.util.concurrent.Uninterruptibles;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.mrpaulwoods.models.sec09.*;
import org.mrpaulwoods.sec09.repository.AccountRepository;
import org.mrpaulwoods.sec09.validator.RequestValidator;

import java.util.concurrent.TimeUnit;

public class BankService extends BankServiceGrpc.BankServiceImplBase {

    public static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BankService.class);

    private static void sendMoney(WithdrawRequest request, StreamObserver<Money> responseObserver) {
        try {
            var accountNumber = request.getAccountNumber();
            var requestedAmount = request.getAmount();
            for (int i = 0; i < (requestedAmount / 10); i++) {
                var money = Money.newBuilder().setAmount(10).build();
                if (i == 3) {
                    throw new RuntimeException("Boom!");
                }
                responseObserver.onNext(money);
                log.info("money {} sent {}", i, money);
                AccountRepository.deductAmount(accountNumber, 10);
                Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
            }
            responseObserver.onCompleted();
        } catch (Exception ex) {
            responseObserver.onError(Status.INTERNAL.withDescription(ex.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getAccountBalance(BalanceCheckRequest request, StreamObserver<AccountBalance> responseObserver) {
        RequestValidator.validateAccount(request.getAccountNumber())
                .map(Status::asRuntimeException)
                .ifPresentOrElse(
                        responseObserver::onError,
                        () -> sendAccountBalance(request, responseObserver)
                );
    }

    private void sendAccountBalance(BalanceCheckRequest request, StreamObserver<AccountBalance> responseObserver) {
        int accountNumber = request.getAccountNumber();
        int balance = AccountRepository.getBalance(accountNumber);
        AccountBalance accountBalance = AccountBalance.newBuilder()
                .setAccountNumber(accountNumber)
                .setBalance(balance)
                .build();
        responseObserver.onNext(accountBalance);
        responseObserver.onCompleted();
    }

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<Money> responseObserver) {
        RequestValidator.validateAccount(request.getAccountNumber())
                .or(() -> RequestValidator.isAmountDivisibleBy10(request.getAmount()))
                .or(() -> RequestValidator.hasSufficientBalance(request.getAmount(), AccountRepository.getBalance(request.getAccountNumber())))
                .map(Status::asRuntimeException)
                .ifPresentOrElse(
                        responseObserver::onError,
                        () -> sendMoney(request, responseObserver)
                );
    }
}
