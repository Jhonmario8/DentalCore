package com.lc.dentalcore.domain.api;

import com.lc.dentalcore.domain.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public interface IPaymentServicePort {
    Payment createPayment(Payment payment);
    Payment updateMount(Long id, BigDecimal mount);
    PaymentHistory getAllByPatientId(Long patientId);
    DashboardSummary getDashboardSummary();
    List<PaymentTransaction> getAllTransactionsByPaymentId(Long paymentId);
    EarningsResponse getEarnings(PeriodType period, LocalDate date);

}
