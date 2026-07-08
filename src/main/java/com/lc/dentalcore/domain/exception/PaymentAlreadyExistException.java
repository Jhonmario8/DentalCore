package com.lc.dentalcore.domain.exception;

import com.lc.dentalcore.domain.constants.DomainConstants;

public class PaymentAlreadyExistException extends ConflictException{

    public PaymentAlreadyExistException() {
        super(DomainConstants.PAYMENT_ALREADY_EXIST);
    }
}
