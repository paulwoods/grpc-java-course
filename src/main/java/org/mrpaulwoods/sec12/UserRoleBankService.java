package org.mrpaulwoods.sec12;

import io.grpc.stub.StreamObserver;
import org.mrpaulwoods.models.sec12.AccountBalance;
import org.mrpaulwoods.models.sec12.BalanceCheckRequest;
import org.mrpaulwoods.models.sec12.BankServiceGrpc;
import org.mrpaulwoods.sec12.repository.AccountRepository;

public class UserRoleBankService extends BankServiceGrpc.BankServiceImplBase {

    public static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserRoleBankService.class);

    @Override
    public void getAccountBalance(BalanceCheckRequest request, StreamObserver<AccountBalance> responseObserver) {
        int accountNumber = request.getAccountNumber();
        int balance = AccountRepository.getBalance(accountNumber);

        if (UserRole.STANDARD.equals(Constants.USER_ROLE_KEY.get())) {
            var fee = balance > 0 ? 1 : 0;
            AccountRepository.deductAmount(accountNumber, fee);
            balance = balance - fee;
        }

        AccountBalance ab = AccountBalance.newBuilder()
                .setAccountNumber(accountNumber)
                .setBalance(balance)
                .build();

        responseObserver.onNext(ab);
        responseObserver.onCompleted();
    }

}
