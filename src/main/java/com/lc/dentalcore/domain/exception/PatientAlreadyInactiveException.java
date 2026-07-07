package com.lc.dentalcore.domain.exception;

import com.lc.dentalcore.domain.constants.DomainConstants;

public class PatientAlreadyInactiveException extends ConflictException{

    public PatientAlreadyInactiveException() {
        super(DomainConstants.PATIENT_ALREADY_INACTIVE);
    }
}
