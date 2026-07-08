package com.lc.dentalcore.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Appointment {

    private Long id;
    private Long patientId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String treatment;
    private AppointmentStatus status;

}
