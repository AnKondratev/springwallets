package an.kondratev.springwallets.Impl;

import an.kondratev.springwallets.model.Wallet;
import an.kondratev.springwallets.model.WalletOperation;
import an.kondratev.springwallets.repository.WalletOperationRepository;
import an.kondratev.springwallets.repository.WalletRepository;
import an.kondratev.springwallets.service.WalletService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@AllArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository repository;
    private final WalletOperationRepository walletOperationRepository;

    @Override
    public void saveWallet(@Validated Wallet wallet) {
        repository.save(wallet);
    }

    @Override
    public Wallet findByUUID(UUID walletId) {
        if (!isValidUUID(walletId.toString())) {
            throw new IllegalArgumentException("Некорректный формат UUID!");
        }

        return repository.findByWalletId(walletId);
    }

    @Override
    public void operation(@Validated WalletOperation operation) {
        Wallet wallet = repository.findByWalletId(operation.getWalletId());
        if (wallet == null) {
            throw new IllegalArgumentException("Кошелек не найден");
        }

        performOperation(wallet, operation.getOperationType(), operation.getAmount());
        repository.save(wallet);
        walletOperationRepository.save(operation);
    }

    private void performOperation(Wallet wallet, WalletOperation.OperationType operationType, long amount) {
        switch (operationType) {
            case DEPOSIT:
                wallet.setBalance(wallet.getBalance() + amount);
                break;
            case WITHDRAW:
                if (wallet.getBalance() < amount) {
                    throw new IllegalArgumentException("Недостаточно средств для снятия");
                }
                wallet.setBalance(wallet.getBalance() - amount);
                break;
        }
    }

    public boolean isValidUUID(String uuidStr) {
        try {
            UUID.fromString(uuidStr);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

