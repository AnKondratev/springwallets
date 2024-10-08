package an.kondratev.springwallets.controller;

import an.kondratev.springwallets.model.Wallet;
import an.kondratev.springwallets.model.WalletOperation;
import an.kondratev.springwallets.repository.WalletOperationRepository;
import an.kondratev.springwallets.service.WalletService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class WalletController {

    private final WalletService walletService;
    private final WalletOperationRepository operationRepository;

    @PostMapping("save_wallet")
    public String saveWallet(@RequestBody Wallet wallet) {
        walletService.saveWallet(wallet);
        return "The new account has been successfully created" + "\n" + wallet.toString();
    }

    @GetMapping("wallets/{uuid}")
    public String findWalletById(@PathVariable String uuid) {
        Wallet wallet = walletService.findByUUID(UUID.fromString(uuid));
        return wallet.toString();
    }

    @PostMapping("wallet")
    public String operation(@RequestBody WalletOperation operation) {
        walletService.performOperation(operation);
        operationRepository.save(operation);
        return "The transaction was successful\n";
    }
}
