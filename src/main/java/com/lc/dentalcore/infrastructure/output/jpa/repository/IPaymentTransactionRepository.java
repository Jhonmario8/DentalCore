package com.lc.dentalcore.infrastructure.output.jpa.repository;

import com.lc.dentalcore.infrastructure.output.jpa.entity.PaymentTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IPaymentTransactionRepository extends JpaRepository<PaymentTransactionEntity, Long > {

    List<PaymentTransactionEntity> findAllByTransactionDateGreaterThanEqualAndTransactionDateLessThan(LocalDateTime startDate, LocalDateTime endDate);

}
