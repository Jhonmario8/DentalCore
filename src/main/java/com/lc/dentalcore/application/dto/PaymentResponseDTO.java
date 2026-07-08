package com.lc.dentalcore.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lc.dentalcore.domain.model.PaymentStatus;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponseDTO {

    private Long appointmentId;
    private Long patientId;
    private BigDecimal treatmentCost;
    private BigDecimal amountPaid;
    private BigDecimal balance;
    private PaymentStatus status;
    private LocalDateTime paymentDate;

}
