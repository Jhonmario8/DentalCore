package com.lc.dentalcore.domain.exception;

import com.lc.dentalcore.domain.constants.DomainConstants;

public class PaymentNotFoundException extends NotFoundException{

    public PaymentNotFoundException() {
        super(DomainConstants.PAYMENT_NOT_FOUND);
    }

}
