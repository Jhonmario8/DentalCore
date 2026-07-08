package com.lc.dentalcore.infrastructure.input.controller;

import com.lc.dentalcore.application.dto.AppointmentDTO;
import com.lc.dentalcore.application.handler.IAppointmentHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final IAppointmentHandler appointmentHandler;

    @PostMapping()
    public AppointmentDTO createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        return appointmentHandler.createAppointment(appointmentDTO);
    }

}
