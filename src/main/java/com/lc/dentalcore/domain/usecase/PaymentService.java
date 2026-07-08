package com.lc.dentalcore.domain.usecase;

import com.lc.dentalcore.domain.api.IPaymentServicePort;
import com.lc.dentalcore.domain.exception.AppointmentNotFoundException;
import com.lc.dentalcore.domain.exception.InvalidPaymentAmountException;
import com.lc.dentalcore.domain.exception.PaymentAlreadyExistException;
import com.lc.dentalcore.domain.model.Appointment;
import com.lc.dentalcore.domain.model.Payment;
import com.lc.dentalcore.domain.model.PaymentStatus;
import com.lc.dentalcore.domain.spi.IAppointmentPersistencePort;
import com.lc.dentalcore.domain.spi.IPaymentPersistencePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@RequiredArgsConstructor
public class PaymentService implements IPaymentServicePort {

    private final IPaymentPersistencePort paymentPersistencePort;
    private final IAppointmentPersistencePort appointmentPersistencePort;


    @Override
    @Transactional
    public Payment createPayment(Payment payment) {

        paymentPersistencePort.findByAppointmentId(payment.getAppointmentId()).ifPresent(existingPayment -> {
            throw new PaymentAlreadyExistException();
        });

        Appointment appointment = appointmentPersistencePort.findById(payment.getAppointmentId())
                .orElseThrow(AppointmentNotFoundException::new);

        payment.setPatientId(appointment.getPatientId());
        if (payment.getAmountPaid().compareTo(BigDecimal.ZERO) < 0 || payment.getAmountPaid().compareTo(payment.getTreatmentCost()) > 0) {
            throw new InvalidPaymentAmountException();
        }
        payment.setBalance(payment.getTreatmentCost().subtract(payment.getAmountPaid()));
        if (payment.getBalance().compareTo(payment.getTreatmentCost()) == 0){
            payment.setStatus(PaymentStatus.PENDING);
        } else if (payment.getBalance().compareTo(BigDecimal.ZERO) == 0) {

            payment.setStatus(PaymentStatus.PAID);
        }else {
            payment.setStatus(PaymentStatus.PARTIAL);
        }
        payment.setPaymentDate(LocalDateTime.now());
        return paymentPersistencePort.savePayment(payment);
    }
}
