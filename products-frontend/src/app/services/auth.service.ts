import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';

/**
 * Servicio de autenticación para la aplicación frontend.
 *
 * Administra el flujo de login/logout, guarda el token JWT en localStorage y
 * expone el estado reactivo de autenticación con una `signal`.
 */
@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private apiUrl = 'http://localhost:8080/api/v1/auth';

  /**
   * Estado reactivo que indica si el usuario está autenticado.
   * Se inicializa a true si ya existe un token JWT en localStorage.
   */
  isAuthenticated = signal<boolean>(!!localStorage.getItem('jwt_token'));

  /**
   * Envía las credenciales al endpoint de login del backend.
   *
   * Si la respuesta contiene un token, lo almacena en localStorage y actualiza
   * la señal de autenticación para que otros componentes reaccionen.
   */
  login(credentials: { username: string; password: string }) {
    return this.http.post<any>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        if (response && response.token) {
          localStorage.setItem('jwt_token', response.token);
          this.isAuthenticated.set(true);
        }
      })
    );
  }

  /**
   * Desconecta al usuario limpiando el token y redirigiendo a la pantalla de
   * login.
   */
  logout() {
    localStorage.removeItem('jwt_token');
    this.isAuthenticated.set(false);
    this.router.navigate(['/login']);
  }

  /**
   * Devuelve el token JWT almacenado en localStorage, o null si no existe.
   */
  getToken(): string | null {
    return localStorage.getItem('jwt_token');
  }
}
