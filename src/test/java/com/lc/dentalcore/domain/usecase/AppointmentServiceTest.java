package com.lc.dentalcore.domain.usecase;

import com.lc.dentalcore.domain.exception.*;
import com.lc.dentalcore.domain.model.Appointment;
import com.lc.dentalcore.domain.model.AppointmentStatus;
import com.lc.dentalcore.domain.model.Patient;
import com.lc.dentalcore.domain.spi.IAppointmentPersistencePort;
import com.lc.dentalcore.domain.spi.IPatientPersistencePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    private static final Long PATIENT_ID = 1L;
    private static final Long APPOINTMENT_ID = 10L;

    @Mock
    private IAppointmentPersistencePort appointmentPersistencePort;
    @Mock
    private IPatientPersistencePort patientPersistencePort;

    @InjectMocks
    private AppointmentService appointmentService;

    private static Patient patient(boolean active) {
        Patient patient = new Patient();
        patient.setId(PATIENT_ID);
        patient.setName("Paciente de Ejemplo");
        patient.setActive(active);
        return patient;
    }

    private static Appointment appointment(LocalDate date, LocalTime time, AppointmentStatus status) {
        return new Appointment(APPOINTMENT_ID, PATIENT_ID, date, time, "Tratamiento de ejemplo", status);
    }

    @Nested
    @DisplayName("createAppointment")
    class CreateAppointment {

        @Test
        @DisplayName("una cita válida se guarda con estado PENDING")
        void validAppointmentIsSavedAsPending() {
            // given
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            Appointment appointment = appointment(tomorrow, LocalTime.of(9, 0), null);
            when(patientPersistencePort.findById(PATIENT_ID)).thenReturn(Optional.of(patient(true)));
            when(appointmentPersistencePort.existsByDateAndTime(tomorrow, LocalTime.of(9, 0))).thenReturn(false);
            when(appointmentPersistencePort.saveAppointment(any(Appointment.class))).thenAnswer(inv -> inv.getArgument(0));

            // when
            Appointment result = appointmentService.createAppointment(appointment);

            // then
            assertEquals(AppointmentStatus.PENDING, result.getStatus());
            verify(appointmentPersistencePort).saveAppointment(appointment);
        }

        @Test
        @DisplayName("el estado enviado por el cliente se ignora y se fuerza PENDING")
        void clientProvidedStatusIsOverriddenWithPending() {
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            Appointment appointment = appointment(tomorrow, LocalTime.of(9, 0), AppointmentStatus.ATTENDED);
            when(patientPersistencePort.findById(PATIENT_ID)).thenReturn(Optional.of(patient(true)));
            when(appointmentPersistencePort.existsByDateAndTime(any(), any())).thenReturn(false);
            when(appointmentPersistencePort.saveAppointment(any(Appointment.class))).thenAnswer(inv -> inv.getArgument(0));

            Appointment result = appointmentService.createAppointment(appointment);

            assertEquals(AppointmentStatus.PENDING, result.getStatus());
        }

        @Test
        @DisplayName("una cita en una fecha pasada lanza PastAppointmentTimeException")
        void pastDateThrows() {
            Appointment appointment = appointment(LocalDate.now().minusDays(1), LocalTime.of(9, 0), null);
            when(patientPersistencePort.findById(PATIENT_ID)).thenReturn(Optional.of(patient(true)));

            assertThrows(PastAppointmentTimeException.class, () -> appointmentService.createAppointment(appointment));

            verify(appointmentPersistencePort, never()).existsByDateAndTime(any(), any());
            verify(appointmentPersistencePort, never()).saveAppointment(any());
        }

        @Test
        @DisplayName("una cita hoy a una hora que ya pasó lanza PastAppointmentTimeException")
        void todayWithPastTimeThrows() {
            // 00:00 de hoy siempre queda en el pasado mientras el test corre después de medianoche
            Appointment appointment = appointment(LocalDate.now(), LocalTime.MIN, null);
            when(patientPersistencePort.findById(PATIENT_ID)).thenReturn(Optional.of(patient(true)));

            assertThrows(PastAppointmentTimeException.class, () -> appointmentService.createAppointment(appointment));

            verify(appointmentPersistencePort, never()).saveAppointment(any());
        }

        @Test
        @DisplayName("un horario ya ocupado lanza DuplicateAppointmentException")
        void occupiedSlotThrows() {
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            Appointment appointment = appointment(tomorrow, LocalTime.of(10, 30), null);
            when(patientPersistencePort.findById(PATIENT_ID)).thenReturn(Optional.of(patient(true)));
            when(appointmentPersistencePort.existsByDateAndTime(tomorrow, LocalTime.of(10, 30))).thenReturn(true);

            assertThrows(DuplicateAppointmentException.class, () -> appointmentService.createAppointment(appointment));

            verify(appointmentPersistencePort, never()).saveAppointment(any());
        }

        @Test
        @DisplayName("un paciente inexistente lanza PatientNotFoundException")
        void unknownPatientThrows() {
            Appointment appointment = appointment(LocalDate.now().plusDays(1), LocalTime.of(9, 0), null);
            when(patientPersistencePort.findById(PATIENT_ID)).thenReturn(Optional.empty());

            assertThrows(PatientNotFoundException.class, () -> appointmentService.createAppointment(appointment));

            verifyNoInteractions(appointmentPersistencePort);
        }

        @Test
        @DisplayName("un paciente inactivo se trata como no encontrado")
        void inactivePatientThrows() {
            Appointment appointment = appointment(LocalDate.now().plusDays(1), LocalTime.of(9, 0), null);
            when(patientPersistencePort.findById(PATIENT_ID)).thenReturn(Optional.of(patient(false)));

            assertThrows(PatientNotFoundException.class, () -> appointmentService.createAppointment(appointment));

            verifyNoInteractions(appointmentPersistencePort);
        }
    }

    @Nested
    @DisplayName("updateStatus")
    class UpdateStatus {

        @ParameterizedTest(name = "{0} -> {1} es válido")
        @CsvSource({
                "PENDING, CONFIRMED",
                "PENDING, CANCELLED",
                "CONFIRMED, CANCELLED",
                "CONFIRMED, ATTENDED"
        })
        @DisplayName("transiciones válidas actualizan y guardan la cita")
        void validTransitions(AppointmentStatus current, AppointmentStatus next) {
            // given: cita de hoy para que ATTENDED no choque con la regla de fecha futura
            Appointment existing = appointment(LocalDate.now(), LocalTime.of(9, 0), current);
            when(appointmentPersistencePort.findById(APPOINTMENT_ID)).thenReturn(Optional.of(existing));
            when(appointmentPersistencePort.saveAppointment(any(Appointment.class))).thenAnswer(inv -> inv.getArgument(0));

            // when
            Appointment result = appointmentService.updateStatus(APPOINTMENT_ID, next);

            // then
            assertEquals(next, result.getStatus());
            verify(appointmentPersistencePort).saveAppointment(existing);
        }

        @ParameterizedTest(name = "{0} -> {1} lanza ConflictException")
        @CsvSource({
                "PENDING, ATTENDED",
                "CONFIRMED, PENDING",
                "ATTENDED, CONFIRMED",
                "ATTENDED, CANCELLED",
                "CANCELLED, PENDING",
                "CANCELLED, CONFIRMED"
        })
        @DisplayName("transiciones inválidas lanzan ConflictException y no guardan")
        void invalidTransitions(AppointmentStatus current, AppointmentStatus next) {
            Appointment existing = appointment(LocalDate.now(), LocalTime.of(9, 0), current);
            when(appointmentPersistencePort.findById(APPOINTMENT_ID)).thenReturn(Optional.of(existing));

            assertThrows(ConflictException.class, () -> appointmentService.updateStatus(APPOINTMENT_ID, next));

            assertEquals(current, existing.getStatus());
            verify(appointmentPersistencePort, never()).saveAppointment(any());
        }

        @Test
        @DisplayName("un estado nulo lanza BadRequest")
        void nullStatusThrows() {
            Appointment existing = appointment(LocalDate.now(), LocalTime.of(9, 0), AppointmentStatus.PENDING);
            when(appointmentPersistencePort.findById(APPOINTMENT_ID)).thenReturn(Optional.of(existing));

            assertThrows(BadRequest.class, () -> appointmentService.updateStatus(APPOINTMENT_ID, null));

            verify(appointmentPersistencePort, never()).saveAppointment(any());
        }

        @Test
        @DisplayName("marcar como ATTENDED una cita futura lanza FutureAppointmentException")
        void attendingFutureAppointmentThrows() {
            Appointment existing = appointment(LocalDate.now().plusDays(1), LocalTime.of(9, 0), AppointmentStatus.CONFIRMED);
            when(appointmentPersistencePort.findById(APPOINTMENT_ID)).thenReturn(Optional.of(existing));

            assertThrows(FutureAppointmentException.class,
                    () -> appointmentService.updateStatus(APPOINTMENT_ID, AppointmentStatus.ATTENDED));

            verify(appointmentPersistencePort, never()).saveAppointment(any());
        }

        @Test
        @DisplayName("una cita inexistente lanza AppointmentNotFoundException")
        void unknownAppointmentThrows() {
            when(appointmentPersistencePort.findById(APPOINTMENT_ID)).thenReturn(Optional.empty());

            assertThrows(AppointmentNotFoundException.class,
                    () -> appointmentService.updateStatus(APPOINTMENT_ID, AppointmentStatus.CONFIRMED));

            verify(appointmentPersistencePort, never()).saveAppointment(any());
        }
    }

    @Nested
    @DisplayName("findAllByDate")
    class FindAllByDate {

        @Test
        @DisplayName("con fecha consulta esa fecha")
        void withDateUsesGivenDate() {
            LocalDate date = LocalDate.of(2030, 1, 15);
            List<Appointment> expected = List.of(appointment(date, LocalTime.of(8, 0), AppointmentStatus.PENDING));
            when(appointmentPersistencePort.findAllByDate(date)).thenReturn(expected);

            assertSame(expected, appointmentService.findAllByDate(date));
        }

        @Test
        @DisplayName("sin fecha consulta el día de hoy")
        void withoutDateUsesToday() {
            when(appointmentPersistencePort.findAllByDate(LocalDate.now())).thenReturn(List.of());

            assertTrue(appointmentService.findAllByDate(null).isEmpty());

            verify(appointmentPersistencePort).findAllByDate(LocalDate.now());
        }
    }
}
