package cl.rwangnet.handlers;

import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.security.UnauthorizedException;
import io.vertx.ext.web.Router;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.ws.rs.ForbiddenException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@ApplicationScoped
public class JwtErrorHandler {

    private static final class ResponseInfo {
        final int status;
        final String error;
        final String message;

        ResponseInfo(int status, String error, String message) {
            this.status = status;
            this.error = error;
            this.message = message;
        }

        String toJson() {
            return String.format("{\"error\":\"%s\",\"message\":\"%s\"}", error, message);
        }
    }

    private final Map<Class<?>, Function<Throwable, ResponseInfo>> exceptionHandlers = new HashMap<>();

    public JwtErrorHandler() {
        exceptionHandlers.put(AuthenticationFailedException.class,
                ex -> new ResponseInfo(401, "Unauthorized", "Invalid or expired JWT token"));

        exceptionHandlers.put(UnauthorizedException.class, ex -> new ResponseInfo(403, "Forbidden", "Access denied"));

        exceptionHandlers.put(ForbiddenException.class,
                ex -> new ResponseInfo(403, "Forbidden", "You don't have permission to access this resource"));
                
    }

    public void init(@Observes Router router) {
        
        router.route().failureHandler(ctx -> {
            Throwable failure = ctx.failure();
            ResponseInfo info = resolveResponse(failure);

            ctx.response()
                    .setStatusCode(info.status)
                    .putHeader("Content-Type", "application/json")
                    .end(info.toJson());
        });
    }

    private ResponseInfo resolveResponse(Throwable failure) {
        if (failure == null) {
            return new ResponseInfo(500, "Internal Server Error", "Unknown error");
        }

        // Match the specific exception class or its superclasses
        for (Map.Entry<Class<?>, Function<Throwable, ResponseInfo>> entry : exceptionHandlers.entrySet()) {
            if (entry.getKey().isAssignableFrom(failure.getClass())) {
                return entry.getValue().apply(failure);
            }
        }

        // Default fallback
        return new ResponseInfo(500, "Internal Server Error",
                failure.getMessage() != null ? failure.getMessage() : "Unexpected error");
    }
}