package gob.imss.mx.products.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad de Spring Security para la aplicación.
 *
 * Esta clase define el filtro de seguridad y las reglas de autorización
 * para los endpoints de autenticación y productos.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desactiva CSRF ya que no manejamos cookies/sesiones tradicionales
            .csrf(csrf -> csrf.disable())
            
            // Habilita la configuración de CORS nativa de Spring
            .cors(cors -> cors.configure(http))
            
            // Reglas de autorización de accesos
            .authorizeHttpRequests(auth -> auth
                // Endpoint público de login (sin autenticación requerida)
                .requestMatchers("/api/v1/auth/**").permitAll() 
                // Todos los endpoints de productos exigen autenticación JWT válida
                .requestMatchers("/api/v1/products/**").authenticated()
                .anyRequest().authenticated()
            )
            
            // Forzamos a que la aplicación sea enteramente Stateless (Sin estado)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Insertamos nuestro filtro de verificación JWT
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
