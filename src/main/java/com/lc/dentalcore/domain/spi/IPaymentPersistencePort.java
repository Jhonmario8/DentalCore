package com.lc.dentalcore.domain.spi;

import com.lc.dentalcore.domain.model.Payment;

import java.util.Optional;

public interface IPaymentPersistencePort {
    Payment savePayment(Payment payment);
    Optional<Payment> findByAppointmentId(Long id);
}
