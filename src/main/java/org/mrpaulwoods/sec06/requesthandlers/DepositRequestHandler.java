package org.mrpaulwoods.sec06.requesthandlers;

import io.grpc.stub.StreamObserver;
import org.mrpaulwoods.models.sec06.AccountBalance;
import org.mrpaulwoods.models.sec06.DepositRequest;
import org.mrpaulwoods.sec06.repository.AccountRepository;
import org.slf4j.Logger;

public class DepositRequestHandler implements StreamObserver<DepositRequest> {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DepositRequestHandler.class);
    private final StreamObserver<AccountBalance> responseObserver;
    private int accountNumber;

    public DepositRequestHandler(StreamObserver<AccountBalance> responseObserver) {
        this.responseObserver = responseObserver;
    }

    @Override
    public void onNext(DepositRequest depositRequest) {
        log.info("client received: {}", depositRequest);
        switch (depositRequest.getRequestCase()) {
            case ACCOUNT_NUMBER -> this.accountNumber = depositRequest.getAccountNumber();
            case MONEY -> AccountRepository.addAmount(this.accountNumber, depositRequest.getMoney().getAmount());
        }
    }

    @Override
    public void onError(Throwable throwable) {
        log.info("client error: {}", throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        log.info("client completed account number {}", accountNumber);
        var accountBalance = AccountBalance.newBuilder()
                .setAccountNumber(accountNumber)
                .setBalance(AccountRepository.getBalance(accountNumber))
                .build();

        responseObserver.onNext(accountBalance);
        responseObserver.onCompleted();
    }

}
