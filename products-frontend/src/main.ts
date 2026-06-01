import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';

/**
 * Punto de entrada de la aplicación Angular.
 *
 * `bootstrapApplication` inicializa el componente raíz `App` junto con la
 * configuración definida en `appConfig`.
 *
 * Los errores de arranque se capturan y se muestran en consola.
 */
bootstrapApplication(App, appConfig)
  .catch((err) => console.error(err));
