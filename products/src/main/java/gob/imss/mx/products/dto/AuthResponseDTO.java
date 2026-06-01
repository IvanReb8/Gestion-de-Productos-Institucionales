package gob.imss.mx.products.dto;

import lombok.Data;

/**
 * DTO devuelto tras autenticación exitosa.
 *
 * - `token`: JWT emitido por el servidor.
 * - `type`: tipo de esquema (por defecto "Bearer").
 */
@Data
public class AuthResponseDTO {

    private String token;
    private String type = "Bearer";

    public AuthResponseDTO(String token) {
        this.token = token;
    } 

}
