package com.lc.dentalcore.domain.usecase;

import com.lc.dentalcore.domain.api.IPaymentServicePort;
import com.lc.dentalcore.domain.exception.*;
import com.lc.dentalcore.domain.model.*;
import com.lc.dentalcore.domain.spi.IAppointmentPersistencePort;
import com.lc.dentalcore.domain.spi.IPatientPersistencePort;
import com.lc.dentalcore.domain.spi.IPaymentPersistencePort;
import com.lc.dentalcore.domain.spi.IPaymentTransactionPersistencePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class PaymentService implements IPaymentServicePort {

    private final IPaymentPersistencePort paymentPersistencePort;
    private final IAppointmentPersistencePort appointmentPersistencePort;
    private final IPatientPersistencePort patientPersistencePort;
    private final IPaymentTransactionPersistencePort paymentTransactionPersistencePort;


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

        Payment payment1 = savePayment(payment);

        registerTransaction(payment1.getId(), payment1.getAmountPaid());

        return payment1;
    }



    @Override
    @Transactional
    public Payment updateMount(Long id, BigDecimal mount) {
        if (mount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPaymentAmountException();
        }
        Payment payment = paymentPersistencePort.findById(id)
                .orElseThrow(PaymentNotFoundException::new);

        BigDecimal newAmountPaid = payment.getAmountPaid().add(mount);
        if (newAmountPaid.compareTo(payment.getTreatmentCost()) > 0) {
            throw new InvalidPaymentAmountException();
        }

        payment.setAmountPaid(newAmountPaid);
        payment.setBalance(payment.getTreatmentCost().subtract(newAmountPaid));
        Payment payment1 = savePayment(payment);

        registerTransaction(payment1.getId(), mount);

        return payment1;
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

    @Override
    public DashboardSummary getDashboardSummary() {
        List<Appointment> appointments = appointmentPersistencePort.findAllByDate(LocalDate.now());

        long totalAppointments = appointments.size();

        Map<AppointmentStatus, Long> appointmentsByStatus = appointments.stream()
                .collect(Collectors.groupingBy(Appointment::getStatus, Collectors.counting()));

        List<PaymentTransaction> transactions = paymentTransactionPersistencePort.findAllByDate(LocalDate.now());

        BigDecimal totalCollected = transactions.stream()
                .map(PaymentTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new DashboardSummary(totalAppointments, appointmentsByStatus, totalCollected);
    }

    @Override
    public List<PaymentTransaction> getAllTransactionsByPaymentId(Long paymentId) {
        return paymentTransactionPersistencePort.findAllByPaymentId(paymentId);
    }

    @Override
    public EarningsResponse getEarnings(PeriodType period, LocalDate date) {
        if (date == null){
            date = LocalDate.now();
        }

        LocalDate startDate;
        LocalDate endDate;

        switch (period) {

            case WEEKLY -> {
                startDate = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                endDate = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
            }

            case MONTHLY -> {
                startDate = date.withDayOfMonth(1);
                endDate = date.withDayOfMonth(date.lengthOfMonth());
            }

            case YEARLY -> {
                startDate = date.withDayOfYear(1);
                endDate = date.withDayOfYear(date.lengthOfYear());
            }

            default -> throw new InvalidPeriodException();
        }

        BigDecimal totalCollected = paymentTransactionPersistencePort.sumAmountByDateRange(
                startDate.atStartOfDay(),
                endDate.plusDays(1).atStartOfDay()
        );

        return new EarningsResponse(
                totalCollected,
                startDate,
                endDate
        );
    }

    private void registerTransaction(Long paymentId, BigDecimal amount) {

        PaymentTransaction transaction = new PaymentTransaction();

        transaction.setPaymentId(paymentId);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());

        paymentTransactionPersistencePort.savePaymentTransaction(transaction);
    }
    private Payment savePayment(Payment payment) {

        if (payment.getBalance().compareTo(payment.getTreatmentCost()) == 0) {
            payment.setStatus(PaymentStatus.PENDING);

        } else if (payment.getBalance().compareTo(BigDecimal.ZERO) == 0) {
            payment.setStatus(PaymentStatus.PAID);

        } else {
            payment.setStatus(PaymentStatus.PARTIAL);
        }
        return paymentPersistencePort.savePayment(payment);
    }
}
