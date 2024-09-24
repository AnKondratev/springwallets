package an.kondratev.springwallets.service;

import an.kondratev.springwallets.model.Wallet;
import an.kondratev.springwallets.model.WalletOperation;
import org.springframework.stereotype.Service;

@Service
public class WalletValidationService {

    public void validateWallet(Wallet wallet) {
        if (wallet.getBalance() < 0) {
            throw new IllegalArgumentException("The balance cannot be negative\n");
        }

    }

    public void validateOperation(WalletOperation operation) {
        if (operation.getAmount() <= 0) {
            throw new IllegalArgumentException("The transaction amount must be greater than zero\n");
        }
    }
}