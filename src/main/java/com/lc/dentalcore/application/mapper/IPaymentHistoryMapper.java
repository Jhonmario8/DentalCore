package com.lc.dentalcore.application.mapper;

import com.lc.dentalcore.application.dto.PaymentHistoryDTO;
import com.lc.dentalcore.domain.model.PaymentHistory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IPaymentHistoryMapper {


    PaymentHistoryDTO toDto(PaymentHistory paymentHistory);

}
