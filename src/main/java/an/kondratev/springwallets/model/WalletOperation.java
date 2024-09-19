package an.kondratev.springwallets.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@Table(name = "wallets_operations")
public class WalletOperation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;
    private UUID walletId;

    @Enumerated(EnumType.STRING)
    private OperationType operationType;
    private long amount;

    public enum OperationType {
        DEPOSIT,
        WITHDRAW
    }
}

