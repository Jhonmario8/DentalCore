package com.lc.dentalcore.domain.usecase;

import com.lc.dentalcore.domain.api.IAppointmentServicePort;
import com.lc.dentalcore.domain.exception.DuplicateAppointmentException;
import com.lc.dentalcore.domain.exception.PastAppointmentTimeException;
import com.lc.dentalcore.domain.exception.PatientNotFoundException;
import com.lc.dentalcore.domain.model.Appointment;
import com.lc.dentalcore.domain.model.AppointmentStatus;
import com.lc.dentalcore.domain.model.Patient;
import com.lc.dentalcore.domain.spi.IAppointmentPersistencePort;
import com.lc.dentalcore.domain.spi.IPatientPersistencePort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

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
}
