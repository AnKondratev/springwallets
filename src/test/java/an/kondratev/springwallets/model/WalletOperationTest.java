package an.kondratev.springwallets.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WalletOperationTest {

    private Wallet wallet;

    @BeforeEach
    void setUp() {
        wallet = new Wallet();
        wallet.setBalance(1000);
    }

    @Test
    void testDeposit() {
        long depositAmount = 500;
        WalletOperation.OperationType.DEPOSIT.execute(wallet, depositAmount);

        assertEquals(1500, wallet.getBalance());
    }

    @Test
    void testWithdraw() {
        long withdrawAmount = 300;
        WalletOperation.OperationType.WITHDRAW.execute(wallet, withdrawAmount);

        assertEquals(700, wallet.getBalance());
    }

    @Test
    void testWithdrawInsufficientFunds() {
        long withdrawAmount = 1200;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            WalletOperation.OperationType.WITHDRAW.execute(wallet, withdrawAmount);
        });

        assertEquals("Недостаточно средств", exception.getMessage());
    }

    @Test
    void testDepositWithZeroAmount() {
        long depositAmount = 0;
        WalletOperation.OperationType.DEPOSIT.execute(wallet, depositAmount);

        assertEquals(1000, wallet.getBalance());
    }

    @Test
    void testWithdrawWithZeroAmount() {
        long withdrawAmount = 0;
        WalletOperation.OperationType.WITHDRAW.execute(wallet, withdrawAmount);

        assertEquals(1000, wallet.getBalance());
    }

    @Test
    void testWithdrawNegativeAmount() {
        long withdrawAmount = -500;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            WalletOperation.OperationType.WITHDRAW.execute(wallet, withdrawAmount);
        });

        assertEquals("Внесите корректную сумму для пополнения", exception.getMessage());
    }

    @Test
    void testDepositNegativeAmount() {
        long depositAmount = -500;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            WalletOperation.OperationType.DEPOSIT.execute(wallet, depositAmount);
        });

        assertEquals("Внесите корректную сумму для пополнения", exception.getMessage());
    }
}