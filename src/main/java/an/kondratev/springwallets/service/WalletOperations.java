package an.kondratev.springwallets.service;

import an.kondratev.springwallets.model.Wallet;

public interface WalletOperations {
    void execute(Wallet wallet, long amount);
}