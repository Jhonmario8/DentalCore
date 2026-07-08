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
public class Payment {

    private Long id;
    private Long appointmentId;
    private Long patientId;
    private BigDecimal treatmentCost;
    private BigDecimal amountPaid;
    private BigDecimal balance;
    private PaymentStatus status;
    private LocalDateTime paymentDate;

}
