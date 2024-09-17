package an.kondratev.springwallets.controller;

import an.kondratev.springwallets.Impl.WalletServiceImpl;
import an.kondratev.springwallets.model.Wallet;
import an.kondratev.springwallets.model.WalletOperation;
import an.kondratev.springwallets.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WalletControllerTest {

    @InjectMocks
    private WalletController walletController;

    @Mock
    private WalletService walletService;

    @Mock
    private WalletServiceImpl walletServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveWallet() {
        Wallet wallet = new Wallet();
        String result = walletController.saveWallet(wallet);
        assertEquals("Новый счет успешно создан", result);
        verify(walletService, times(1)).saveWallet(wallet);
    }


    @Test
    void findWalletById_validUUID() {
        UUID uuid = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setWalletId(uuid);
        when(walletServiceImpl.isValidUUID(uuid.toString())).thenReturn(true);
        when(walletService.findByUUID(uuid)).thenReturn(wallet);
        String result = walletController.findWalletById(uuid.toString());
        assertEquals(wallet.toString(), result);
        verify(walletServiceImpl, times(1)).isValidUUID(uuid.toString());
        verify(walletService, times(1)).findByUUID(uuid);
    }

    @Test
    void findWalletById_invalidUUID() {
        String invalidUUID = "invalid-uuid";
        when(walletServiceImpl.isValidUUID(invalidUUID)).thenReturn(false);
        String result = walletController.findWalletById(invalidUUID);
        assertEquals("Некорректный формат UUID!", result);
        verify(walletServiceImpl, times(1)).isValidUUID(invalidUUID);
        verify(walletService, never()).findByUUID(any());
    }

    @Test
    void operation() {
        WalletOperation operation = new WalletOperation();
        String result = walletController.operation(operation);
        assertEquals("Операция прошла успешно", result);
        verify(walletService, times(1)).operation(operation);
    }
}
