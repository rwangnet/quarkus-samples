package cl.rwangnet.resource;

import java.util.Map;

import cl.rwangnet.model.GreetingModel;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/saludo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GreetingResource {

     @GET
    public Response getSaludo(@QueryParam("name") String name) {
        String saludo = (name == null || name.isBlank())
            ? "¡Hola visitante!"
            : "¡Hola " + name + "!";
        return Response.ok(Map.of("mensaje", saludo)).build();
    }

    @POST
    public Response postSaludo(@Valid GreetingModel request) {
        String mensaje = String.format("¡Hola %s! Tienes %d años. Te escribiremos a %s.",
                request.name, request.age, request.email);
        return Response.ok(Map.of("mensaje", mensaje)).build();
    }

}
