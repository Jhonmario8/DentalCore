package com.lc.dentalcore.domain.constants;

public class DomainConstants {
    public static final String KEY_SUBJECT = "sub";
    public static final String KEY_USER_ID = "user_id";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String INVALID_EMAIL_ADDRESS = "Invalid email address";
    public static final String INVALID_PASSWORD = "Invalid password";
    public static final String MSG_INVALID_CREDENTIALS = "Invalid credentials";
    public static final String MSG_PASSWORD_CANNOT_BE_NULL_OR_EMPTY = "Password cannot be null or empty.";
    public static final String INVALID_PHONE_NUMBER = "Invalid phone number";
    public static final String INVALID_IDENTIFICATION_NUMBER = "Invalid identification number";
    public static final String MSG_IDENTIFICATION_NUMBER_ALREADY_EXISTS = "Identification number already exists";
    public static final String MSG_PHONE_NUMBER_ALREADY_EXISTS = "Phone number already exists";
    public static final String PATIENT_NOT_FOUND = "Patient not found";
    public static final String PATIENT_ALREADY_INACTIVE = "Patient is already inactive";
    public static final String PAST_APPOINTMENT_TIME = "Appointment time cannot be in the past";
    public static final String DUPLICATE_APPOINTMENT = "Duplicate appointment at the same time";
    public static final String APPOINTMENT_NOT_FOUND = "Appointment not found";
    public static final String MSG_STATUS_NULL = "Status cannot be null";
    public static final String MSG_ONLY_CONFIRMED_CAN_BE_ATTENDED = "Only confirmed appointments can be marked as attended";
    public static final String MSG_CANNOT_GO_BACK_TO_PENDING = "Cannot change status from confirmed back to pending";
    public static final String MSG_CANNOT_UPDATE_CANCELLED = "Cannot update a cancelled appointment";
    public static final String MSG_CANNOT_UPDATE_ATTENDED = "Cannot update an attended appointment";
    public static final String FUTURE_APPOINTMENT = "Cannot mark an appointment as attended if it is scheduled for a future date";
    public static final String MSG_INVALID_PAYMENT_AMOUNT = "Payment amount must be greater than or equal to 0 and less than or equal to the treatment cost";
    public static final String PAYMENT_ALREADY_EXIST = "Payment already exists for this appointment";
    public static final String PAYMENT_NOT_FOUND = "Payment not found";
    public static final String MSG_USERNAME_ALREADY_EXISTS = "Username already exists";
    public static final String MSG_INVALID_PERIOD = "Invalid period. Please provide a valid period (DAILY, WEEKLY, MONTHLY, YEARLY) and a date in the format YYYY-MM-DD.";
}
