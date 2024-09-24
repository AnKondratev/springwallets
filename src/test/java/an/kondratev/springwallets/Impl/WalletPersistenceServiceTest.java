package an.kondratev.springwallets.Impl;

import an.kondratev.springwallets.model.Wallet;
import an.kondratev.springwallets.repository.WalletRepository;
import an.kondratev.springwallets.service.WalletPersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WalletPersistenceServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletPersistenceService walletPersistenceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveWallet_WhenWalletIdIsNull_ShouldGenerateNewId() {

        Wallet wallet = new Wallet();
        wallet.setBalance(100L);
        assert wallet.getWalletId() == null;

        walletPersistenceService.saveWallet(wallet);

        ArgumentCaptor<Wallet> walletCaptor = ArgumentCaptor.forClass(Wallet.class);
        verify(walletRepository, times(1)).save(walletCaptor.capture());

        Wallet capturedWallet = walletCaptor.getValue();
        assertEquals(100L, capturedWallet.getBalance());
    }

    @Test
    void testSaveWallet_WhenWalletIdIsNotNull_ShouldNotGenerateNewId() {

        UUID existingWalletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setWalletId(existingWalletId);
        wallet.setBalance(100L);

        walletPersistenceService.saveWallet(wallet);

        ArgumentCaptor<Wallet> walletCaptor = ArgumentCaptor.forClass(Wallet.class);
        verify(walletRepository, times(1)).save(walletCaptor.capture());

        Wallet capturedWallet = walletCaptor.getValue();
        assertEquals(existingWalletId, capturedWallet.getWalletId());
        assertEquals(100L, capturedWallet.getBalance());
    }

    @Test
    void testFindByUUID_ShouldReturnWallet() {
        UUID walletId = UUID.randomUUID();
        Wallet expectedWallet = new Wallet();
        expectedWallet.setWalletId(walletId);
        expectedWallet.setBalance(200L);

        when(walletRepository.findByWalletId(walletId)).thenReturn(expectedWallet);

        Wallet actualWallet = walletPersistenceService.findByUUID(walletId);

        assertEquals(expectedWallet, actualWallet);
        verify(walletRepository, times(1)).findByWalletId(walletId);
    }
}