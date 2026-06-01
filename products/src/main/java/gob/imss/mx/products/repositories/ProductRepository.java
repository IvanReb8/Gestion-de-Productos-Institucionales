package gob.imss.mx.products.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gob.imss.mx.products.entities.Product;

/**
 * Repositorio JPA para la entidad `Product`.
 *
 * Provee acceso a la persistencia de productos y define consultas
 * personalizadas para búsquedas con filtros de nombre, código y rango de precio.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Búsqueda paginada de productos con filtros opcionales.
     *
     * @param name filtro parcial por nombre de producto
     * @param code filtro parcial por código de producto
     * @param priceMin filtro mínimo de precio
     * @param priceMax filtro máximo de precio
     * @param pageable datos de paginación
     * @return página de productos que coinciden con los filtros
     */
    @Query("SELECT p FROM Product p WHERE " +
           "(:name IS NULL OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:code IS NULL OR UPPER(p.productCode) LIKE UPPER(CONCAT('%', :code, '%'))) AND " +
           "(:priceMin IS NULL OR p.price >= :priceMin) AND " +
           "(:priceMax IS NULL OR p.price <= :priceMax)")
    Page<Product> searchWithFilters(
            @Param("name") String name,
            @Param("code") String code,
            @Param("priceMin") BigDecimal priceMin,
            @Param("priceMax") BigDecimal priceMax,
            Pageable pageable);

    /**
     * Búsqueda no paginada usada para exportar resultados a Excel.
     *
     * Mantiene la misma lógica de filtros de búsqueda, pero devuelve la lista
     * completa de productos en lugar de una página.
     */
    @Query("SELECT p FROM Product p WHERE " +
           "(:name IS NULL OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:code IS NULL OR UPPER(p.productCode) LIKE UPPER(CONCAT('%', :code, '%'))) AND " +
           "(:priceMin IS NULL OR p.price >= :priceMin) AND " +
           "(:priceMax IS NULL OR p.price <= :priceMax)")
    List<Product> searchForExcel(
            @Param("name") String name,
            @Param("code") String code,
            @Param("priceMin") BigDecimal priceMin,
            @Param("priceMax") BigDecimal priceMax);

}
