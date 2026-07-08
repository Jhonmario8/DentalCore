package com.lc.dentalcore.domain.exception;

import com.lc.dentalcore.domain.constants.DomainConstants;

public class FutureAppointmentException extends ConflictException{

    public FutureAppointmentException(){
        super(DomainConstants.FUTURE_APPOINTMENT);
    }

}
