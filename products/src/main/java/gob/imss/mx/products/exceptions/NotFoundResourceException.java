package gob.imss.mx.products.exceptions;

/**
 * Excepción lanzada cuando un recurso solicitado no existe.
 *
 * Se mapea a un `404 Not Found` en `GlobalExceptionHandler`.
 */
public class NotFoundResourceException extends RuntimeException {
    public NotFoundResourceException(String message) {
        super(message);
    }

}
