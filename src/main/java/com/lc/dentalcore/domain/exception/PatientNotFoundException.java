package com.lc.dentalcore.domain.exception;

import com.lc.dentalcore.domain.constants.DomainConstants;

public class PatientNotFoundException extends NotFoundException{

    public PatientNotFoundException(){
        super(DomainConstants.PATIENT_NOT_FOUND);
    }

}
