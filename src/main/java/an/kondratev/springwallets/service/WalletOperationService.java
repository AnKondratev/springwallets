package an.kondratev.springwallets.service;

import an.kondratev.springwallets.model.Wallet;
import an.kondratev.springwallets.model.WalletOperation;
import an.kondratev.springwallets.repository.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class WalletOperationService {

    private final WalletRepository repository;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public void operation(WalletOperation operation) {
        Wallet wallet = repository.findByWalletId(operation.getWalletId());
        if (wallet == null) {
            throw new IllegalArgumentException("Кошелек не найден");
        }
        executeOperation(wallet, operation.getOperationType(), operation.getAmount());
        repository.save(wallet);
    }

    private void modifyBalance(Wallet wallet, Long amount) {
        wallet.setBalance(wallet.getBalance() + amount);
    }

    private void executeOperation(Wallet wallet, WalletOperation.OperationType operationType, long amount) {
        switch (operationType) {
            case DEPOSIT:
                modifyBalance(wallet, amount);
                break;
            case WITHDRAW:
                long currentBalance = wallet.getBalance();
                if (currentBalance < amount) {
                    throw new IllegalArgumentException("Недостаточно средств для снятия");
                }
                modifyBalance(wallet, -amount);
                break;
        }
    }
}
