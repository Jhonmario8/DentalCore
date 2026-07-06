package com.lc.dentalcore.domain.exception;

import com.lc.dentalcore.domain.constants.DomainConstants;

public class InvalidCredentialsException extends UnauthorizedException{

    public InvalidCredentialsException() {
        super(DomainConstants.MSG_INVALID_CREDENTIALS);
    }
}
