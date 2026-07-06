package com.lc.dentalcore.domain.model;

import com.lc.dentalcore.domain.api.IPasswordServicePort;
import com.lc.dentalcore.domain.exception.InvalidEmail;
import com.lc.dentalcore.domain.exception.InvalidPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*\\d).{8,}$";

    private Long id;
    private String email;
    private String password;
    private Boolean active=true;


    public void encodePassword(IPasswordServicePort passwordServicePort) {
        this.password = passwordServicePort.encodePassword(this.password);
    }

    public void validate(){
        if (!this.email.matches(EMAIL_PATTERN)){
            throw new InvalidEmail();
        }
        if (!password.matches(PASSWORD_PATTERN)) {
            throw new InvalidPassword();
        }

    }
}
