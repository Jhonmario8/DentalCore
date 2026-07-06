package com.lc.dentalcore.domain.exception;

import com.lc.dentalcore.domain.constants.DomainConstants;

public class InvalidPassword extends BadRequest {
    public InvalidPassword() {
        super(DomainConstants.INVALID_PASSWORD);
    }
}
