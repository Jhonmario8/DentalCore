package com.lc.dentalcore.domain.spi;

import com.lc.dentalcore.domain.model.PaymentTransaction;

import java.time.LocalDate;
import java.util.List;

public interface IPaymentTransactionPersistencePort {

    void savePaymentTransaction(PaymentTransaction paymentTransaction);
    List<PaymentTransaction> findAllByDate(LocalDate date);
    List<PaymentTransaction> findAllByPaymentId(Long paymentId);

}
