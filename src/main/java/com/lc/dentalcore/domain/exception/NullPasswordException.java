package com.lc.dentalcore.domain.exception;

import com.lc.dentalcore.domain.constants.DomainConstants;

public class NullPasswordException extends BadRequest{
    public NullPasswordException() {
        super(DomainConstants.MSG_PASSWORD_CANNOT_BE_NULL_OR_EMPTY);
    }
}
