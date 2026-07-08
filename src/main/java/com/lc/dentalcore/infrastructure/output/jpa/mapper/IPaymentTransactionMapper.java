package com.lc.dentalcore.infrastructure.output.jpa.mapper;

import com.lc.dentalcore.domain.model.PaymentTransaction;
import com.lc.dentalcore.infrastructure.output.jpa.entity.PaymentTransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IPaymentTransactionMapper {

    PaymentTransactionEntity toEntity(PaymentTransaction paymentTransaction);

    PaymentTransaction toDomain(PaymentTransactionEntity paymentTransactionEntity);

}
