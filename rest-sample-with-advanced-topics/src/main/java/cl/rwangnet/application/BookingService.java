package cl.rwangnet.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import cl.rwangnet.domain.Booking;
import cl.rwangnet.domain.port.BookingRepository;

@ApplicationScoped
public class BookingService {

    @Inject
    BookingRepository repository;

    public Booking create(String customerName, LocalDateTime datetime) {
        Booking booking = new Booking(UUID.randomUUID(), customerName, datetime);
        repository.save(booking);
        return booking;
    }

    public Optional<Booking> findById(UUID id) {
        return repository.findById(id);
    }

    public List<Booking> findAll() {
        return repository.findAll();
    }

    public Booking update(UUID id, Booking updatedBooking) {
        Optional<Booking> existing = repository.findById(id);
        if (existing.isEmpty()) {
            throw new NotFoundException("Booking not found");
        }
        Booking toUpdate = new Booking(id, updatedBooking.getCustomerName(), updatedBooking.getDatetime());
        repository.update(toUpdate);
        return toUpdate;
    }

    public void delete(UUID id) {
        Optional<Booking> existing = repository.findById(id);
        if (existing.isEmpty()) {
            throw new NotFoundException("Booking not found");
        }
        repository.delete(id);
    }

}