package com.lc.dentalcore.domain.exception;

import com.lc.dentalcore.domain.constants.DomainConstants;

public class InvalidEmail extends BadRequest {
    public InvalidEmail() {
        super(DomainConstants.INVALID_EMAIL_ADDRESS);
    }
}
