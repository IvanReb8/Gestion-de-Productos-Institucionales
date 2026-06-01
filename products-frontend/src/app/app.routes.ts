import { Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard.component/dashboard.component';
import { LoginComponent } from './components/login.component/login.component';
import { authGuard } from './guards/auth.guard';

/**
 * Configuración de rutas de la aplicación.
 *
 * Define las rutas públicas y protegidas, así como los redireccionamientos
 * por defecto para el flujo de login y el dashboard.
 */
export const routes: Routes = [
  // Ruta pública de login accesible sin token
  { path: 'login', component: LoginComponent },
  // El dashboard requiere autenticación JWT mediante authGuard
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
  // Redirect de raíz a login cuando no hay path
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  // Cualquier ruta desconocida redirige a login
  { path: '**', redirectTo: '/login' }
];
