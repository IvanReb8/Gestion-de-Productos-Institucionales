import { Component, inject, OnInit, signal } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

/**
 * Componente de dashboard del frontend.
 *
 * Controla la vista principal de productos, incluyendo búsqueda filtrada,
 * paginación, exportación a Excel y los formularios de creación/edición.
 */
@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  private productService = inject(ProductService);
  authService = inject(AuthService);

  // Estados reactivos locales con Signals para que la UI se actualice automáticamente
  products = signal<any[]>([]);
  totalElements = signal<number>(0);
  totalPages = signal<number>(0);
  currentPage = signal<number>(0);
  pageSize = 5;

  // Filtros de búsqueda vinculados a los inputs de la tarjeta de filtros
  filters = { productName: '', productCode: '', priceMin: null, priceMax: null };

  // Indica si el formulario del modal está en modo edición o creación
  isEditionMode = signal<boolean>(false);

  // Objeto molde que alimenta el formulario de creación/edición
  formProduct = {
    technicalId: null as number | null,
    productCode: '',
    productName: '',
    price: null as number | null,
    validityIndicator: true
  };

  ngOnInit() {
    this.loadProducts();
  }

  /**
   * Carga productos desde el backend usando los filtros y la página actual.
   * @param page índice de página a solicitar
   */
  loadProducts(page: number = 0) {
    this.currentPage.set(page);
    this.productService.findProducts(this.filters, page, this.pageSize).subscribe({
      next: (res) => {
        this.products.set(res.content);
        this.totalElements.set(res.totalElements);
        this.totalPages.set(res.totalPages);
      },
      error: (err) => alert('Error al cargar los productos o la sesión ha caducado.')
    });
  }

  /**
   * Restaura los filtros a su estado inicial y recarga la primera página.
   */
  cleanFilters() {
    this.filters = { productName: '', productCode: '', priceMin: null, priceMax: null };
    this.loadProducts(0);
  }

  // --- NUEVOS MÉTODOS DE CONTROL DEL MODAL ---

  openModalCreate() {
    this.isEditionMode.set(false);
    this.formProduct = {
      technicalId: null,
      productCode: '',
      productName: '',
      price: null,
      validityIndicator: true
    };
  }

  openModalUpdate(product: any) {
    this.isEditionMode.set(true);
    // Clonamos el objeto para evitar modificar la tabla directamente antes de guardar
    this.formProduct = {
      technicalId: product.technicalId,
      productCode: product.productCode,
      productName: product.productName,
      price: product.price,
      validityIndicator: product.validityIndicator
    };
  }

  /**
   * Guarda el producto en modo creación o en modo edición, según el estado activo.
   */
  saveProduct() {
    // Validaciones básicas preventivas en FrontEnd
    if (!this.formProduct.productCode || !this.formProduct.productName || this.formProduct.price === null) {
      alert('Por favor, rellene todos los campos obligatorios.');
      return;
    }

    if (this.formProduct.price < 0) {
      alert('El precio no puede ser un valor negativo.');
      return;
    }

    if (this.isEditionMode()) {
      // Flujo de Actualización (PUT)
      this.productService.updateProduct(this.formProduct.technicalId!, this.formProduct).subscribe({
        next: () => {
          alert('Producto actualizado correctamente.');
          this.closeProgrammaticModal();
          this.loadProducts(this.currentPage()); // Recarga la página actual
        },
        error: (err) => alert('Error al actualizar: ' + (err.error?.message || 'Internal failure'))
      });
    } else {
      // Flujo de Alta (POST)
      this.productService.createProduct(this.formProduct).subscribe({
        next: () => {
          alert('Producto creado correctamente.');
          this.closeProgrammaticModal();
          this.loadProducts(0); // Vuelve a la página 1 para ver el nuevo registro
        },
        error: (err) => alert('Error al registrar: ' + (err.error?.message || 'Internal failure'))
      });
    }
  }

  private closeProgrammaticModal() {
    // Cerramos el modal simulando el clic en el botón 'Cancelar' o 'X'
    const btnClose = document.getElementById('btnCloseModal');
    if (btnClose) btnClose.click();
  }

  /**
   * Descarga un reporte Excel basado en los filtros actuales.
   *
   * El backend devuelve el archivo codificado en Base64 y el cliente lo
   * descarga como Blob usando un enlace temporal.
   */
  downloadExcelReport() {
    this.productService.exportProductsToExcel(this.filters).subscribe({
      next: (response) => {
        const base64Data = response.fileBase64;
        const contentType = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';

        // Algoritmo nativo de decodificación binaria en cliente
        const byteCharacters = atob(base64Data);
        const byteNumbers = new Array(byteCharacters.length);
        for (let i = 0; i < byteCharacters.length; i++) {
          byteNumbers[i] = byteCharacters.charCodeAt(i);
        }
        const byteArray = new Uint8Array(byteNumbers);
        const blob = new Blob([byteArray], { type: contentType });

        // Simulación de clic del usuario para disparar la descarga en el navegador
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = response.fileName || 'products_catalog.xlsx';
        link.click();
        window.URL.revokeObjectURL(url);
      },
      error: () => alert('Error en la construcción del informe de Excel.')
    });
  }
}
