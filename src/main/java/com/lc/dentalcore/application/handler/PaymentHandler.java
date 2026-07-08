package com.lc.dentalcore.application.handler;

import com.lc.dentalcore.application.dto.PaymentDTO;
import com.lc.dentalcore.application.dto.PaymentHistoryDTO;
import com.lc.dentalcore.application.dto.PaymentResponseDTO;
import com.lc.dentalcore.application.mapper.IPaymentHistoryMapper;
import com.lc.dentalcore.application.mapper.IPaymentMapper;
import com.lc.dentalcore.domain.api.IPaymentServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentHandler implements IPaymentHandler{

    private final IPaymentServicePort paymentServicePort;
    private final IPaymentMapper mapper;
    private final IPaymentHistoryMapper historyMapper;

    @Override
    public PaymentResponseDTO createPayment(PaymentDTO paymentDTO) {
        return mapper.toDto(paymentServicePort.createPayment(mapper.toDomain(paymentDTO)))  ;
    }

    @Override
    public PaymentResponseDTO updateMount(Long id, BigDecimal mount) {
        return mapper.toDto(paymentServicePort.updateMount(id, mount));
    }

    @Override
    public PaymentHistoryDTO getAllByPatientId(Long patientId) {
        return historyMapper.toDto(paymentServicePort.getAllByPatientId(patientId));
    }
}
