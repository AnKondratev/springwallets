package an.kondratev.springwallets.Impl;

import an.kondratev.springwallets.model.Wallet;
import an.kondratev.springwallets.model.WalletOperation;
import an.kondratev.springwallets.repository.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicLong;

@Service
@AllArgsConstructor
public class WalletOperationService {

    private final WalletRepository repository;

    @Transactional
    public void operation(WalletOperation operation) {
        Wallet wallet = repository.findByWalletId(operation.getWalletId());
        if (wallet == null) {
            throw new IllegalArgumentException("Кошелек не найден");
        }
        executeOperation(wallet, operation.getOperationType(), operation.getAmount());
        repository.save(wallet);
    }

    private void modifyBalance(Wallet wallet, Long amount) {
        Long newBalance = wallet.getBalance() + amount;
        wallet.setBalance(newBalance);
    }

    private void executeOperation(Wallet wallet, WalletOperation.OperationType operationType, long amount) {
        switch (operationType) {
            case DEPOSIT:
                modifyBalance(wallet, amount);
                break;
            case WITHDRAW:
                long currentBalance = wallet.getBalance();
                while (true) {
                    if (currentBalance < amount) {
                        throw new IllegalArgumentException("Недостаточно средств для снятия");
                    }
                    AtomicLong atomicLong = new AtomicLong(wallet.getBalance());
                    if (atomicLong.compareAndSet(currentBalance, currentBalance - amount)) {
                        modifyBalance(wallet, -amount);
                        break;
                    }
                    currentBalance = wallet.getBalance();
                }
                break;
        }
    }
}