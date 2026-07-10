package com.lc.dentalcore.domain.exception;

import com.lc.dentalcore.domain.constants.DomainConstants;

public class UsernameAlreadyExistsException extends ConflictException{
    public UsernameAlreadyExistsException() {
        super(DomainConstants.MSG_USERNAME_ALREADY_EXISTS);
    }
}
