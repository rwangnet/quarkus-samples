package cl.rwangnet.infrastructure;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import cl.rwangnet.domain.Booking;
import cl.rwangnet.domain.port.BookingRepository;

@ApplicationScoped
public class InMemoryBookingRepository implements BookingRepository {

    private final Map<UUID, Booking> storage = new ConcurrentHashMap<>();

    @Override
    public void save(Booking booking) {
        storage.put(booking.getId(), booking);
    }

    @Override
    public Optional<Booking> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Booking> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void update(Booking booking) {
        storage.put(booking.getId(), booking);
    }

    @Override
    public void delete(UUID id) {
        storage.remove(id);
    }

}