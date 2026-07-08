package com.lc.dentalcore.infrastructure.output.jpa.repository;

import com.lc.dentalcore.infrastructure.output.jpa.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPaymentRepository extends JpaRepository<PaymentEntity, Long> {

    Optional<PaymentEntity> findByAppointmentId(Long appointmentId);
    List<PaymentEntity> findAllByPatientIdOrderByPaymentDateDesc(Long patientId);

}
