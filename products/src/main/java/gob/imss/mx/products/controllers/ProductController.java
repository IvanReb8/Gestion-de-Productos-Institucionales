package gob.imss.mx.products.controllers;

import java.math.BigDecimal;
import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gob.imss.mx.products.dto.ExcelResponseDto;
import gob.imss.mx.products.dto.ProductRequestDto;
import gob.imss.mx.products.entities.Product;
import gob.imss.mx.products.services.ProductService;
import jakarta.validation.Valid;

/**
 * Controlador REST de productos.
 *
 * Expone la API de productos bajo `/api/v1/products` e invoca el servicio de
 * negocio para operar con productos, búsquedas y exportación a Excel.
 *
 * Soporta:
 * - creación y actualización de productos.
 * - búsqueda paginada con filtros.
 * - exportación de resultados a Excel.
 */
@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    /**
     * POST /api/v1/products: 
     * Crea un nuevo producto. * El `@Valid` gatilla las validaciones de los DTOs.
     * 
     * @param productRequestDto Datos del producto a crear
     * @param principal recupera automáticamente el usuario autenticado por JWT para la auditoría
     * 
     * @return Producto creado
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(
            @Valid @RequestBody ProductRequestDto productRequestDto,
            Principal principal) {
        // Si la seguridad aún no está activa, usamos un usuario por defecto
        String userAuditor = (principal != null) ? principal.getName() : "WEB_SYSTEM";
        Product product = service.createProduct(productRequestDto, userAuditor);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    /**
     * PUT /api/v1/products/{id} 
     * Actualiza un producto existente por su ID técnico.
     * 
     * @param productId ID técnico del producto
     * @param productRequestDto Nuevos datos del producto
     * @param userAuditor Usuario que realiza la actualización
     * @param principal recupera automáticamente el usuario autenticado por JWT para la auditoría
     * 
     * @return Producto actualizado
     */
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductRequestDto productRequestDto,
            Principal principal) {
        // Si la seguridad aún no está activa, usamos un usuario por defecto
        String userAuditor = (principal != null) ? principal.getName() : "SISTEMA_WEB";
        Product product = service.updateProduct(productId, productRequestDto, userAuditor);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    /**
     * GET /api/v1/products
     * Busca productos con filtros opcionales y paginación
     * 
     * @param productName Nombre del producto (opcional)
     * @param productCode Código del producto (opcional)
     * @param priceMin Precio mínimo (opcional)
     * @param priceMax Precio máximo (opcional)
     * @param pageable Información de paginación
     * @return Pagina de productos que coinciden con los filtros
     */
    @GetMapping
    public ResponseEntity<Page<Product>> findProducts(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String productCode,
            @RequestParam(required = false) BigDecimal priceMin,
            @RequestParam(required = false) BigDecimal priceMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "technicalId") String sortBy) {

        // Configuramos la paginación y ordenamos de forma descendente por id técnico por defecto
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<Product> products = service.findProducts(productName, productCode, priceMin, priceMax, pageable);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * GET /api/v1/products/export
     * Exporta productos a un archivo Excel en Base64
     * 
     * @param productName Nombre del producto (opcional)
     * @param productCode Código del producto (opcional)
     * @param priceMin Precio mínimo (opcional)
     * @param priceMax Precio máximo (opcional)
     * @return ExcelResponseDto con el archivo Excel en Base64
     */
    @GetMapping("/export")
    public ResponseEntity<ExcelResponseDto> exportProductsToExcel(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String productCode,
            @RequestParam(required = false) BigDecimal priceMin,
            @RequestParam(required = false) BigDecimal priceMax) {

        ExcelResponseDto response = service.exportToExcel(productName, productCode, priceMin, priceMax);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
