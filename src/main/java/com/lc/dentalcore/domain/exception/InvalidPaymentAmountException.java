package com.lc.dentalcore.domain.exception;

import com.lc.dentalcore.domain.constants.DomainConstants;

public class InvalidPaymentAmountException extends BadRequest{

    public InvalidPaymentAmountException() {
        super(DomainConstants.MSG_INVALID_PAYMENT_AMOUNT);
    }

}
