package an.kondratev.springwallets.model;

import an.kondratev.springwallets.service.WalletOperations;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;


@Data
@Entity
@Table(name = "wallets_operations")
public class WalletOperation {
    @Id
    @GeneratedValue
    @Column(unique = true)
    private Long id;
    private UUID walletId;
    @Enumerated(EnumType.STRING)
    private OperationType operationType;
    private long amount;

    public enum OperationType implements WalletOperations {
        DEPOSIT {
            @Override
            public void execute(Wallet wallet, long amount) {
                if (amount < 0) {
                    throw new IllegalArgumentException("Внесите корректную сумму для пополнения");
                }
                wallet.setBalance(wallet.getBalance() + amount);
                System.out.println("Баланс пополнен на сумму: " + amount);
                System.out.println("Новый баланс кошелька: " + wallet.getBalance());
            }
        },
        WITHDRAW {
            @Override
            public void execute(Wallet wallet, long amount) {
                if (amount < 0) {
                    throw new IllegalArgumentException("Внесите корректную сумму для пополнения");
                }
                if (wallet.getBalance() >= amount) {
                    wallet.setBalance(wallet.getBalance() - amount);
                    System.out.println("С баланса снята сумма: " + amount);
                    System.out.println("Новый баланс кошелька: " + wallet.getBalance());
                } else {
                    throw new IllegalArgumentException("Недостаточно средств");
                }
            }
        }
    }
}
