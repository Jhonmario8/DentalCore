package com.lc.dentalcore.domain.usecase;

import com.lc.dentalcore.domain.exception.*;
import com.lc.dentalcore.domain.model.Patient;
import com.lc.dentalcore.domain.spi.IPatientPersistencePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    // Datos ficticios: no corresponden a ninguna persona real.
    private static final String NAME = "Paciente de Ejemplo";
    private static final String ID_NUMBER = "1000000001";
    private static final String PHONE = "3000000000";
    private static final String EMAIL = "paciente@ejemplo.com";

    @Mock
    private IPatientPersistencePort patientPersistencePort;

    @InjectMocks
    private PatientService patientService;

    private static Patient validPatient() {
        return new Patient(null, NAME, ID_NUMBER, PHONE, EMAIL, LocalDate.of(2000, 1, 1), "Nota de ejemplo", true);
    }

    @Nested
    @DisplayName("createPatient")
    class CreatePatient {

        @Test
        @DisplayName("un paciente válido y único se guarda")
        void validPatientIsSaved() {
            // given
            Patient patient = validPatient();
            when(patientPersistencePort.findByEmail(EMAIL)).thenReturn(Optional.empty());
            when(patientPersistencePort.findByIdentificationNumber(ID_NUMBER)).thenReturn(Optional.empty());
            when(patientPersistencePort.existsByPhoneNumber(PHONE)).thenReturn(false);
            when(patientPersistencePort.savePatient(patient)).thenReturn(patient);

            // when
            Patient result = patientService.createPatient(patient);

            // then
            assertSame(patient, result);
            assertTrue(result.getActive());
            verify(patientPersistencePort).savePatient(patient);
        }

        @ParameterizedTest
        @ValueSource(strings = {"123456", "30000000a0", "+573000000000", "300 000 0000", "123456789012345678901"})
        @DisplayName("un teléfono con formato inválido lanza InvalidPhoneNumberException")
        void invalidPhoneThrows(String phone) {
            Patient patient = validPatient();
            patient.setPhoneNumber(phone);

            assertThrows(InvalidPhoneNumberException.class, () -> patientService.createPatient(patient));

            verifyNoInteractions(patientPersistencePort);
        }

        @ParameterizedTest
        @ValueSource(strings = {"sin-arroba.com", "paciente@", "paciente@ejemplo", "@ejemplo.com"})
        @DisplayName("un email con formato inválido lanza InvalidEmail")
        void invalidEmailThrows(String email) {
            Patient patient = validPatient();
            patient.setEmail(email);

            assertThrows(InvalidEmail.class, () -> patientService.createPatient(patient));

            verifyNoInteractions(patientPersistencePort);
        }

        @ParameterizedTest
        @ValueSource(strings = {"12", "ABC123", "100-000", ""})
        @DisplayName("un documento con formato inválido lanza InvalidIdentificationNumberException")
        void invalidIdentificationThrows(String identification) {
            Patient patient = validPatient();
            patient.setIdentificationNumber(identification);

            assertThrows(InvalidIdentificationNumberException.class, () -> patientService.createPatient(patient));

            verifyNoInteractions(patientPersistencePort);
        }

        @Test
        @DisplayName("un email ya registrado lanza una excepción de conflicto y no guarda")
        void duplicateEmailThrows() {
            Patient patient = validPatient();
            when(patientPersistencePort.findByEmail(EMAIL)).thenReturn(Optional.of(validPatient()));

            // Nota: hoy el código reutiliza UsernameAlreadyExistsException para el email duplicado.
            assertThrows(UsernameAlreadyExistsException.class, () -> patientService.createPatient(patient));

            verify(patientPersistencePort, never()).savePatient(any());
        }

        @Test
        @DisplayName("un documento ya registrado lanza IdentificationNumberAlreadyExistException")
        void duplicateIdentificationThrows() {
            Patient patient = validPatient();
            when(patientPersistencePort.findByEmail(EMAIL)).thenReturn(Optional.empty());
            when(patientPersistencePort.findByIdentificationNumber(ID_NUMBER)).thenReturn(Optional.of(validPatient()));

            assertThrows(IdentificationNumberAlreadyExistException.class, () -> patientService.createPatient(patient));

            verify(patientPersistencePort, never()).savePatient(any());
        }

        @Test
        @DisplayName("un teléfono ya registrado lanza PhoneNumberAlreadyExist")
        void duplicatePhoneThrows() {
            Patient patient = validPatient();
            when(patientPersistencePort.findByEmail(EMAIL)).thenReturn(Optional.empty());
            when(patientPersistencePort.findByIdentificationNumber(ID_NUMBER)).thenReturn(Optional.empty());
            when(patientPersistencePort.existsByPhoneNumber(PHONE)).thenReturn(true);

            assertThrows(PhoneNumberAlreadyExist.class, () -> patientService.createPatient(patient));

            verify(patientPersistencePort, never()).savePatient(any());
        }
    }

    @Nested
    @DisplayName("updatePatient")
    class UpdatePatient {

        @Test
        @DisplayName("actualiza los campos del paciente existente y conserva su id y estado")
        void updatesExistingPatient() {
            // given
            Patient existing = validPatient();
            existing.setId(5L);
            Patient changes = new Patient(null, "Otro Paciente de Ejemplo", "2000000002", "3111111111",
                    "otro@ejemplo.com", LocalDate.of(1990, 6, 15), "Otra nota", null);
            when(patientPersistencePort.findById(5L)).thenReturn(Optional.of(existing));
            when(patientPersistencePort.savePatient(any(Patient.class))).thenAnswer(inv -> inv.getArgument(0));

            // when
            Patient result = patientService.updatePatient(5L, changes);

            // then
            assertSame(existing, result);
            assertEquals(5L, result.getId());
            assertTrue(result.getActive());
            assertEquals("Otro Paciente de Ejemplo", result.getName());
            assertEquals("2000000002", result.getIdentificationNumber());
            assertEquals("3111111111", result.getPhoneNumber());
            assertEquals("otro@ejemplo.com", result.getEmail());
            assertEquals(LocalDate.of(1990, 6, 15), result.getBirthDate());
            assertEquals("Otra nota", result.getNotes());
        }

        @Test
        @DisplayName("un paciente inexistente lanza PatientNotFoundException")
        void unknownPatientThrows() {
            when(patientPersistencePort.findById(99L)).thenReturn(Optional.empty());

            assertThrows(PatientNotFoundException.class, () -> patientService.updatePatient(99L, validPatient()));

            verify(patientPersistencePort, never()).savePatient(any());
        }

        @Test
        @DisplayName("datos con formato inválido no se guardan")
        void invalidDataIsNotSaved() {
            Patient changes = validPatient();
            changes.setPhoneNumber("123");
            when(patientPersistencePort.findById(5L)).thenReturn(Optional.of(validPatient()));

            assertThrows(InvalidPhoneNumberException.class, () -> patientService.updatePatient(5L, changes));

            verify(patientPersistencePort, never()).savePatient(any());
        }
    }

    @Nested
    @DisplayName("findAll")
    class FindAll {

        @Test
        @DisplayName("con nombre busca por nombre")
        void withNameSearchesByName() {
            List<Patient> expected = List.of(validPatient());
            when(patientPersistencePort.findAllByName("Ejemplo")).thenReturn(expected);

            assertSame(expected, patientService.findAll("Ejemplo"));

            verify(patientPersistencePort, never()).findAll();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   "})
        @DisplayName("sin nombre (nulo o en blanco) lista todos")
        void withoutNameListsAll(String name) {
            List<Patient> expected = List.of(validPatient());
            when(patientPersistencePort.findAll()).thenReturn(expected);

            assertSame(expected, patientService.findAll(name));

            verify(patientPersistencePort, never()).findAllByName(anyString());
        }
    }

    @Nested
    @DisplayName("inactivatePatient")
    class InactivatePatient {

        @Test
        @DisplayName("un paciente activo queda inactivo y se guarda")
        void activePatientIsInactivated() {
            Patient existing = validPatient();
            when(patientPersistencePort.findById(5L)).thenReturn(Optional.of(existing));

            patientService.inactivatePatient(5L);

            assertFalse(existing.getActive());
            verify(patientPersistencePort).savePatient(existing);
        }

        @Test
        @DisplayName("un paciente ya inactivo lanza PatientAlreadyInactiveException")
        void alreadyInactiveThrows() {
            Patient existing = validPatient();
            existing.setActive(false);
            when(patientPersistencePort.findById(5L)).thenReturn(Optional.of(existing));

            assertThrows(PatientAlreadyInactiveException.class, () -> patientService.inactivatePatient(5L));

            verify(patientPersistencePort, never()).savePatient(any());
        }

        @Test
        @DisplayName("un paciente inexistente lanza PatientNotFoundException")
        void unknownPatientThrows() {
            when(patientPersistencePort.findById(5L)).thenReturn(Optional.empty());

            assertThrows(PatientNotFoundException.class, () -> patientService.inactivatePatient(5L));

            verify(patientPersistencePort, never()).savePatient(any());
        }
    }
}
