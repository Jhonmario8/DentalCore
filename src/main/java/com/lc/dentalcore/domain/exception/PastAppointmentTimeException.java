package com.lc.dentalcore.domain.exception;

import com.lc.dentalcore.domain.constants.DomainConstants;

public class PastAppointmentTimeException extends BadRequest{

    public PastAppointmentTimeException(){
        super(DomainConstants.PAST_APPOINTMENT_TIME);
    }
}
