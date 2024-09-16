package an.kondratev.springwallets.repository;

import an.kondratev.springwallets.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Wallet findByWalletId(UUID walletId);

}
