package com.lc.dentalcore.infrastructure.config;

import com.lc.dentalcore.domain.api.*;
import com.lc.dentalcore.domain.spi.*;
import com.lc.dentalcore.domain.usecase.AppointmentService;
import com.lc.dentalcore.domain.usecase.PatientService;
import com.lc.dentalcore.domain.usecase.PaymentService;
import com.lc.dentalcore.domain.usecase.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AllArgsConstructor
public class BeanConfiguration {

    private final IUserPersistencePort userPersistencePort;
    private final ITokenServicePort tokenServicePort;
    private final IPatientPersistencePort patientPersistencePort;
    private final IAppointmentPersistencePort appointmentPersistencePort;
    private final IPaymentPersistencePort paymentPersistencePort;
    private final IPaymentTransactionPersistencePort paymentTransactionPersistencePort;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public IUserServicePort userServicePort(IPasswordServicePort passwordServicePort){
        return new UserService(userPersistencePort, passwordServicePort, tokenServicePort);
    }

    @Bean
    public IPatientServicePort patientServicePort(){
        return new PatientService(patientPersistencePort);
    }

    @Bean
    public IAppointmentServicePort appointmentServicePort(){
        return new AppointmentService(appointmentPersistencePort, patientPersistencePort);
    }

    @Bean
    public IPaymentServicePort paymentServicePort(){
        return new PaymentService(paymentPersistencePort, appointmentPersistencePort, patientPersistencePort, paymentTransactionPersistencePort);
    }
}
