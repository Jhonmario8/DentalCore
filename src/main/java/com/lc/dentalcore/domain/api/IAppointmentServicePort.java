package com.lc.dentalcore.domain.api;

import com.lc.dentalcore.domain.model.Appointment;
import com.lc.dentalcore.domain.model.AppointmentStatus;

import java.time.LocalDate;
import java.util.List;

public interface IAppointmentServicePort {
    Appointment createAppointment(Appointment appointment);
    Appointment updateStatus(Long id, AppointmentStatus status);
    List<Appointment> findAllByDate(LocalDate date);
}
