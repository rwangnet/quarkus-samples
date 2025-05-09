package cl.rwangnet.handler;

import java.util.List;
import java.util.Map;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<Map<String, String>> errores = exception.getConstraintViolations()
            .stream()
            .map(v -> Map.of(
                "campo", v.getPropertyPath().toString(),
                "mensaje", v.getMessage()
            ))
            .toList();

        return Response.status(Response.Status.BAD_REQUEST)
            .entity(Map.of("errores", errores))
            .build();
    }

}
