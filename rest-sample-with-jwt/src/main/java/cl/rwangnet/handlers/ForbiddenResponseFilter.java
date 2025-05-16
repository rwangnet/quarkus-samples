package cl.rwangnet.handlers;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import java.io.IOException;

@Provider
@Priority(Priorities.AUTHORIZATION)
/**
 * Works to handle 403 when there is a forbidden access token in play.
 */
public class ForbiddenResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {

        if (responseContext.getStatus() == 403) {
            responseContext.setEntity("{\"error\":\"Forbidden\",\"message\":\"Access denied\"}");
            responseContext.getHeaders().putSingle("Content-Type", "application/json");
        }
    }
}