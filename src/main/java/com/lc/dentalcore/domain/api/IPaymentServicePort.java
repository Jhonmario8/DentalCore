package com.lc.dentalcore.domain.api;

import com.lc.dentalcore.domain.model.Payment;

import java.math.BigDecimal;

public interface IPaymentServicePort {
    Payment createPayment(Payment payment);
    Payment updateMount(Long id, BigDecimal mount);
}
