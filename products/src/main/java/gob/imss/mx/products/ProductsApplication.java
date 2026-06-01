package gob.imss.mx.products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de arranque de la aplicación Spring Boot.
 *
 * Marca el inicio del servicio `products` y activa el escaneo de componentes,
 * configuración automática y todos los mecanismos de arranque de Spring Boot.
 */
@SpringBootApplication
public class ProductsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductsApplication.class, args);
	}

}
