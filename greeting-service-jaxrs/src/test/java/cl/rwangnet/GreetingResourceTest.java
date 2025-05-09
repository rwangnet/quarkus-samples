package cl.rwangnet;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class GreetingResourceTest {

     @Test
    void testSaludoGetDefault() {
        given()
          .when().get("/saludo")
          .then()
             .statusCode(200)
             .body("mensaje", is("¡Hola visitante!"));
    }

    @Test
    void testSaludoGetWithName() {
        given()
          .queryParam("name", "Ana")
          .when().get("/saludo")
          .then()
             .statusCode(200)
             .body("mensaje", is("¡Hola Ana!"));
    }

    @Test
    void testSaludoPostValido() {
        given()
          .contentType(ContentType.JSON)
          .body("""
                {
                  "name": "Luis",
                  "age": 25,
                  "email": "luis@example.com"
                }
              """)
          .when().post("/saludo")
          .then()
             .statusCode(200)
             .body("mensaje", containsString("Luis"));
    }

    @Test
    void testSaludoPostInvalido() {
        given()
          .contentType(ContentType.JSON)
          .body("""
                {
                  "name": "",
                  "age": 0,
                  "email": "no-es-correo"
                }
              """)
          .when().post("/saludo")
          .then()
             .statusCode(400)
             .body("errores", hasSize(4))
             .body("errores.mensaje", hasItems(
                 "El nombre debe tener entre 2 y 30 caracteres",
                 "El nombre no puede estar vacío",
                 "El correo debe tener un formato válido"
             ));
    }

}
