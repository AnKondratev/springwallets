package an.kondratev.springwallets.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@Table(name = "wallets")
public class Wallet implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    @Column(unique = true)
    private UUID walletId;

    @Column(name = "balance")
    private Long balance;

    @Override
    public String toString() {
        return "Identifier: " + walletId + "\n"
               + "Account balance: " + getBalance();
    }
}

