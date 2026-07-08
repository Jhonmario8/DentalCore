package com.lc.dentalcore.domain.exception;

import com.lc.dentalcore.domain.constants.DomainConstants;

public class AppointmentNotFoundException extends NotFoundException{

    public AppointmentNotFoundException() {
        super(DomainConstants.APPOINTMENT_NOT_FOUND);
    }
}
