import { Component, inject, signal } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

/**
 * Componente de login.
 *
 * Presenta el formulario de autenticación, procesa las credenciales y
 * redirige al usuario al dashboard cuando el login es exitoso.
 *
 * Usa señales de Angular para manejar el estado de carga y los mensajes de error.
 */
@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  /** Modelo de credenciales vinculado por `ngModel` al formulario. */
  credentials = { username: '', password: '' };

  /** Mensaje de error mostrado cuando falla la autenticación. */
  errorMessage = signal<string | null>(null);

  /** Estado de carga para deshabilitar el botón y mostrar spinner. */
  loading = signal<boolean>(false);

  /**
   * Envía el formulario de login al servicio de autenticación.
   *
   * Valida que los campos no estén vacíos, establece el estado de carga y
   * consume el endpoint de login. En caso de éxito redirige al dashboard.
   */
  onSubmit() {
    if (!this.credentials.username || !this.credentials.password) {
      this.errorMessage.set('Please fill in all fields.');
      return;
    }

    this.loading.set(true);
    this.errorMessage.set(null);

    this.authService.login(this.credentials).subscribe({
      next: () => {
        this.loading.set(false);
        this.router.navigate(['/dashboard']); // Redirección al éxito
      },
      error: (err) => {
        this.loading.set(false);
        // Captura el JSON de error homogéneo estructurado en tu Backend
        const apiError = err.error?.message || 'Error de conexión con el servidor.';
        this.errorMessage.set(apiError);
      }
    });
  }
}
