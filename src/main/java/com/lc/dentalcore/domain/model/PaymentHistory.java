package com.lc.dentalcore.domain.model;


import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentHistory {
    private List<Payment> payments;
    private BigDecimal totalPendingBalance;

}
