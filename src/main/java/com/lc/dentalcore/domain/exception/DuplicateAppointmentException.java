package com.lc.dentalcore.domain.exception;

import com.lc.dentalcore.domain.constants.DomainConstants;

public class DuplicateAppointmentException extends ConflictException{

    public DuplicateAppointmentException() {
        super(DomainConstants.DUPLICATE_APPOINTMENT);
    }
}
