package com.lc.dentalcore.domain.usecase;

import com.lc.dentalcore.domain.api.IAppointmentServicePort;
import com.lc.dentalcore.domain.constants.DomainConstants;
import com.lc.dentalcore.domain.exception.*;
import com.lc.dentalcore.domain.model.Appointment;
import com.lc.dentalcore.domain.model.AppointmentStatus;
import com.lc.dentalcore.domain.model.Patient;
import com.lc.dentalcore.domain.spi.IAppointmentPersistencePort;
import com.lc.dentalcore.domain.spi.IPatientPersistencePort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class AppointmentService implements IAppointmentServicePort {

    private final IAppointmentPersistencePort appointmentPersistencePort;
    private final IPatientPersistencePort patientPersistencePort;

    @Override
    public Appointment createAppointment(Appointment appointment) {

        Patient patient = patientPersistencePort.findById(appointment.getPatientId()).orElseThrow(PatientNotFoundException::new);
        if (!patient.getActive()){
            throw new PatientNotFoundException();
        }
        if (appointment.getAppointmentDate().isBefore(LocalDate.now())){
            throw new PastAppointmentTimeException();
        }
        List<Appointment> appointments = appointmentPersistencePort.findAll();
        for (Appointment ap : appointments) {
           if (ap.getAppointmentDate().equals(appointment.getAppointmentDate()) && ap.getPatientId().equals(appointment.getPatientId())) {
               throw new DuplicateAppointmentException();
           }
        }
        appointment.setStatus(AppointmentStatus.PENDING);
        return appointmentPersistencePort.saveAppointment(appointment);
    }

    @Override
    public Appointment updateStatus(Long id, AppointmentStatus status) {
        Appointment appointment = appointmentPersistencePort.findById(id).orElseThrow(AppointmentNotFoundException::new);
        validateStatus(appointment.getStatus(), status);
        if (status.equals(AppointmentStatus.ATTENDED)){
            if (appointment.getAppointmentDate().isAfter(LocalDate.now())){
                throw new FutureAppointmentException();
            }
        }
        appointment.setStatus(status);
        return appointmentPersistencePort.saveAppointment(appointment);
    }

    @Override
    public List<Appointment> findAllByDate(LocalDate date) {
        return appointmentPersistencePort.findAllByDate(Objects.requireNonNullElseGet(date, LocalDate::now));
    }

    private void validateStatus(AppointmentStatus currentStatus, AppointmentStatus newStatus) {
        if (newStatus == null) {
            throw new BadRequest(DomainConstants.MSG_STATUS_NULL);
        }
        if (currentStatus.equals(AppointmentStatus.PENDING) && newStatus.equals(AppointmentStatus.ATTENDED)) {
            throw new ConflictException(DomainConstants.MSG_ONLY_CONFIRMED_CAN_BE_ATTENDED);
        }
        if (currentStatus.equals(AppointmentStatus.CONFIRMED) && newStatus.equals(AppointmentStatus.PENDING)) {
            throw new ConflictException(DomainConstants.MSG_CANNOT_GO_BACK_TO_PENDING);
        }
        if (currentStatus.equals(AppointmentStatus.ATTENDED)){
            throw new ConflictException(DomainConstants.MSG_CANNOT_UPDATE_ATTENDED);
        }
        if (currentStatus.equals(AppointmentStatus.CANCELLED)){
            throw new ConflictException(DomainConstants.MSG_CANNOT_UPDATE_CANCELLED);
        }
    }
}
