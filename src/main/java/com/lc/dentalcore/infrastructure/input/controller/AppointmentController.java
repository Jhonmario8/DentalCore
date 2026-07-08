package com.lc.dentalcore.infrastructure.input.controller;

import com.lc.dentalcore.application.dto.AppointmentDTO;
import com.lc.dentalcore.application.handler.IAppointmentHandler;
import com.lc.dentalcore.domain.model.AppointmentStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final IAppointmentHandler appointmentHandler;

    @PostMapping()
    public ResponseEntity<AppointmentDTO> createAppointment(@Valid @RequestBody AppointmentDTO appointmentDTO) {
        AppointmentDTO dto = appointmentHandler.createAppointment(appointmentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentDTO> updateStatus(@PathVariable Long id, @RequestParam AppointmentStatus status) {
        AppointmentDTO appointmentDTO = appointmentHandler.updateStatus(id, status);
        return ResponseEntity.ok(appointmentDTO);
    }

    @GetMapping()
    public ResponseEntity<List<AppointmentDTO>> findAllByDate(@RequestParam(required = false) LocalDate date) {
        List<AppointmentDTO> appointments = appointmentHandler.findAllByDate(date);
        return ResponseEntity.ok(appointments);
    }
}
