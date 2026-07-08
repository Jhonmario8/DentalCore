package com.lc.dentalcore.domain.usecase;

import com.lc.dentalcore.domain.api.IPaymentServicePort;
import com.lc.dentalcore.domain.exception.*;
import com.lc.dentalcore.domain.model.*;
import com.lc.dentalcore.domain.spi.IAppointmentPersistencePort;
import com.lc.dentalcore.domain.spi.IPatientPersistencePort;
import com.lc.dentalcore.domain.spi.IPaymentPersistencePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
public class PaymentService implements IPaymentServicePort {

    private final IPaymentPersistencePort paymentPersistencePort;
    private final IAppointmentPersistencePort appointmentPersistencePort;
    private final IPatientPersistencePort patientPersistencePort;


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
        return savePayment(payment);
    }



    @Override
    public Payment updateMount(Long id, BigDecimal mount) {
        Payment payment = paymentPersistencePort.findById(id)
                .orElseThrow(PaymentNotFoundException::new);

        BigDecimal newAmountPaid = payment.getAmountPaid().add(mount);
        if (newAmountPaid.compareTo(payment.getTreatmentCost()) > 0) {
            throw new InvalidPaymentAmountException();
        }
        payment.setAmountPaid(newAmountPaid);
        payment.setBalance(payment.getTreatmentCost().subtract(newAmountPaid));
        return savePayment(payment);

    }

    @Override
    public PaymentHistory getAllByPatientId(Long patientId) {
        Patient patient = patientPersistencePort.findById(patientId)
                .orElseThrow(PatientNotFoundException::new);
        List<Payment> payments = paymentPersistencePort.findAllByPatientId(patient.getId());
        BigDecimal balance = payments.stream()
                .map(Payment::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new PaymentHistory(payments,balance);
    }

    private Payment savePayment(Payment payment) {

        if (payment.getBalance().compareTo(payment.getTreatmentCost()) == 0) {
            payment.setStatus(PaymentStatus.PENDING);

        } else if (payment.getBalance().compareTo(BigDecimal.ZERO) == 0) {
            payment.setStatus(PaymentStatus.PAID);

        } else {
            payment.setStatus(PaymentStatus.PARTIAL);
        }

        payment.setPaymentDate(LocalDateTime.now());

        return paymentPersistencePort.savePayment(payment);
    }
}
