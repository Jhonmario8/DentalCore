package com.lc.dentalcore.infrastructure.input.controller;

import com.lc.dentalcore.application.dto.PaymentDTO;
import com.lc.dentalcore.application.dto.PaymentResponseDTO;
import com.lc.dentalcore.application.handler.IPaymentHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final IPaymentHandler paymentHandler;

    @PostMapping()
    public ResponseEntity<PaymentResponseDTO> createPayment(@RequestBody PaymentDTO paymentDTO) {
        PaymentResponseDTO responseDTO = paymentHandler.createPayment(paymentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }


}
