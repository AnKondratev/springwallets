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

//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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

//    @Test
//    void testConcurrentWithdraw_SerializableIsolation() throws InterruptedException {
//        WalletOperation operationWithdraw = new WalletOperation();
//        operationWithdraw.setWalletId(wallet.getWalletId());
//        operationWithdraw.setOperationType(WalletOperation.OperationType.WITHDRAW);
//        operationWithdraw.setAmount(50L); // Снимаем 50
//
//        when(walletRepository.findByWalletId(wallet.getWalletId())).thenReturn(wallet);
//
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//        CountDownLatch latch = new CountDownLatch(1);
//        Runnable withdrawTask = () -> {
//            try {
//                latch.await();
//                walletOperationService.operation(operationWithdraw);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        };
//
//        executor.submit(withdrawTask);
//        executor.submit(withdrawTask);
//
//        latch.countDown();
//        executor.shutdown();
//        boolean terminated = executor.awaitTermination(1, java.util.concurrent.TimeUnit.MINUTES);
//
//        if (!terminated) {
//            executor.shutdownNow();
//            fail("Executor did not terminate in the specified time.");
//        }
//
//        assertEquals(100L, wallet.getBalance()); // Ожидаем, что баланс останется 100 после двух попыток
//        verify(walletRepository, times(1)).save(wallet); // Проверяем, что save был вызван один раз
//    }
//
//    @Test
//    void testConcurrentDepositAndWithdraw_SerializableIsolation() throws InterruptedException {
//        WalletOperation depositOperation = new WalletOperation();
//        depositOperation.setWalletId(wallet.getWalletId());
//        depositOperation.setOperationType(WalletOperation.OperationType.DEPOSIT);
//        depositOperation.setAmount(100L); // Депозит 100
//
//        WalletOperation withdrawOperation = new WalletOperation();
//        withdrawOperation.setWalletId(wallet.getWalletId());
//        withdrawOperation.setOperationType(WalletOperation.OperationType.WITHDRAW);
//        withdrawOperation.setAmount(150L); // Попытка снять 150
//
//        when(walletRepository.findByWalletId(wallet.getWalletId())).thenReturn(wallet);
//
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//        CountDownLatch latch = new CountDownLatch(1);
//
//        Runnable depositTask = () -> {
//            try {
//                latch.await();
//                walletOperationService.operation(depositOperation); // Выполняем депозит
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        };
//
//        Runnable withdrawTask = () -> {
//            try {
//                latch.await();
//                Executable executable = () -> walletOperationService.operation(withdrawOperation);
//                assertThrows(IllegalArgumentException.class, executable, "Недостаточно средств для снятия"); // Проверяем на исключение
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        };
//
//        executor.submit(depositTask);
//        executor.submit(withdrawTask);
//
//        latch.countDown();
//
//        executor.shutdown();
//        boolean terminated = executor.awaitTermination(1, java.util.concurrent.TimeUnit.MINUTES);
//
//        if (!terminated) {
//            executor.shutdownNow();
//            fail("Executor did not terminate in the specified time.");
//        }
//
//        assertEquals(200L, wallet.getBalance()); // Проверьте, что депозит успешен
//        verify(walletRepository, times(1)).save(wallet); // Проверка на сохранение
//    }
}

