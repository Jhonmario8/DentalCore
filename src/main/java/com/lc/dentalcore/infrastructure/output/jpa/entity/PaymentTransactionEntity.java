package com.lc.dentalcore.infrastructure.output.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_transactions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "payment_id", nullable = false)
    private Long paymentId;
    private BigDecimal amount;
    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;
}
