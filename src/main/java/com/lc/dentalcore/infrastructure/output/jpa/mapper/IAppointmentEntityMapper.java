package com.lc.dentalcore.infrastructure.output.jpa.mapper;

import com.lc.dentalcore.domain.model.Appointment;
import com.lc.dentalcore.infrastructure.output.jpa.entity.AppointmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IAppointmentEntityMapper {

    AppointmentEntity toEntity(Appointment appointment);

    Appointment toDomain(AppointmentEntity appointmentEntity);

}
