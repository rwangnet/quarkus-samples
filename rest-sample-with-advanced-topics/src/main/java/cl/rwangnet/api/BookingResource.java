package cl.rwangnet.api;

import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

import cl.rwangnet.application.BookingService;
import cl.rwangnet.domain.Booking;

@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
/**
 * Defines REST Resources to access to different CRUD endpoints
 * @author rwangnet <rwangnet@gmail.com>
 */
public class BookingResource {

    @Inject
    BookingService service;

    private final Map<UUID, Booking> cache = new ConcurrentHashMap<>();
    private final RateLimiter rateLimiter = new RateLimiter(5);

    @GET
    public List<Booking> getAll() {
        return service.findAll();
    }

    @GET
    @Path("/{id}")
    @Retry(maxRetries = 2, delay = 200)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5)
    @Timeout(500)
    public Response getById(@PathParam("id") UUID id) {
        if (!rateLimiter.allowRequest()) {
            return Response.status(Response.Status.TOO_MANY_REQUESTS).build();
        }

        return Response
                .ok(cache.computeIfAbsent(id,
                        key -> service.findById(key).orElseThrow(() -> new NotFoundException("No encontrado"))))
                .build();
    }

    @POST
    @Blocking
    public Response create(@Valid CreateBookingRequest request) {
        Booking booking = service.create(request.customerName, request.datetime);
        cache.put(booking.getId(), booking);
        return Response.status(Response.Status.CREATED).entity(booking).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Booking updateBooking(@PathParam("id") UUID id, Booking updatedBooking) {
        return service.update(id, updatedBooking);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBooking(@PathParam("id") UUID id) {
        service.delete(id);
        return Response.noContent().build();
    }

}