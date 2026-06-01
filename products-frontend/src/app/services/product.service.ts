import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { Observable } from 'rxjs';

/**
 * Servicio de frontend para consumir los endpoints de producto.
 *
 * Proporciona operaciones para buscar productos, crear/actualizar productos
 * y exportar datos a Excel. También agrega el token JWT a las solicitudes
 * mediante el helper `getHeaders()`.
 */
@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private http = inject(HttpClient);
  private authService = inject(AuthService);
  private apiUrl = 'http://localhost:8080/api/v1/products';

  /**
   * Construye los encabezados HTTP necesarios para las llamadas autenticadas.
   *
   * Incluye el token JWT en el encabezado `Authorization` cuando está disponible.
   */
  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  /**
   * Consulta el backend con filtros y paginación.
   *
   * @param filters filtros opcionales para nombre, código y rango de precios
   * @param page número de página
   * @param size tamaño de página
   */
  findProducts(filters: any, page: number, size: number): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (filters.productName) params = params.set('productName', filters.productName);
    if (filters.productCode) params = params.set('productCode', filters.productCode);
    if (filters.priceMin) params = params.set('priceMin', filters.priceMin);
    if (filters.priceMax) params = params.set('priceMax', filters.priceMax);

    return this.http.get<any>(this.apiUrl, { params, headers: this.getHeaders() });
  }

  /**
   * Crea un producto enviando los datos al backend.
   */
  createProduct(product: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, product, { headers: this.getHeaders() });
  }

  /**
   * Actualiza un producto existente usando su ID técnico.
   */
  updateProduct(productId: number, product: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${productId}`, product, { headers: this.getHeaders() });
  }

  /**
   * Descarga la lista de productos filtrada en formato Excel.
   *
   * El resultado retorna un objeto que incluye el archivo en Base64.
   */
  exportProductsToExcel(filters: any): Observable<any> {
    let params = new HttpParams();
    if (filters.productName) params = params.set('productName', filters.productName);
    if (filters.productCode) params = params.set('productCode', filters.productCode);
    if (filters.priceMin) params = params.set('priceMin', filters.priceMin);
    if (filters.priceMax) params = params.set('priceMax', filters.priceMax);

    return this.http.get<any>(`${this.apiUrl}/export`, { params, headers: this.getHeaders() });
  }
}
