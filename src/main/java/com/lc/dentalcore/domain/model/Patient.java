package com.lc.dentalcore.domain.model;

import com.lc.dentalcore.domain.exception.InvalidEmail;
import com.lc.dentalcore.domain.exception.InvalidIdentificationNumberException;
import com.lc.dentalcore.domain.exception.InvalidPhoneNumberException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Patient {

        private static final String CELLPHONE_PATTERN = "^\\d{7,20}$";
        private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        private static final String DOCUMENT_PATTERN = "^\\d{3,}$";

        private Long id;
        private String name;
        private String identificationNumber;
        private String phoneNumber;
        private String email;
        private LocalDate birthDate;
        private String notes;
        private Boolean active=true;

        public void validate(){
                if (!this.phoneNumber.matches(CELLPHONE_PATTERN)){
                        throw new InvalidPhoneNumberException();
                }
                if (!this.email.matches(EMAIL_PATTERN)){
                        throw new InvalidEmail();
                }
                if (!this.identificationNumber.matches(DOCUMENT_PATTERN)){
                        throw new InvalidIdentificationNumberException();
                }


        }
}
