import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';

/**
 * Componente raíz de la aplicación Angular.
 *
 * Este componente se monta en `<app-root>` dentro de `index.html` y actúa
 * como el contenedor principal de la aplicación. Utiliza una señal para
 * mantener el título de la aplicación y expone `RouterOutlet` para el
 * enrutamiento de las páginas secundarias.
 */
@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  /** Título visible de la aplicación. */
  protected readonly title = signal('products-frontend');
}
