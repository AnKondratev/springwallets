package an.kondratev.springwallets.service;

import an.kondratev.springwallets.model.Wallet;
import an.kondratev.springwallets.repository.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@AllArgsConstructor
public class WalletPersistenceService {

    private final WalletRepository repository;

    public void saveWallet(@Validated Wallet wallet) {
        if (wallet.getWalletId() == null) {
            wallet.setWalletId(UUID.randomUUID());
        }
        repository.save(wallet);
    }

    public Wallet findByUUID(UUID walletId) {
        return repository.findByWalletId(walletId);
    }
}

