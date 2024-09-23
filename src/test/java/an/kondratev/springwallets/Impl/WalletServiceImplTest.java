package an.kondratev.springwallets.Impl;

import an.kondratev.springwallets.model.Wallet;
import an.kondratev.springwallets.model.WalletOperation;
import an.kondratev.springwallets.repository.WalletOperationRepository;
import an.kondratev.springwallets.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

class WalletServiceImplTest {

    private WalletServiceImpl walletService;
    private WalletRepository walletRepositoryMock;
    private WalletOperationRepository walletOperationRepositoryMock;

    @BeforeEach
    void setUp() {
        walletRepositoryMock = Mockito.mock(WalletRepository.class);
        walletOperationRepositoryMock = Mockito.mock(WalletOperationRepository.class);
        walletService = new WalletServiceImpl(walletRepositoryMock, walletOperationRepositoryMock);
    }

    @Test
    void testSaveWallet() {
        Wallet wallet = new Wallet();
        wallet.setBalance(new AtomicLong(100));

        walletService.saveWallet(wallet);
        Mockito.verify(walletRepositoryMock, Mockito.times(1)).save(wallet);
    }

    @Test
    void testFindByUUID() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setWalletId(walletId);

        Mockito.when(walletRepositoryMock.findByWalletId(walletId)).thenReturn(wallet);
        Wallet foundWallet = walletService.findByUUID(walletId);

        assertEquals(wallet, foundWallet);
    }

    @Test
    void testOperationWithDeposit() {
        Wallet wallet = new Wallet();
        UUID walletId = UUID.randomUUID();
        wallet.setWalletId(walletId);
        wallet.setBalance(new AtomicLong(100));

        Mockito.when(walletRepositoryMock.findByWalletId(walletId)).thenReturn(wallet);

        WalletOperation operation = new WalletOperation();
        operation.setWalletId(walletId);
        operation.setOperationType(WalletOperation.OperationType.DEPOSIT);
        operation.setAmount(50);

        walletService.operation(operation);

        assertEquals(150, wallet.getBalance().get());
        Mockito.verify(walletOperationRepositoryMock).save(operation);
    }

    @Test
    void testOperationWithWithdraw() {
        Wallet wallet = new Wallet();
        UUID walletId = UUID.randomUUID();
        wallet.setWalletId(walletId);
        wallet.setBalance(new AtomicLong(100));

        Mockito.when(walletRepositoryMock.findByWalletId(walletId)).thenReturn(wallet);

        WalletOperation operation = new WalletOperation();
        operation.setWalletId(walletId);
        operation.setOperationType(WalletOperation.OperationType.WITHDRAW);
        operation.setAmount(50);

        walletService.operation(operation);

        assertEquals(50, wallet.getBalance().get());
        Mockito.verify(walletOperationRepositoryMock).save(operation);
    }

    @Test
    void testPerformOperationMultiThreaded() throws InterruptedException {
        Wallet wallet = new Wallet();
        UUID walletId = UUID.randomUUID();
        wallet.setWalletId(walletId);
        wallet.setBalance(new AtomicLong(100));

        Mockito.when(walletRepositoryMock.findByWalletId(walletId)).thenReturn(wallet);

        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(20);
        for (int i = 0; i < 10; i++) {
            executor.execute(() -> {
                WalletOperation depositOperation = new WalletOperation();
                depositOperation.setWalletId(walletId);
                depositOperation.setOperationType(WalletOperation.OperationType.DEPOSIT);
                depositOperation.setAmount(10);
                walletService.operation(depositOperation);
                latch.countDown();
            });
            executor.execute(() -> {
                WalletOperation withdrawOperation = new WalletOperation();
                withdrawOperation.setWalletId(walletId);
                withdrawOperation.setOperationType(WalletOperation.OperationType.WITHDRAW);
                withdrawOperation.setAmount(5);
                walletService.operation(withdrawOperation);
                latch.countDown();
            });
        }

        latch.await();
        assertEquals(100 + (10 * 10) - (5 * 10), wallet.getBalance().get());
        executor.shutdown();
    }
}

