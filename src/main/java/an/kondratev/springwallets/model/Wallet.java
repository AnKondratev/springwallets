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
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private UUID walletId;
    private long balance;

    @Override
    public String toString() {
        return "Идентификатор: " + walletId + "\n"
                + "Баланс счета: " + balance;
    }
}
