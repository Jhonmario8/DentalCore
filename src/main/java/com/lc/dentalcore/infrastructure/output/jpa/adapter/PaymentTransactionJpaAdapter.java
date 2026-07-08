package com.lc.dentalcore.infrastructure.output.jpa.adapter;

import com.lc.dentalcore.domain.model.PaymentTransaction;
import com.lc.dentalcore.domain.spi.IPaymentTransactionPersistencePort;
import com.lc.dentalcore.infrastructure.output.jpa.mapper.IPaymentTransactionMapper;
import com.lc.dentalcore.infrastructure.output.jpa.repository.IPaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentTransactionJpaAdapter implements IPaymentTransactionPersistencePort {

    private final IPaymentTransactionMapper mapper;
    private final IPaymentTransactionRepository paymentTransactionRepository;

    @Override
    public void savePaymentTransaction(PaymentTransaction paymentTransaction) {
        paymentTransactionRepository.save(mapper.toEntity(paymentTransaction));
    }

    @Override
    public List<PaymentTransaction> findAllByDate(LocalDate date) {
        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = date.plusDays(1).atStartOfDay();
        return paymentTransactionRepository.findAllByTransactionDateGreaterThanEqualAndTransactionDateLessThan(startDate,endDate)
                .stream().map(mapper::toDomain).toList() ;
    }
}
