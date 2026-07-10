package com.lc.dentalcore.infrastructure.output.jpa.repository;

import com.lc.dentalcore.infrastructure.output.jpa.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface IAppointmentRepository extends JpaRepository<AppointmentEntity, Long> {

    List<AppointmentEntity> findAllByAppointmentDateOrderByAppointmentTimeAsc(LocalDate date);
    boolean existsByAppointmentDateAndAppointmentTime(LocalDate appointmentDate, LocalTime appointmentTime);
}
