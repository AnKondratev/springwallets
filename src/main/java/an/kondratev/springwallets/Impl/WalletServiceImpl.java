package an.kondratev.springwallets.Impl;

import an.kondratev.springwallets.model.Wallet;
import an.kondratev.springwallets.model.WalletOperation;
import an.kondratev.springwallets.repository.WalletOperationRepository;
import an.kondratev.springwallets.repository.WalletRepository;
import an.kondratev.springwallets.service.WalletService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository repository;
    private final WalletOperationRepository walletOperationRepository;

    @Override
   // @Cacheable(value = "wallets")
    public List<Wallet> getWallets() {
        return repository.findAll();
    }

    @Override
    @CachePut(value = "wallets", key = "#wallet.id")
    public void saveWallet(Wallet wallet) {
        repository.save(wallet);
    }

    @Override
    @Cacheable(value = "wallets", key = "#walletId")
    public Wallet findByUUID(UUID walletId) {
        return repository.findByWalletId(walletId);
    }

    @Override
    @CacheEvict(value = "wallets", key = "#operation.walletId")
    public void operation(WalletOperation operation) {
        UUID walletId = operation.getWalletId();
        long amount = operation.getAmount();

        Wallet wallet = repository.findByWalletId(walletId);
        if (wallet == null) {
            throw new IllegalArgumentException("Кошелек не найден");
        }
        operation.getOperationType().execute(wallet, amount);
        repository.save(wallet);
        walletOperationRepository.save(operation);
    }
}