package an.kondratev.springwallets.repository;

import an.kondratev.springwallets.model.WalletOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletOperationRepository extends JpaRepository<WalletOperation, Long> {
}
