package com.lc.dentalcore.domain.exception;

import com.lc.dentalcore.domain.constants.DomainConstants;

public class InvalidPhoneNumberException extends BadRequest{

    public InvalidPhoneNumberException() {
        super(DomainConstants.INVALID_PHONE_NUMBER);
    }
}
