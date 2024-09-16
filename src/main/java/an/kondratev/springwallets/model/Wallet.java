package an.kondratev.springwallets.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private UUID walletId;
    private long balance;
}
