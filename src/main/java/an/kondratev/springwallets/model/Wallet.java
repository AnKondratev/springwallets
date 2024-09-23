package an.kondratev.springwallets.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

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
    private AtomicLong balance;


    public Wallet() {
        this.walletId = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return "Идентификатор: " + walletId + "\n"
               + "Баланс счета: " + balance;
    }
}
