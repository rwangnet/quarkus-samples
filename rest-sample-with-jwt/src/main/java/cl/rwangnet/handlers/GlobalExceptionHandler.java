package cl.rwangnet.handlers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionHandler.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        Map<String, Object> error = new HashMap<>();
        int status = 500;

        if (exception instanceof NotAuthorizedException) {
            status = 401;
            error.put("error", "Not authorized");
        } else if (exception instanceof ForbiddenException) {
            status = 403;
            error.put("error", "Access forbidden");
        } else if (exception instanceof NotFoundException) {
            status = 404;
            error.put("error", "Resource not found");
        } else if (exception instanceof BadRequestException) {
            status = 400;
            error.put("error", "Bad request");
        } else if (exception instanceof InternalServerErrorException) {
            status = 500;
            error.put("error", "Internal server error");
        } else if (exception instanceof WebApplicationException) {
            status = ((WebApplicationException) exception).getResponse().getStatus();
            error.put("error", exception.getMessage());
        } else {
            error.put("error", "Unexpected error");
        }

        error.put("exception", exception.getClass().getSimpleName());
        error.put("message", exception.getMessage());

        // Log stacktrace si es 5xx
        if (status >= 500) {
            LOGGER.log(Level.SEVERE, "Unhandled exception", exception);
        }

        return Response.status(status)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
