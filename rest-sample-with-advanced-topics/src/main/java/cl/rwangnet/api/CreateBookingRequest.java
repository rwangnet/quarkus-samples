package cl.rwangnet.api;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class CreateBookingRequest {
    @NotBlank
    public String customerName;

    @Future
    public LocalDateTime datetime;
}