package gob.imss.mx.products.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO de entrada para crear/actualizar productos.
 *
 * Campos y validaciones:
 * - `productCode`: código corto del producto (obligatorio, máximo 10 caracteres).
 * - `productName`: nombre descriptivo (obligatorio, máximo 200 caracteres).
 * - `price`: precio del producto (obligatorio, no negativo).
 * - `validityIndicator`: indicador de vigencia (obligatorio).
 *
 * Este DTO usa anotaciones de validación `jakarta.validation` que son
 * procesadas por los controladores para validar la entrada antes de
 * mapear a la entidad `Product`.
 */
@Data
public class ProductRequestDto {

    @NotBlank(message = "The product code is required")
    @Size(max = 10, message = "The password cannot exceed 10 characters")
    private String productCode;

    @NotBlank(message = "The product name is required")
    @Size(max = 200, message = "The name cannot exceed 200 characters")
    private String productName;

    @NotNull(message = "The price is mandatory")
    @DecimalMin(value = "0.0", inclusive = true, 
                message = "The price cannot be negative.")
    private BigDecimal price;

    @NotNull(message = "The validity indicator is mandatory")
    private Boolean validityIndicator;

}
