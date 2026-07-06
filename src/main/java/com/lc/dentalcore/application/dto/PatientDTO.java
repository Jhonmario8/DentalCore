package com.lc.dentalcore.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lc.dentalcore.application.constants.ApplicationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(min = 3, max = 100, message = ApplicationConstants.MSG_NAME_SIZE_BETWEEN)
    private String name;
    @NotBlank(message = ApplicationConstants.MSG_IDENTIFICATION_NUMBER_NOT_BLANK)
    private String identificationNumber;
    @NotBlank(message = ApplicationConstants.MSG_PHONE_NOT_BLANK)
    @Size(min = 7, max = 15, message = ApplicationConstants.MSG_PHONE_SIZE_BETWEEN)
    private String phoneNumber;
    @Email(message = ApplicationConstants.MSG_EMAIL_NOT_VALID)
    private String email;
    private LocalDate birthDate;
    @Size(max = 500, message = ApplicationConstants.MSG_NOTES_SIZE_MAX)
    private String notes;

}
