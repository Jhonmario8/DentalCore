package com.lc.dentalcore.domain.api;

import com.lc.dentalcore.domain.model.Payment;

public interface IPaymentServicePort {
    Payment createPayment(Payment payment);
}
