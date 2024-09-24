package an.kondratev.springwallets.Impl;

import an.kondratev.springwallets.model.Wallet;
import an.kondratev.springwallets.model.WalletOperation;
import an.kondratev.springwallets.repository.WalletRepository;
import an.kondratev.springwallets.service.WalletOperationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletOperationServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletOperationService walletOperationService;

    private Wallet wallet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        wallet = new Wallet();
        wallet.setWalletId(UUID.randomUUID());
        wallet.setBalance(100L);
    }

    @Test
    void testOperationDeposit_Success() {
        WalletOperation operation = new WalletOperation();
        operation.setWalletId(wallet.getWalletId());
        operation.setOperationType(WalletOperation.OperationType.DEPOSIT);
        operation.setAmount(50L);

        when(walletRepository.findByWalletId(wallet.getWalletId())).thenReturn(wallet);

        walletOperationService.operation(operation);

        assertEquals(150L, wallet.getBalance());
        verify(walletRepository).save(wallet);
    }

    @Test
    void testOperationWithdraw_Success() {
        WalletOperation operation = new WalletOperation();
        operation.setWalletId(wallet.getWalletId());
        operation.setOperationType(WalletOperation.OperationType.WITHDRAW);
        operation.setAmount(50L);

        when(walletRepository.findByWalletId(wallet.getWalletId())).thenReturn(wallet);

        walletOperationService.operation(operation);

        assertEquals(50L, wallet.getBalance());
        verify(walletRepository).save(wallet);
    }

    @Test
    void testOperationWithdraw_InsufficientFunds() {
        WalletOperation operation = new WalletOperation();
        operation.setWalletId(wallet.getWalletId());
        operation.setOperationType(WalletOperation.OperationType.WITHDRAW);
        operation.setAmount(150L);

        when(walletRepository.findByWalletId(wallet.getWalletId())).thenReturn(wallet);

        Executable executable = () -> walletOperationService.operation(operation);
        assertThrows(IllegalArgumentException.class, executable, "Недостаточно средств для снятия");
        verify(walletRepository, never()).save(any());
    }

    @Test
    void testOperation_WalletNotFound() {
        WalletOperation operation = new WalletOperation();
        operation.setWalletId(UUID.randomUUID());
        operation.setOperationType(WalletOperation.OperationType.WITHDRAW);
        operation.setAmount(50L);

        when(walletRepository.findByWalletId(operation.getWalletId())).thenReturn(null);

        Executable executable = () -> walletOperationService.operation(operation);
        assertThrows(IllegalArgumentException.class, executable, "Кошелек не найден");
        verify(walletRepository, never()).save(any());
    }
}

