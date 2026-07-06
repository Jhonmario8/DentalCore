package com.lc.dentalcore.domain.exception;

import com.lc.dentalcore.domain.constants.DomainConstants;

public class PhoneNumberAlreadyExist extends ConflictException{

    public PhoneNumberAlreadyExist(){
        super(DomainConstants.MSG_PHONE_NUMBER_ALREADY_EXISTS);
    }

}
