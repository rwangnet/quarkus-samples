package cl.rwangnet.resources;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.HashMap;
import java.util.Map;

@Path("/secured")
@RequestScoped
public class SecuredResource {

    @Inject
    JsonWebToken jwt;

    @Inject
    @Claim(standard = Claims.birthdate)
    String birthdate;

    @GET
    @Path("permit-all")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response hello(@Context SecurityContext ctx) {
        return buildJsonResponse("message", getResponseString(ctx));
    }

    @GET
    @Path("roles-allowed")
    @RolesAllowed({ "user", "admin" })
    @Produces(MediaType.APPLICATION_JSON)
    public Response helloRolesAllowed(@Context SecurityContext ctx) {
        String message = getResponseString(ctx);
        Object permissions = jwt.getClaim("permissions");
        Map<String, Object> result = new HashMap<>();
        result.put("message", message);
        result.put("permissions", permissions);
        return Response.ok(result).build();
    }

    @GET
    @Path("roles-allowed-admin")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response helloRolesAllowedAdmin(@Context SecurityContext ctx) {
        String message = getResponseString(ctx);
        Map<String, Object> result = new HashMap<>();
        result.put("message", message);
        result.put("birthdate", birthdate);
        return Response.ok(result).build();
    }

    private String getResponseString(SecurityContext ctx) {
        if (ctx.getUserPrincipal() == null) {
            throw new jakarta.ws.rs.NotAuthorizedException("User not authenticated");
        }

        String name = ctx.getUserPrincipal().getName();

        if (!name.equals(jwt.getName())) {
            throw new InternalServerErrorException("Principal and JWT name do not match");
        }
        return String.format("hello %s, isHttps: %s, authScheme: %s, hasJWT: %s",
                name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJwt());
    }

    private boolean hasJwt() {
        return jwt.getClaimNames() != null;
    }

    private Response buildJsonResponse(String key, Object value) {
        return Response.ok(Map.of(key, value)).build();
    }
}
