package cl.rwangnet;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class ReactiveGreetingResourceTest {

    @Test
    void testSaludoGetDefault() {
        given()
          .when().get("/saludo-reactivo")
          .then()
             .statusCode(200)
             .body("mensaje", is("¡Hola visitante!"));
    }

    @Test
    void testSaludoPostValido() {
        given()
          .contentType(ContentType.JSON)
          .body("""
                {
                  "name": "Carmen",
                  "age": 31,
                  "email": "carmen@example.com"
                }
              """)
          .when().post("/saludo-reactivo")
          .then()
             .statusCode(200)
             .body("mensaje", containsString("Carmen"));
    }

}
