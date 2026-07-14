package com.lc.dentalcore.domain.exception;

import com.lc.dentalcore.domain.constants.DomainConstants;

public class InvalidPeriodException extends BadRequest{

    public InvalidPeriodException() {
        super(DomainConstants.MSG_INVALID_PERIOD);
    }

}
