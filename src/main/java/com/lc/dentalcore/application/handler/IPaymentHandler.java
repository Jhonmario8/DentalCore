package com.lc.dentalcore.application.handler;

import com.lc.dentalcore.application.dto.PaymentDTO;
import com.lc.dentalcore.application.dto.PaymentHistoryDTO;
import com.lc.dentalcore.application.dto.PaymentResponseDTO;
import com.lc.dentalcore.domain.model.DashboardSummary;

import java.math.BigDecimal;

public interface IPaymentHandler {
    PaymentResponseDTO createPayment(PaymentDTO paymentDTO);
    PaymentResponseDTO updateMount(Long id, BigDecimal mount);
    PaymentHistoryDTO getAllByPatientId(Long patientId);
    DashboardSummary getDashboardSummary();
}
