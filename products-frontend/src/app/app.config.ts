import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideHttpClient } from '@angular/common/http';

/**
 * Configuración principal de la aplicación Angular.
 *
 * Define los proveedores de nivel raíz usados por `bootstrapApplication`.
 *
 * - `provideRouter(routes)` configura el enrutamiento basado en la colección
 *   de rutas definida en `app.routes.ts`.
 * - `provideHttpClient()` activa el cliente HTTP moderno de Angular sin
 *   depender de los módulos clásicos de `HttpClientModule`.
 */
export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient() // Requerido para inyectar servicios HTTP sin módulos antiguos
  ]
};
