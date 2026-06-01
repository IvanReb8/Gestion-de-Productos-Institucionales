package gob.imss.mx.products.exceptions;

import java.time.LocalDateTime;

/**
 * DTO de respuesta de error usado por el manejador global de excepciones.
 *
 * Contiene un timestamp, un código de estado HTTP y un mensaje descriptivo.
 */
public record ErrorResponse(
    LocalDateTime timestamp, 
    int status, 
    String message
) {}
