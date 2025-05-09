package cl.rwangnet.model;

import org.eclipse.microprofile.graphql.Input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Input
public class GreetingModel {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 30, message = "El nombre debe tener entre 2 y 30 caracteres")
    public String name;

    @Min(value = 1, message = "La edad debe ser mayor que 0")
    @Max(value = 120, message = "La edad debe ser razonable (hasta 120)")
    public int age;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato válido")
    public String email;
}