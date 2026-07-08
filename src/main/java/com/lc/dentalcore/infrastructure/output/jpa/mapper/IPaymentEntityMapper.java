package com.lc.dentalcore.infrastructure.output.jpa.mapper;

import com.lc.dentalcore.domain.model.Payment;
import com.lc.dentalcore.infrastructure.output.jpa.entity.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IPaymentEntityMapper {

    PaymentEntity toEntity(Payment payment);

    Payment toDomain(PaymentEntity paymentEntity);

}
