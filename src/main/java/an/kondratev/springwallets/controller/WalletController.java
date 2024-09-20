package an.kondratev.springwallets.controller;

import an.kondratev.springwallets.model.Wallet;
import an.kondratev.springwallets.model.WalletOperation;
import an.kondratev.springwallets.service.WalletService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("save_wallet")
    public String saveWallet(@RequestBody Wallet wallet) {
        walletService.saveWallet(wallet);
        return "Новый счет успешно создан" + "\n" + wallet.toString();
    }

    @GetMapping("wallets/{uuid}")
    public ResponseEntity<String> findWalletById(@PathVariable String uuid) {
        Wallet wallet = walletService.findByUUID(UUID.fromString(uuid));
        return ResponseEntity.ok(wallet.toString());
    }

    @PostMapping("wallet")
    public String operation(@RequestBody WalletOperation operation) {
        walletService.operation(operation);
        return "Операция прошла успешно";
    }
}
