package an.kondratev.springwallets.Impl;

import an.kondratev.springwallets.model.Wallet;
import an.kondratev.springwallets.model.WalletOperation;
import an.kondratev.springwallets.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletServiceImplTest {

    @InjectMocks
    private WalletServiceImpl walletService;

    @Mock
    private WalletRepository walletRepository;

    private Wallet wallet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        wallet = new Wallet();
        wallet.setWalletId(UUID.randomUUID());
        wallet.setBalance(100L);
    }

    @Test
    void testSaveWallet_NewWallet_ShouldGenerateUUID() {
        Wallet newWallet = new Wallet();
        walletService.saveWallet(newWallet);

        assertNotNull(newWallet.getWalletId());
        verify(walletRepository).save(newWallet);
    }

    @Test
    void testSaveWallet_ExistingWallet_ShouldSaveWithoutChangingUUID() {
        walletService.saveWallet(wallet);
        verify(walletRepository).save(wallet);
    }

    @Test
    void testFindByUUID_ShouldReturnWallet() {
        when(walletRepository.findByWalletId(wallet.getWalletId())).thenReturn(wallet);

        Wallet foundWallet = walletService.findByUUID(wallet.getWalletId());

        assertEquals(wallet, foundWallet);
    }

    @Test
    void testFindByUUID_WalletNotFound_ShouldReturnNull() {
        when(walletRepository.findByWalletId(wallet.getWalletId())).thenReturn(null);

        Wallet foundWallet = walletService.findByUUID(UUID.randomUUID());

        assertNull(foundWallet);
    }

    @Test
    void testOperation_ValidDeposit_ShouldIncreaseBalance() {
        WalletOperation depositOperation = new WalletOperation();
        depositOperation.setWalletId(wallet.getWalletId());
        depositOperation.setOperationType(WalletOperation.OperationType.DEPOSIT);
        depositOperation.setAmount(50L);

        when(walletRepository.findByWalletId(wallet.getWalletId())).thenReturn(wallet);

        walletService.operation(depositOperation);

        assertEquals(150L, wallet.getBalance());
        verify(walletRepository).save(wallet);
    }

    @Test
    void testOperation_ValidWithdraw_ShouldDecreaseBalance() {
        WalletOperation withdrawOperation = new WalletOperation();
        withdrawOperation.setWalletId(wallet.getWalletId());
        withdrawOperation.setOperationType(WalletOperation.OperationType.WITHDRAW);
        withdrawOperation.setAmount(50L);

        when(walletRepository.findByWalletId(wallet.getWalletId())).thenReturn(wallet);

        walletService.operation(withdrawOperation);

        assertEquals(50L, wallet.getBalance());
        verify(walletRepository).save(wallet);
    }

    @Test
    void testOperation_Withdraw_InsufficientFunds_ShouldThrowException() {
        WalletOperation withdrawOperation = new WalletOperation();
        withdrawOperation.setWalletId(wallet.getWalletId());
        withdrawOperation.setOperationType(WalletOperation.OperationType.WITHDRAW);
        withdrawOperation.setAmount(150L); // Insufficient funds

        when(walletRepository.findByWalletId(wallet.getWalletId())).thenReturn(wallet);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()
                -> walletService.operation(withdrawOperation));

        assertEquals("Недостаточно средств для снятия", exception.getMessage());
        verify(walletRepository, never()).save(wallet);
    }

    @Test
    void testOperation_WalletNotFound_ShouldThrowException() {
        WalletOperation operation = new WalletOperation();
        operation.setWalletId(UUID.randomUUID()); // Non-existent wallet
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()
                -> walletService.operation(operation));

        assertEquals("Кошелек не найден", exception.getMessage());
    }
}

