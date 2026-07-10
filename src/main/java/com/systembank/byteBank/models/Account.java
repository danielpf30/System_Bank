package com.systembank.byteBank.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "contas")
public class Account {

    public Account() {
        this.accountType = AccountType.CURRENT;
        this.balance = BigDecimal.ZERO;
    }

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, updatable = false, unique = true)
    private User user;

    @Column(name = "saldo", nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    @org.hibernate.annotations.GeneratedColumn("num_conta")
    @Column(name = "num_conta", nullable = false, updatable = false, unique = true)
    private int accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private AccountType accountType;
}
