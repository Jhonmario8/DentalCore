package com.lc.dentalcore.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lc.dentalcore.application.constants.ApplicationConstants;
import com.lc.dentalcore.domain.model.AppointmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentDTO {
    private Long id;
    @NotNull(message = ApplicationConstants.MSG_PATIENT_ID_CAN_NOT_BE_NULL)
    private Long patientId;
    @NotNull(message = ApplicationConstants.MSG_APPOINTMENT_DATE_CAN_NOT_BE_NULL)
    private LocalDate appointmentDate;
    @NotNull(message = ApplicationConstants.MSG_APPOINTMENT_TIME_CAN_NOT_BE_NULL)
    private LocalTime appointmentTime;
    @NotBlank(message = ApplicationConstants.MSG_TREATMENT_CAN_NOT_BE_BLANK)
    @Size(min = 3, max = 100, message = ApplicationConstants.MSG_TREATMENT_SIZE)
    private String treatment;
    private AppointmentStatus status;
}
