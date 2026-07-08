package com.lc.dentalcore.application.handler;

import com.lc.dentalcore.application.dto.PaymentDTO;
import com.lc.dentalcore.application.dto.PaymentResponseDTO;

public interface IPaymentHandler {
    PaymentResponseDTO createPayment(PaymentDTO paymentDTO);
}
