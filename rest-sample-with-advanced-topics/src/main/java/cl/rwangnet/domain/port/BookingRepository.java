package cl.rwangnet.domain.port;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import cl.rwangnet.domain.Booking;

public interface BookingRepository {
    void save(Booking booking);

    void update(Booking booking);

    void delete(UUID id);

    Optional<Booking> findById(UUID id);

    List<Booking> findAll();
}