package gob.imss.mx.products.controllers;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gob.imss.mx.products.config.JwtUtil;
import gob.imss.mx.products.dto.AuthRequestDTO;
import gob.imss.mx.products.dto.AuthResponseDTO;
import gob.imss.mx.products.exceptions.ErrorResponse;
import jakarta.validation.Valid;

/**
 * Controlador de autenticación.
 *
 * Proporciona un endpoint de login que emite un JWT válido para credenciales
 * de usuario de ejemplo. Ideal para pruebas y demostraciones del flujo de
 * autenticación en el proyecto.
 */
@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * POST /api/v1/auth/login
     *
     * Valida credenciales de ejemplo y emite un JWT cuando son correctas.
     *
     * Este endpoint está diseñado como un stub de autenticación para el
     * ambiente de examen/demo, usando credenciales fijas (`admin` / `admin`).
     *
     * @param authRequest credenciales de usuario
     * @return respuesta con el token JWT o un error 401
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequestDTO authRequest) {
        
        // Simulación rápida de credenciales para el entorno del examen
        if ("admin".equals(authRequest.getUsername()) && "admin".equals(authRequest.getPassword())) {
            
            // Generamos el token usando nuestro componente utilitario
            String token = jwtUtil.generateToken(authRequest.getUsername());
            return ResponseEntity.ok(new AuthResponseDTO(token));
        }

        // Si las credenciales fallan, usamos nuestro formato homogéneo de error
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Invalid credentials. Access denied."
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

}
