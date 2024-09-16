package an.kondratev.springwallets.controller;

import an.kondratev.springwallets.model.Wallet;
import an.kondratev.springwallets.model.WalletOperation;
import an.kondratev.springwallets.service.WalletService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class WalletController {

    private final WalletService walletService;


    @GetMapping("wallets")
    public List<Wallet> getWallets() {
        return walletService.getWallets();
    }

    @PostMapping("save_wallet")
    public String saveWallet(@RequestBody Wallet wallet) {
        walletService.saveWallet(wallet);
        return "Saved wallet";
    }

    @GetMapping("wallets/{uuid}")
    public Wallet findWalletById(@PathVariable UUID uuid) {
        return walletService.findByUUID(uuid);
    }

    @PostMapping("wallet")
    public void operation(@RequestBody WalletOperation operation) {
        walletService.operation(operation);
    }
}
