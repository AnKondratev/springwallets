package an.kondratev.springwallets.Impl;

import an.kondratev.springwallets.model.Wallet;
import an.kondratev.springwallets.model.WalletOperation;
import an.kondratev.springwallets.service.WalletService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletPersistenceService persistenceService;
    private final WalletOperationService operationService;
    private final WalletValidationService validationService;

    public void saveWallet(Wallet wallet) {
        validationService.validateWallet(wallet);
        persistenceService.saveWallet(wallet);
    }

    public Wallet findByUUID(UUID walletId) {
        return persistenceService.findByUUID(walletId);
    }

    public void performOperation(WalletOperation operation) {
        validationService.validateOperation(operation);
        operationService.operation(operation);
    }
}
