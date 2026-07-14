package com.lc.dentalcore.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EarningsResponse {

    private BigDecimal totalCollected;

    private LocalDate startDate;

    private LocalDate endDate;

}

