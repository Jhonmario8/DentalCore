package com.lc.dentalcore.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DashboardSummary {

   private Long totalAppointmentsToday;
   private Map<AppointmentStatus, Long> appointmentsByStatus;
   private BigDecimal totalCollectedToday;

}
