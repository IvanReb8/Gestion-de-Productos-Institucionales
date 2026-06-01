package gob.imss.mx.products.dto;

import lombok.Data;

/**
 * DTO para respuestas que contienen archivos Excel codificados en Base64.
 *
 * Campos:
 * - `status`: código de estado interno del proceso de generación.
 * - `message`: mensaje de éxito o error.
 * - `fileName`: nombre del archivo generado.
 * - `fileBase64`: contenido del archivo en Base64 listo para descarga.
 */
@Data
public class ExcelResponseDto {

    private int status;
    private String message;
    private String fileName;
    private String fileBase64;

    public ExcelResponseDto(int status, String message, 
        String fileName, String fileBase64) {
        this.status = status;
        this.message = message;
        this.fileName = fileName;
        this.fileBase64 = fileBase64;
    }

}
