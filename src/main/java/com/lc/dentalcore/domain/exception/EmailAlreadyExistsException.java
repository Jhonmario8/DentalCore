package com.lc.dentalcore.domain.exception;

import com.lc.dentalcore.domain.constants.DomainConstants;

public class EmailAlreadyExistsException extends ConflictException{
    public EmailAlreadyExistsException() {
        super(DomainConstants.MSG_EMAIL_ALREADY_EXISTS);
    }
}
