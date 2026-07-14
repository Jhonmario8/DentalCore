package com.lc.dentalcore.domain.spi;

import com.lc.dentalcore.domain.model.PaymentTransaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IPaymentTransactionPersistencePort {

    void savePaymentTransaction(PaymentTransaction paymentTransaction);
    List<PaymentTransaction> findAllByDate(LocalDate date);
    List<PaymentTransaction> findAllByPaymentId(Long paymentId);
    BigDecimal sumAmountByDateRange(LocalDateTime startDate, LocalDateTime endDate);

}
