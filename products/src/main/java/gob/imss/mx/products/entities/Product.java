package gob.imss.mx.products.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "products", indexes = { 
    @Index(name = "idx_prod_search", columnList = "product_name, product_code, price") 
})
@Data
/**
 * Entidad `Product` usada por el servicio `products`.
 *
 * Descripción general:
 * - Representa un producto del catálogo con identificador técnico y negocio,
 *   código, nombre, precio y metadatos de auditoría.
 * - Está mapeada a la tabla `products` y tiene un índice compuesto
 *   (`product_name`, `product_code`, `price`) optimizado para búsquedas.
 *
 * Comportamiento importante:
 * - `technicalId`: clave primaria autogenerada (IDENTITY).
 * - `bussinesId`: identificador de negocio único (por ejemplo SKU externo).
 * - `registrationDate` se establece automáticamente antes de persistir
 *   (método `onCreate()` marcado con `@PrePersist`).
 * - `validityIndicator` permite marcar registros activos/inactivos sin
 *   eliminarlos físicamente (soft-delete/conveniencia de negocio).
 */
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "technical_id")
    private Long technicalId;

    @Column(name = "business_id", unique = true, nullable = false, length = 50)
    private String bussinesId;

    @Column(name = "product_code", nullable = false, length = 10)
    private String productCode;

    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "validity_indicator", nullable = false)
    private Boolean validityIndicator;

    @Column(name = "registration_date", nullable = false, updatable = false)
    private LocalDateTime registrationDate;

    @Column(name = "user_auditor", nullable = false, length = 100)
    private String userAuditor;

    @PrePersist
    protected void onCreate() {
        // Establece la fecha de registro en el momento de la primera inserción.
        // No es updatable para mantener inmutabilidad de la fecha de creación.
        this.registrationDate = LocalDateTime.now();
    }

}
