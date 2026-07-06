package com.lc.dentalcore.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lc.dentalcore.application.constants.ApplicationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientDTO {

    @NotBlank(message = ApplicationConstants.MSG_NAME_NOT_BLANK)
    private String name;
    @NotBlank(message = ApplicationConstants.MSG_PHONE_NOT_BLANK)
    private String phoneNumber;
    @Email(message = ApplicationConstants.MSG_EMAIL_NOT_VALID)
    private String email;
    private LocalDate birthDate;
    private String notes;

}
