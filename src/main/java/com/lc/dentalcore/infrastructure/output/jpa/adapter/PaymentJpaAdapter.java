package com.lc.dentalcore.infrastructure.output.jpa.adapter;

import com.lc.dentalcore.domain.model.Payment;
import com.lc.dentalcore.domain.spi.IPaymentPersistencePort;
import com.lc.dentalcore.infrastructure.output.jpa.mapper.IPaymentEntityMapper;
import com.lc.dentalcore.infrastructure.output.jpa.repository.IPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentJpaAdapter implements IPaymentPersistencePort {

    private final IPaymentRepository paymentRepository;
    private final IPaymentEntityMapper mapper;


    @Override
    public Payment savePayment(Payment payment) {
        return mapper.toDomain(paymentRepository.save(mapper.toEntity(payment)));
    }

    @Override
    public Optional<Payment> findByAppointmentId(Long id) {
        return paymentRepository.findByAppointmentId(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id).map(mapper::toDomain);
    }
}
