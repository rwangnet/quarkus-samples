package cl.rwangnet.api;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * Simple data model to simulate a Booking.
 * @author rwangnet <rwangnet@gmail.com>
 */
public class CreateBookingRequest {
    @NotBlank
    public String customerName;

    @Future
    public LocalDateTime datetime;
}