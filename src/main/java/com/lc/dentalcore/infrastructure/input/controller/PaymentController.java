package com.lc.dentalcore.infrastructure.input.controller;

import com.lc.dentalcore.application.dto.PaymentDTO;
import com.lc.dentalcore.application.dto.PaymentHistoryDTO;
import com.lc.dentalcore.application.dto.PaymentResponseDTO;
import com.lc.dentalcore.application.handler.IPaymentHandler;
import com.lc.dentalcore.domain.model.DashboardSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController

@RequiredArgsConstructor
public class PaymentController {

    private final IPaymentHandler paymentHandler;

    @PostMapping("/payments")
    public ResponseEntity<PaymentResponseDTO> createPayment(@RequestBody PaymentDTO paymentDTO) {
        PaymentResponseDTO responseDTO = paymentHandler.createPayment(paymentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/payments/{id}/amount")
    public ResponseEntity<PaymentResponseDTO> updateMount(@PathVariable Long id, @RequestParam BigDecimal mount) {
        PaymentResponseDTO responseDTO = paymentHandler.updateMount(id, mount);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/payments/patient/{patientId}")
    public ResponseEntity<PaymentHistoryDTO> getAllByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(paymentHandler.getAllByPatientId(patientId));
    }

    @GetMapping("/dashboard/summary")
    public ResponseEntity<DashboardSummary> getDashboardSummary() {
        return ResponseEntity.ok(paymentHandler.getDashboardSummary());
    }
}
