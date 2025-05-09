package cl.rwangnet.resource;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

import cl.rwangnet.model.GreetingModel;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;

@GraphQLApi
@ApplicationScoped
public class GreetingGraphQLResource {

    @Query
    public Uni<String> greet(@Valid GreetingModel input) {
        String message = String.format(
            "¡Hola %s! Tienes %d años. Te escribiremos a %s.",
            input.name, input.age, input.email
        );
        return Uni.createFrom().item(message);
    }
}
