package an.kondratev.springwallets.service;

import an.kondratev.springwallets.model.Wallet;
import an.kondratev.springwallets.model.WalletOperation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
public interface WalletService {
    void saveWallet(Wallet wallet);

    Wallet findByUUID(UUID uuid);

    void operation(WalletOperation operation);
}