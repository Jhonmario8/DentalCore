package com.lc.dentalcore.domain.usecase;

import com.lc.dentalcore.domain.exception.*;
import com.lc.dentalcore.domain.model.*;
import com.lc.dentalcore.domain.spi.IAppointmentPersistencePort;
import com.lc.dentalcore.domain.spi.IPatientPersistencePort;
import com.lc.dentalcore.domain.spi.IPaymentPersistencePort;
import com.lc.dentalcore.domain.spi.IPaymentTransactionPersistencePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    // Montos ficticios, elegidos solo para que las cuentas sean fáciles de verificar.
    private static final BigDecimal COST = new BigDecimal("100000");
    private static final Long APPOINTMENT_ID = 10L;
    private static final Long PATIENT_ID = 1L;
    private static final Long PAYMENT_ID = 20L;

    @Mock
    private IPaymentPersistencePort paymentPersistencePort;
    @Mock
    private IAppointmentPersistencePort appointmentPersistencePort;
    @Mock
    private IPatientPersistencePort patientPersistencePort;
    @Mock
    private IPaymentTransactionPersistencePort paymentTransactionPersistencePort;

    @InjectMocks
    private PaymentService paymentService;

    private static Payment newPayment(String amountPaid) {
        Payment payment = new Payment();
        payment.setAppointmentId(APPOINTMENT_ID);
        payment.setTreatmentCost(COST);
        payment.setAmountPaid(new BigDecimal(amountPaid));
        return payment;
    }

    private static Payment existingPayment(String amountPaid) {
        BigDecimal paid = new BigDecimal(amountPaid);
        return new Payment(PAYMENT_ID, APPOINTMENT_ID, PATIENT_ID, COST, paid, COST.subtract(paid), null);
    }

    private static Appointment appointment() {
        return new Appointment(APPOINTMENT_ID, PATIENT_ID, LocalDate.now(), LocalTime.of(9, 0),
                "Tratamiento de ejemplo", AppointmentStatus.ATTENDED);
    }

    private void givenNewPaymentCanBeCreated() {
        when(paymentPersistencePort.findByAppointmentId(APPOINTMENT_ID)).thenReturn(Optional.empty());
        when(appointmentPersistencePort.findById(APPOINTMENT_ID)).thenReturn(Optional.of(appointment()));
    }

    private void givenSaveReturnsWithId() {
        when(paymentPersistencePort.savePayment(any(Payment.class))).thenAnswer(inv -> {
            Payment p = inv.getArgument(0);
            p.setId(PAYMENT_ID);
            return p;
        });
    }

    private PaymentTransaction capturedTransaction() {
        ArgumentCaptor<PaymentTransaction> captor = ArgumentCaptor.forClass(PaymentTransaction.class);
        verify(paymentTransactionPersistencePort).savePaymentTransaction(captor.capture());
        return captor.getValue();
    }

    @Nested
    @DisplayName("createPayment")
    class CreatePayment {

        @Test
        @DisplayName("un abono parcial queda PARTIAL con el saldo correcto y registra la transacción")
        void partialPayment() {
            // given
            givenNewPaymentCanBeCreated();
            givenSaveReturnsWithId();

            // when
            Payment result = paymentService.createPayment(newPayment("40000"));

            // then
            assertEquals(PaymentStatus.PARTIAL, result.getStatus());
            assertEquals(0, new BigDecimal("60000").compareTo(result.getBalance()));
            assertEquals(PATIENT_ID, result.getPatientId());

            PaymentTransaction tx = capturedTransaction();
            assertEquals(PAYMENT_ID, tx.getPaymentId());
            assertEquals(0, new BigDecimal("40000").compareTo(tx.getAmount()));
            assertNotNull(tx.getTransactionDate());
        }

        @Test
        @DisplayName("un pago por el total queda PAID con saldo cero")
        void fullPayment() {
            givenNewPaymentCanBeCreated();
            givenSaveReturnsWithId();

            Payment result = paymentService.createPayment(newPayment("100000"));

            assertEquals(PaymentStatus.PAID, result.getStatus());
            assertEquals(0, BigDecimal.ZERO.compareTo(result.getBalance()));
        }

        @Test
        @DisplayName("un pago de cero queda PENDING con saldo igual al costo")
        void zeroPayment() {
            givenNewPaymentCanBeCreated();
            givenSaveReturnsWithId();

            Payment result = paymentService.createPayment(newPayment("0"));

            assertEquals(PaymentStatus.PENDING, result.getStatus());
            assertEquals(0, COST.compareTo(result.getBalance()));
        }

        @ParameterizedTest
        @ValueSource(strings = {"-1", "100001"})
        @DisplayName("un monto negativo o mayor al costo lanza InvalidPaymentAmountException")
        void invalidAmountThrows(String amount) {
            givenNewPaymentCanBeCreated();

            assertThrows(InvalidPaymentAmountException.class, () -> paymentService.createPayment(newPayment(amount)));

            verify(paymentPersistencePort, never()).savePayment(any());
            verifyNoInteractions(paymentTransactionPersistencePort);
        }

        @Test
        @DisplayName("si la cita ya tiene pago lanza PaymentAlreadyExistException")
        void duplicatePaymentThrows() {
            when(paymentPersistencePort.findByAppointmentId(APPOINTMENT_ID)).thenReturn(Optional.of(existingPayment("0")));

            assertThrows(PaymentAlreadyExistException.class, () -> paymentService.createPayment(newPayment("1000")));

            verify(paymentPersistencePort, never()).savePayment(any());
            verifyNoInteractions(appointmentPersistencePort, paymentTransactionPersistencePort);
        }

        @Test
        @DisplayName("si la cita no existe lanza AppointmentNotFoundException")
        void unknownAppointmentThrows() {
            when(paymentPersistencePort.findByAppointmentId(APPOINTMENT_ID)).thenReturn(Optional.empty());
            when(appointmentPersistencePort.findById(APPOINTMENT_ID)).thenReturn(Optional.empty());

            assertThrows(AppointmentNotFoundException.class, () -> paymentService.createPayment(newPayment("1000")));

            verify(paymentPersistencePort, never()).savePayment(any());
            verifyNoInteractions(paymentTransactionPersistencePort);
        }
    }

    @Nested
    @DisplayName("updateMount (abonos)")
    class UpdateMount {

        @Test
        @DisplayName("un abono que no completa el total deja el pago PARTIAL")
        void partialInstallment() {
            // given
            when(paymentPersistencePort.findById(PAYMENT_ID)).thenReturn(Optional.of(existingPayment("40000")));
            givenSaveReturnsWithId();

            // when
            Payment result = paymentService.updateMount(PAYMENT_ID, new BigDecimal("30000"));

            // then
            assertEquals(PaymentStatus.PARTIAL, result.getStatus());
            assertEquals(0, new BigDecimal("70000").compareTo(result.getAmountPaid()));
            assertEquals(0, new BigDecimal("30000").compareTo(result.getBalance()));
            assertEquals(0, new BigDecimal("30000").compareTo(capturedTransaction().getAmount()));
        }

        @Test
        @DisplayName("un abono que completa el total deja el pago PAID con saldo cero")
        void completingInstallment() {
            when(paymentPersistencePort.findById(PAYMENT_ID)).thenReturn(Optional.of(existingPayment("40000")));
            givenSaveReturnsWithId();

            Payment result = paymentService.updateMount(PAYMENT_ID, new BigDecimal("60000"));

            assertEquals(PaymentStatus.PAID, result.getStatus());
            assertEquals(0, BigDecimal.ZERO.compareTo(result.getBalance()));
            assertEquals(0, new BigDecimal("60000").compareTo(capturedTransaction().getAmount()));
        }

        @Test
        @DisplayName("un abono que supera el saldo lanza InvalidPaymentAmountException")
        void exceedingInstallmentThrows() {
            when(paymentPersistencePort.findById(PAYMENT_ID)).thenReturn(Optional.of(existingPayment("40000")));

            assertThrows(InvalidPaymentAmountException.class,
                    () -> paymentService.updateMount(PAYMENT_ID, new BigDecimal("60001")));

            verify(paymentPersistencePort, never()).savePayment(any());
            verifyNoInteractions(paymentTransactionPersistencePort);
        }

        @ParameterizedTest
        @ValueSource(strings = {"0", "-5000"})
        @DisplayName("un abono de cero o negativo lanza InvalidPaymentAmountException sin consultar la BD")
        void nonPositiveInstallmentThrows(String amount) {
            assertThrows(InvalidPaymentAmountException.class,
                    () -> paymentService.updateMount(PAYMENT_ID, new BigDecimal(amount)));

            verifyNoInteractions(paymentPersistencePort, paymentTransactionPersistencePort);
        }

        @Test
        @DisplayName("un pago inexistente lanza PaymentNotFoundException")
        void unknownPaymentThrows() {
            when(paymentPersistencePort.findById(PAYMENT_ID)).thenReturn(Optional.empty());

            assertThrows(PaymentNotFoundException.class,
                    () -> paymentService.updateMount(PAYMENT_ID, new BigDecimal("1000")));

            verifyNoInteractions(paymentTransactionPersistencePort);
        }
    }

    @Nested
    @DisplayName("getAllByPatientId (historial)")
    class History {

        @Test
        @DisplayName("el saldo pendiente total es la suma de los saldos de cada pago")
        void totalBalanceIsSumOfBalances() {
            // given
            Patient patient = new Patient();
            patient.setId(PATIENT_ID);
            List<Payment> payments = List.of(
                    existingPayment("40000"),   // saldo 60000
                    existingPayment("100000"),  // saldo 0
                    existingPayment("0")        // saldo 100000
            );
            when(patientPersistencePort.findById(PATIENT_ID)).thenReturn(Optional.of(patient));
            when(paymentPersistencePort.findAllByPatientId(PATIENT_ID)).thenReturn(payments);

            // when
            PaymentHistory history = paymentService.getAllByPatientId(PATIENT_ID);

            // then
            assertSame(payments, history.getPayments());
            assertEquals(0, new BigDecimal("160000").compareTo(history.getTotalPendingBalance()));
        }

        @Test
        @DisplayName("sin pagos el saldo pendiente es cero")
        void noPaymentsMeansZeroBalance() {
            Patient patient = new Patient();
            patient.setId(PATIENT_ID);
            when(patientPersistencePort.findById(PATIENT_ID)).thenReturn(Optional.of(patient));
            when(paymentPersistencePort.findAllByPatientId(PATIENT_ID)).thenReturn(List.of());

            PaymentHistory history = paymentService.getAllByPatientId(PATIENT_ID);

            assertTrue(history.getPayments().isEmpty());
            assertEquals(0, BigDecimal.ZERO.compareTo(history.getTotalPendingBalance()));
        }

        @Test
        @DisplayName("un paciente inexistente lanza PatientNotFoundException")
        void unknownPatientThrows() {
            when(patientPersistencePort.findById(PATIENT_ID)).thenReturn(Optional.empty());

            assertThrows(PatientNotFoundException.class, () -> paymentService.getAllByPatientId(PATIENT_ID));

            verifyNoInteractions(paymentPersistencePort);
        }
    }

    @Nested
    @DisplayName("dashboard")
    class Dashboard {

        @Test
        @DisplayName("el resumen del día cuenta citas por estado y suma lo recaudado")
        void summaryCountsAndSums() {
            // given
            Appointment pending = appointment();
            pending.setStatus(AppointmentStatus.PENDING);
            Appointment attended1 = appointment();
            Appointment attended2 = appointment();
            when(appointmentPersistencePort.findAllByDate(LocalDate.now()))
                    .thenReturn(List.of(pending, attended1, attended2));
            when(paymentTransactionPersistencePort.findAllByDate(LocalDate.now())).thenReturn(List.of(
                    new PaymentTransaction(1L, PAYMENT_ID, new BigDecimal("25000"), LocalDateTime.now()),
                    new PaymentTransaction(2L, PAYMENT_ID, new BigDecimal("15000"), LocalDateTime.now())
            ));

            // when
            DashboardSummary summary = paymentService.getDashboardSummary();

            // then
            assertEquals(3L, summary.getTotalAppointmentsToday());
            assertEquals(Map.of(AppointmentStatus.PENDING, 1L, AppointmentStatus.ATTENDED, 2L),
                    summary.getAppointmentsByStatus());
            assertEquals(0, new BigDecimal("40000").compareTo(summary.getTotalCollectedToday()));
        }

        @Test
        @DisplayName("ingresos semanales: de lunes a domingo, consultando hasta el lunes siguiente (exclusivo)")
        void weeklyEarningsRange() {
            // 2030-01-16 es miércoles
            LocalDate date = LocalDate.of(2030, 1, 16);
            when(paymentTransactionPersistencePort.sumAmountByDateRange(
                    LocalDate.of(2030, 1, 14).atStartOfDay(), LocalDate.of(2030, 1, 21).atStartOfDay()))
                    .thenReturn(new BigDecimal("50000"));

            EarningsResponse response = paymentService.getEarnings(PeriodType.WEEKLY, date);

            assertEquals(LocalDate.of(2030, 1, 14), response.getStartDate());
            assertEquals(LocalDate.of(2030, 1, 20), response.getEndDate());
            assertEquals(0, new BigDecimal("50000").compareTo(response.getTotalCollected()));
        }

        @Test
        @DisplayName("ingresos mensuales: del primer al último día del mes (incluye febrero bisiesto)")
        void monthlyEarningsRange() {
            LocalDate date = LocalDate.of(2028, 2, 10);
            when(paymentTransactionPersistencePort.sumAmountByDateRange(
                    LocalDate.of(2028, 2, 1).atStartOfDay(), LocalDate.of(2028, 3, 1).atStartOfDay()))
                    .thenReturn(BigDecimal.ZERO);

            EarningsResponse response = paymentService.getEarnings(PeriodType.MONTHLY, date);

            assertEquals(LocalDate.of(2028, 2, 1), response.getStartDate());
            assertEquals(LocalDate.of(2028, 2, 29), response.getEndDate());
        }

        @Test
        @DisplayName("ingresos anuales: del 1 de enero al 31 de diciembre")
        void yearlyEarningsRange() {
            LocalDate date = LocalDate.of(2030, 7, 4);
            when(paymentTransactionPersistencePort.sumAmountByDateRange(
                    LocalDate.of(2030, 1, 1).atStartOfDay(), LocalDate.of(2031, 1, 1).atStartOfDay()))
                    .thenReturn(BigDecimal.TEN);

            EarningsResponse response = paymentService.getEarnings(PeriodType.YEARLY, date);

            assertEquals(LocalDate.of(2030, 1, 1), response.getStartDate());
            assertEquals(LocalDate.of(2030, 12, 31), response.getEndDate());
            assertEquals(BigDecimal.TEN, response.getTotalCollected());
        }

        @Test
        @DisplayName("sin fecha usa el día de hoy")
        void earningsWithoutDateUsesToday() {
            LocalDate today = LocalDate.now();
            when(paymentTransactionPersistencePort.sumAmountByDateRange(any(), any())).thenReturn(BigDecimal.ZERO);

            EarningsResponse response = paymentService.getEarnings(PeriodType.MONTHLY, null);

            assertEquals(today.withDayOfMonth(1), response.getStartDate());
            assertEquals(today.withDayOfMonth(today.lengthOfMonth()), response.getEndDate());
        }

        @Test
        @DisplayName("las transacciones de un pago se delegan al puerto")
        void transactionsByPayment() {
            List<PaymentTransaction> expected = List.of(
                    new PaymentTransaction(1L, PAYMENT_ID, BigDecimal.ONE, LocalDateTime.now()));
            when(paymentTransactionPersistencePort.findAllByPaymentId(PAYMENT_ID)).thenReturn(expected);

            assertSame(expected, paymentService.getAllTransactionsByPaymentId(PAYMENT_ID));
        }
    }
}
