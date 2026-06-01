package gob.imss.mx.products.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gob.imss.mx.products.dto.ExcelResponseDto;
import gob.imss.mx.products.dto.ProductRequestDto;
import gob.imss.mx.products.entities.Product;
import gob.imss.mx.products.exceptions.NotFoundResourceException;
import gob.imss.mx.products.repositories.ProductRepository;

/**
 * Implementación del servicio de negocio para productos.
 *
 * Este servicio se encarga de:
 * - Crear y actualizar productos.
 * - Buscar productos con filtros paginados.
 * - Generar un reporte Excel codificado en Base64.
 *
 * La clase centraliza la lógica de persistencia, conversión de DTOs y reglas de
 * negocio específicas como la generación de IDs de negocio.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    /**
     * Crea un producto nuevo en la base de datos.
     *
     * Asigna un `businessId` único con el formato `PROD-AAAAMMDD-HHMMSS` y
     * marca el usuario auditor que realiza la operación.
     */
    public Product createProduct(ProductRequestDto productRequestDto, String userAuditor) {
        Product product = new Product();
        product.setBussinesId(generateBusinessId());
        product.setProductCode(productRequestDto.getProductCode());
        product.setProductName(productRequestDto.getProductName());
        product.setPrice(productRequestDto.getPrice());
        product.setValidityIndicator(productRequestDto.getValidityIndicator());
        product.setUserAuditor(userAuditor);
        return repository.save(product);
    }

    @Override
    @Transactional
    /**
     * Actualiza un producto existente.
     *
     * Utiliza transacción para asegurar que la entidad se mantenga consistente
     * al aplicar los cambios y guardar el resultado.
     */
    public Product updateProduct(Long productId, ProductRequestDto productRequestDto, String userAuditor) {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new NotFoundResourceException("Product not found with technical ID: " + productId));
        
        product.setProductCode(productRequestDto.getProductCode());
        product.setProductName(productRequestDto.getProductName());
        product.setPrice(productRequestDto.getPrice());
        product.setValidityIndicator(productRequestDto.getValidityIndicator());
        product.setUserAuditor(userAuditor); // Se registra quién modificó
        return repository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    /**
     * Busca productos con filtros opcionales y devuelve resultados paginados.
     *
     * El repositorio aplica filtros de nombre, código y rango de precio.
     */
    public Page<Product> findProducts(String productName, String productCode, BigDecimal priceMin, BigDecimal priceMax,
            Pageable pageable) {
        return repository.searchWithFilters(productName, productCode, priceMin, priceMax, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    /**
     * Genera un archivo Excel con los productos que coinciden con los filtros.
     *
     * Construye el libro de Excel en memoria, formatea los encabezados y filas,
     * luego codifica el resultado en Base64 para enviarlo en el DTO de respuesta.
     */
    public ExcelResponseDto exportToExcel(String productName, String productCode, BigDecimal priceMin,
            BigDecimal priceMax) {
        List<Product> productslist = repository.searchForExcel(productName, productCode, priceMin, priceMax);
        
        try (Workbook workbook = new XSSFWorkbook(); 
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("Product catalog");
            
            // Estilo para el encabezado
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            
            // Fila de Encabezados
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID Técnico", "ID Negocio", "Clave", "Nombre del Producto", "Precio", "Estado", "Fecha de Registro"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Llenado de Datos
            int rowNum = 1;
            for (Product product : productslist) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(product.getTechnicalId());
                row.createCell(1).setCellValue(product.getBussinesId());
                row.createCell(2).setCellValue(product.getProductCode());
                row.createCell(3).setCellValue(product.getProductName());
                row.createCell(4).setCellValue(product.getPrice().doubleValue());
                row.createCell(5).setCellValue(product.getValidityIndicator() ? "Sí" : "No");
                row.createCell(6).setCellValue(product.getRegistrationDate().toString());
            }
            
            // Autoajustar columnas
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Escribir flujo en memoria y codificar a Base64
            workbook.write(outputStream);
            byte[] bytes = outputStream.toByteArray();
            String base64String = Base64.getEncoder().encodeToString(bytes);
            
            return new ExcelResponseDto(HttpStatus.OK.value(), "Excel file successfully generated", "products_report.xlsx", base64String);
            
        } catch (IOException e) {
            throw new RuntimeException("Error building report in Excel", e);
        }
    }

    /**
     * Regla de negocio para generar un identificador de producto único.
     *
     * El formato usado es `PROD-AAAAMMDD-HHMMSS`, que facilita rastreo y
     * asegura unicidad temporal en el contexto de este servicio.
     */
    private String generateBusinessId() {
        String prefijo = "PROD-";
        String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        return prefijo + fechaHora;
    }

}
