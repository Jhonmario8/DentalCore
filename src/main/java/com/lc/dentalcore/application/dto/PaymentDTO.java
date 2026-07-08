package com.lc.dentalcore.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lc.dentalcore.application.constants.ApplicationConstants;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentDTO {
    @NotNull(message = ApplicationConstants.MSG_APPOINTMENT_ID_REQUIRED)
    private Long appointmentId;
    @NotNull(message = ApplicationConstants.MSG_TREATMENT_COST_REQUIRED)
    private BigDecimal treatmentCost;
    @NotNull(message = ApplicationConstants.MSG_AMOUNT_PAID_REQUIRED)
    private BigDecimal amountPaid;


}
