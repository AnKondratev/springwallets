package an.kondratev.springwallets.Impl;

import an.kondratev.springwallets.model.Wallet;
import an.kondratev.springwallets.model.WalletOperation;
import an.kondratev.springwallets.service.WalletValidationService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WalletValidationServiceTest {

    private final WalletValidationService walletValidationService = new WalletValidationService();

    @Test
    void validateWallet_ShouldNotThrow_WhenBalanceIsNonNegative() {
        Wallet wallet = new Wallet();
        wallet.setBalance(0L);
        walletValidationService.validateWallet(wallet);
    }

    @Test
    void validateWallet_ShouldThrow_WhenBalanceIsNegative() {
        Wallet wallet = new Wallet();
        wallet.setBalance((long) -1);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, ()
                -> walletValidationService.validateWallet(wallet));
        assertEquals("Баланс не может быть отрицательным", thrown.getMessage());
    }

    @Test
    void validateOperation_ShouldNotThrow_WhenAmountIsPositive() {
        WalletOperation operation = new WalletOperation();
        operation.setAmount(100);
        walletValidationService.validateOperation(operation);
    }

    @Test
    void validateOperation_ShouldNotThrow_WhenAmountIsZero() {
        WalletOperation operation = new WalletOperation();
        operation.setAmount(0);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, ()
                -> walletValidationService.validateOperation(operation));
        assertEquals("Сумма операции должна быть больше нуля", thrown.getMessage());
    }

    @Test
    void validateOperation_ShouldThrow_WhenAmountIsNegative() {
        WalletOperation operation = new WalletOperation();
        operation.setAmount(-50);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, ()
                -> walletValidationService.validateOperation(operation));
        assertEquals("Сумма операции должна быть больше нуля", thrown.getMessage());
    }
}