package gob.imss.mx.products.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO para la petición de autenticación (login).
 *
 * Contiene las credenciales requeridas por `AuthController` para
 * autenticar al usuario y emitir un JWT.
 */
@Data
public class AuthRequestDTO {

    @NotBlank(message = "El usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

}
