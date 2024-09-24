package an.kondratev.springwallets.service;

import an.kondratev.springwallets.model.Wallet;
import an.kondratev.springwallets.model.WalletOperation;
import org.springframework.stereotype.Service;

@Service
public class WalletValidationService {

    public void validateWallet(Wallet wallet) {
        if (wallet.getBalance() < 0) {
            throw new IllegalArgumentException("Баланс не может быть отрицательным");
        }

    }

    public void validateOperation(WalletOperation operation) {
        if (operation.getAmount() <= 0) {
            throw new IllegalArgumentException("Сумма операции должна быть больше нуля");
        }
    }
}