import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * Guard de ruta que protege rutas que requieren autenticación.
 *
 * Comprueba el estado reactivo de autenticación expuesto por `AuthService`
 * y redirige a `/login` cuando el usuario no está autenticado.
 */
export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Verificamos el estado reactivo usando el Signal del servicio
  if (authService.isAuthenticated()) {
    return true;
  }

  // Si no está autenticado, redirige al login y bloquea el acceso
  router.navigate(['/login']);
  return false;
};
