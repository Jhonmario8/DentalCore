package com.lc.dentalcore.application.mapper;

import com.lc.dentalcore.application.dto.PaymentDTO;
import com.lc.dentalcore.application.dto.PaymentResponseDTO;
import com.lc.dentalcore.domain.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IPaymentMapper {

    Payment toDomain(PaymentDTO payment);

    PaymentResponseDTO toDto(Payment payment);

}
