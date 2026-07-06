package com.lc.dentalcore.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lc.dentalcore.application.constants.ApplicationConstants;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    @NotBlank(message = ApplicationConstants.MSG_EMAIL_REQUIRED)
    private String email;
    @NotBlank(message = ApplicationConstants.MSG_PASSWORD_REQUIRED)
    private String password;

}
