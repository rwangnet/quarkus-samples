package cl.rwangnet.domain;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Simulates the <Booking> Entity, but currently implemented as a simple POJO.
 * 
 * @author rwangnet <rwangnet@gmail.com>
 */
public class Booking {
    private final UUID id;
    private final String customerName;
    private final LocalDateTime datetime;

    public Booking(UUID id, String customerName, LocalDateTime datetime) {
        this.id = id;
        this.customerName = customerName;
        this.datetime = datetime;
    }

    public UUID getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }
}