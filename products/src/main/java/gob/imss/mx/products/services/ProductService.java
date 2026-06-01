package gob.imss.mx.products.services;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import gob.imss.mx.products.dto.ExcelResponseDto;
import gob.imss.mx.products.dto.ProductRequestDto;
import gob.imss.mx.products.entities.Product;

/**
 * Servicio de negocio para operaciones sobre productos.
 *
 * Define las acciones que el API de productos expone al controlador:
 * creación y actualización de productos, búsqueda paginada y exportación a Excel.
 *
 * El `ProductService` centraliza la lógica de negocio entre los DTOs de entrada,
 * la entidad `Product` y los repositorios de persistencia.
 */
public interface ProductService {

    /**
     * Crea un nuevo producto a partir del DTO de solicitud.
     *
     * @param productRequestDto datos del producto a crear
     * @param userAuditor nombre del usuario que realiza la operación (auditoría)
     * @return producto persistido
     */
    Product createProduct(ProductRequestDto productRequestDto, String userAuditor);

    /**
     * Actualiza un producto existente con los datos del DTO de solicitud.
     *
     * @param productId identificador del producto a actualizar
     * @param productRequestDto datos actualizados del producto
     * @param userAuditor nombre del usuario que realiza la operación (auditoría)
     * @return producto actualizado
     */
    Product updateProduct(Long productId, ProductRequestDto productRequestDto, String userAuditor);

    /**
     * Busca productos con filtros opcionales y devuelve un resultado paginado.
     *
     * @param productName filtro parcial por nombre
     * @param productCode filtro parcial por código
     * @param priceMin precio mínimo opcional
     * @param priceMax precio máximo opcional
     * @param pageable información de paginación
     * @return página de productos que coinciden con los filtros
     */
    Page<Product> findProducts(String productName, String productCode, BigDecimal priceMin, BigDecimal priceMax, Pageable pageable);

    /**
     * Genera una exportación de productos a Excel según los filtros proporcionados.
     *
     * @param productName filtro parcial por nombre
     * @param productCode filtro parcial por código
     * @param priceMin precio mínimo opcional
     * @param priceMax precio máximo opcional
     * @return DTO con el contenido del archivo Excel codificado en Base64
     */
    ExcelResponseDto exportToExcel(String productName, String productCode, BigDecimal priceMin, BigDecimal priceMax);

}
