package com.lc.dentalcore.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentTransaction {

    private Long id;
    private Long paymentId;
    private BigDecimal amount;
    private LocalDateTime transactionDate;

}
