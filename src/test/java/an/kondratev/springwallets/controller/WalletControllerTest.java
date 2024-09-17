package an.kondratev.springwallets.controller;

import an.kondratev.springwallets.model.Wallet;
import an.kondratev.springwallets.model.WalletOperation;
import an.kondratev.springwallets.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WalletControllerTest {

    @InjectMocks
    private WalletController walletController;

    @Mock
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getWallets() {
        // Arrange
        List<Wallet> wallets = Collections.singletonList(new Wallet());
        when(walletService.getWallets()).thenReturn(wallets);

        // Act
        List<Wallet> result = walletController.getWallets();

        // Assert
        assertEquals(wallets, result);
        verify(walletService, times(1)).getWallets();
    }

    @Test
    void saveWallet() {
        // Arrange
        Wallet wallet = new Wallet();

        // Act
        String response = walletController.saveWallet(wallet);

        // Assert
        assertEquals("Saved wallet", response);
        verify(walletService, times(1)).saveWallet(wallet);
    }

    @Test
    void findWalletById() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        Wallet wallet = new Wallet();
        when(walletService.findByUUID(uuid)).thenReturn(wallet);

        // Act
        Wallet result = walletController.findWalletById(uuid);

        // Assert
        assertEquals(wallet, result);
        verify(walletService, times(1)).findByUUID(uuid);
    }

    @Test
    void operation() {
        // Arrange
        WalletOperation operation = new WalletOperation();

        // Act
        walletController.operation(operation);

        // Assert
        verify(walletService, times(1)).operation(operation);
    }
}
