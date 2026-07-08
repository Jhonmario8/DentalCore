package com.lc.dentalcore.application.mapper;

import com.lc.dentalcore.application.dto.AppointmentDTO;
import com.lc.dentalcore.domain.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IAppointmentMapper {

    AppointmentDTO toDto(Appointment appointment);

    Appointment toDomain(AppointmentDTO appointmentDTO);
}
