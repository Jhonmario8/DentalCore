package com.lc.dentalcore.domain.exception;

import com.lc.dentalcore.domain.constants.DomainConstants;

public class InvalidIdentificationNumberException extends BadRequest{

    public InvalidIdentificationNumberException() {
        super(DomainConstants.INVALID_IDENTIFICATION_NUMBER);
    }

}
