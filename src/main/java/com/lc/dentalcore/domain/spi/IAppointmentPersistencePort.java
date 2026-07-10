package com.lc.dentalcore.domain.spi;

import com.lc.dentalcore.domain.model.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface IAppointmentPersistencePort {
    Appointment saveAppointment(Appointment appointment);
    Optional<Appointment> findById(Long id);
    List<Appointment> findAllByDate(LocalDate date);
    boolean existsByDateAndTime(LocalDate date, LocalTime time);
}
