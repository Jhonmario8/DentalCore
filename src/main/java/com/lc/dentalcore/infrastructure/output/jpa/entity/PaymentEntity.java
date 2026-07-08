package com.lc.dentalcore.infrastructure.output.jpa.entity;

import com.lc.dentalcore.domain.model.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "appointment_id", nullable = false)
    private Long appointmentId;
    @Column(name = "patient_id", nullable = false)
    private Long patientId;
    @Column(name = "treatment_cost", nullable = false)
    private BigDecimal treatmentCost;
    @Column(name = "amount_paid")
    private BigDecimal amountPaid;
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
}
