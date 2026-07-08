package com.lc.dentalcore.domain.api;

import com.lc.dentalcore.domain.model.DashboardSummary;
import com.lc.dentalcore.domain.model.Payment;
import com.lc.dentalcore.domain.model.PaymentHistory;
import com.lc.dentalcore.domain.model.PaymentTransaction;

import java.math.BigDecimal;
import java.util.List;


public interface IPaymentServicePort {
    Payment createPayment(Payment payment);
    Payment updateMount(Long id, BigDecimal mount);
    PaymentHistory getAllByPatientId(Long patientId);
    DashboardSummary getDashboardSummary();
    List<PaymentTransaction> getAllTransactionsByPaymentId(Long paymentId);

}
