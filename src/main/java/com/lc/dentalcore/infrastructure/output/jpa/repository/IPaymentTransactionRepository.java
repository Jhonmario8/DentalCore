package com.lc.dentalcore.infrastructure.output.jpa.repository;

import com.lc.dentalcore.infrastructure.output.jpa.entity.PaymentTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IPaymentTransactionRepository extends JpaRepository<PaymentTransactionEntity, Long > {

    List<PaymentTransactionEntity> findAllByTransactionDateGreaterThanEqualAndTransactionDateLessThan(LocalDateTime startDate, LocalDateTime endDate);
    List<PaymentTransactionEntity> findAllByPaymentId(Long paymentId);
    @Query("""
    SELECT COALESCE(SUM(pt.amount), 0)
    FROM PaymentTransactionEntity pt
    WHERE pt.transactionDate >= :startDate
      AND pt.transactionDate < :endDate
""")
    BigDecimal sumAmountByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
