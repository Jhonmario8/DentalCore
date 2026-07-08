package com.lc.dentalcore.infrastructure.output.jpa.adapter;

import com.lc.dentalcore.domain.model.Appointment;
import com.lc.dentalcore.domain.spi.IAppointmentPersistencePort;
import com.lc.dentalcore.infrastructure.output.jpa.mapper.IAppointmentEntityMapper;
import com.lc.dentalcore.infrastructure.output.jpa.repository.IAppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentJpaAdapter implements IAppointmentPersistencePort {

    private final IAppointmentEntityMapper mapper;
    private final IAppointmentRepository repository;

    @Override
    public Appointment saveAppointment(Appointment appointment) {
        return mapper.toDomain(repository.save(mapper.toEntity(appointment)));
    }

    @Override
    public List<Appointment> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Appointment> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Appointment> findAllByDate(LocalDate date) {
        return repository.findAllByAppointmentDateOrderByAppointmentTimeAsc(date)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
