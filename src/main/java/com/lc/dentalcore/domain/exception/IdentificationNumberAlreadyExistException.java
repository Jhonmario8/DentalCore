package com.lc.dentalcore.domain.exception;

import com.lc.dentalcore.domain.constants.DomainConstants;

public class IdentificationNumberAlreadyExistException extends ConflictException{

    public IdentificationNumberAlreadyExistException() {
        super(DomainConstants.MSG_IDENTIFICATION_NUMBER_ALREADY_EXISTS);
    }
}
