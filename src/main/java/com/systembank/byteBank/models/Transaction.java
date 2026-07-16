package com.systembank.byteBank.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "transacoes")
public class Transaction {

    public Transaction() {
        this.dateTime = LocalDateTime.now();
    }
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, insertable = false)
    private UUID id;

    @Column(name = "valor", nullable = false, precision = 15, scale = 2)
    private BigDecimal value;

    @Column(name = "data_hora",  nullable = false, updatable = false)
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "remetente_id",nullable = false)
    private Account sender;

    @ManyToOne
    @JoinColumn(name = "destinatario_id", nullable = true)
    private Account receiver;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TransactionType type;
}
