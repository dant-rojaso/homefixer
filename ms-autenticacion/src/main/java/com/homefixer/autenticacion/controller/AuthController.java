package com.homefixer.autenticacion.controller;

import com.homefixer.autenticacion.model.Token; // Importa entidad Token
import com.homefixer.autenticacion.model.Sesion; // Importa entidad Sesion
import com.homefixer.autenticacion.service.TokenService; // Importa servicio Token
import com.homefixer.autenticacion.service.SesionService; // Importa servicio Sesion
import lombok.RequiredArgsConstructor; // Constructor automático
import org.springframework.http.ResponseEntity; // Para respuestas HTTP
import org.springframework.web.bind.annotation.*; // Anotaciones REST
import jakarta.servlet.http.HttpServletRequest; // Para obtener IP y user agent
import java.util.Map; // Para mapas de datos
import java.util.HashMap; // Para implementación de mapas

@RestController // Controlador REST
@RequestMapping("/api/auth") // URL base: /api/auth
@RequiredArgsConstructor // Constructor automático
public class AuthController {
    
    private final TokenService tokenService; // Servicio de tokens inyectado
    private final SesionService sesionService; // Servicio de sesiones inyectado
    
    // POST /api/auth/login - Iniciar sesión
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> loginData, HttpServletRequest request) {
        System.out.println("🔐 POST /api/auth/login - Iniciando sesión"); // Log request
        
        try {
            // Extraer datos del request
            Long idUsuario = Long.valueOf(loginData.get("idUsuario").toString()); // ID del usuario
            String ipCliente = obtenerIpCliente(request); // IP del cliente
            String userAgent = request.getHeader("User-Agent"); // Navegador del cliente
            
            // Generar token de login
            Token tokenLogin = tokenService.generarTokenLogin(idUsuario, ipCliente, userAgent); // Crea token
            
            // Iniciar sesión
            Sesion sesion = sesionService.iniciarSesion(idUsuario, ipCliente, userAgent); // Crea sesión
            
            // Preparar respuesta
            Map<String, Object> respuesta = new HashMap<>(); // Mapa para respuesta
            respuesta.put("success", true); // Indica éxito
            respuesta.put("token", tokenLogin.getToken()); // Token de acceso
            respuesta.put("tokenSesion", sesion.getTokenSesion()); // Token de sesión
            respuesta.put("expiresAt", tokenLogin.getFechaExpiracion()); // Cuándo expira
            respuesta.put("message", "Login exitoso"); // Mensaje de éxito
            
            System.out.println("✅ Login exitoso para usuario: " + idUsuario); // Log éxito
            return ResponseEntity.ok(respuesta); // Retorna 200 OK
            
        } catch (Exception e) {
            System.out.println("❌ Error en login: " + e.getMessage()); // Log error
            
            Map<String, Object> error = new HashMap<>(); // Mapa para error
            error.put("success", false); // Indica fallo
            error.put("message", "Error en login: " + e.getMessage()); // Mensaje de error
            
            return ResponseEntity.badRequest().body(error); // Retorna 400
        }
    }
    
    // POST /api/auth/logout - Cerrar sesión
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestHeader("Authorization") String token, @RequestHeader("Session-Token") String tokenSesion) {
        System.out.println("🔚 POST /api/auth/logout - Cerrando sesión"); // Log request
        
        try {
            // Revocar token
            String tokenLimpio = limpiarToken(token); // Quita prefijo Bearer
            tokenService.revocarToken(tokenLimpio); // Revoca token
            
            // Cerrar sesión
            sesionService.cerrarSesion(tokenSesion); // Cierra sesión
            
            Map<String, Object> respuesta = new HashMap<>(); // Mapa para respuesta
            respuesta.put("success", true); // Indica éxito
            respuesta.put("message", "Logout exitoso"); // Mensaje de éxito
            
            System.out.println("✅ Logout exitoso"); // Log éxito
            return ResponseEntity.ok(respuesta); // Retorna 200 OK
            
        } catch (Exception e) {
            System.out.println("❌ Error en logout: " + e.getMessage()); // Log error
            
            Map<String, Object> error = new HashMap<>(); // Mapa para error
            error.put("success", false); // Indica fallo
            error.put("message", "Error en logout"); // Mensaje de error
            
            return ResponseEntity.badRequest().body(error); // Retorna 400
        }
    }
    
    // POST /api/auth/validate - Validar token
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validarToken(@RequestHeader("Authorization") String token) {
        System.out.println("🔍 POST /api/auth/validate - Validando token"); // Log request
        
        String tokenLimpio = limpiarToken(token); // Quita prefijo Bearer
        boolean valido = tokenService.validarToken(tokenLimpio); // Valida token
        
        Map<String, Object> respuesta = new HashMap<>(); // Mapa para respuesta
        respuesta.put("valid", valido); // Indica si es válido
        
        if (valido) {
            // Obtener información del usuario
            Long idUsuario = tokenService.obtenerUsuarioPorToken(tokenLimpio).orElse(null); // Obtiene ID usuario
            respuesta.put("idUsuario", idUsuario); // Agrega ID usuario
            respuesta.put("message", "Token válido"); // Mensaje de éxito
            System.out.println("✅ Token válido para usuario: " + idUsuario); // Log éxito
        } else {
            respuesta.put("message", "Token inválido o expirado"); // Mensaje de error
            System.out.println("❌ Token inválido"); // Log error
        }
        
        return ResponseEntity.ok(respuesta); // Retorna 200 OK
    }
    
    // POST /api/auth/refresh - Renovar token
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> renovarToken(@RequestHeader("Authorization") String token) {
        System.out.println("🔄 POST /api/auth/refresh - Renovando token"); // Log request
        
        try {
            String tokenLimpio = limpiarToken(token); // Quita prefijo Bearer
            
            // Obtener usuario del token actual
            Long idUsuario = tokenService.obtenerUsuarioPorToken(tokenLimpio).orElse(null); // Obtiene ID usuario
            
            if (idUsuario == null) {
                Map<String, Object> error = new HashMap<>(); // Mapa para error
                error.put("success", false); // Indica fallo
                error.put("message", "Token inválido"); // Mensaje de error
                return ResponseEntity.badRequest().body(error); // Retorna 400
            }
            
            // Revocar token anterior
            tokenService.revocarToken(tokenLimpio); // Revoca token anterior
            
            // Generar nuevo token
            Token nuevoToken = tokenService.generarTokenLogin(idUsuario, "127.0.0.1", "Refresh"); // Crea nuevo token
            
            Map<String, Object> respuesta = new HashMap<>(); // Mapa para respuesta
            respuesta.put("success", true); // Indica éxito
            respuesta.put("token", nuevoToken.getToken()); // Nuevo token
            respuesta.put("expiresAt", nuevoToken.getFechaExpiracion()); // Nueva expiración
            respuesta.put("message", "Token renovado"); // Mensaje de éxito
            
            System.out.println("✅ Token renovado para usuario: " + idUsuario); // Log éxito
            return ResponseEntity.ok(respuesta); // Retorna 200 OK
            
        } catch (Exception e) {
            System.out.println("❌ Error renovando token: " + e.getMessage()); // Log error
            
            Map<String, Object> error = new HashMap<>(); // Mapa para error
            error.put("success", false); // Indica fallo
            error.put("message", "Error renovando token"); // Mensaje de error
            
            return ResponseEntity.badRequest().body(error); // Retorna 400
        }
    }
    
    // GET /api/auth/sesiones/{idUsuario} - Obtener sesiones de usuario
    @GetMapping("/sesiones/{idUsuario}")
    public ResponseEntity<Map<String, Object>> obtenerSesiones(@PathVariable Long idUsuario) {
        System.out.println("📋 GET /api/auth/sesiones/" + idUsuario); // Log request
        
        try {
            var sesiones = sesionService.obtenerSesionesUsuario(idUsuario); // Obtiene sesiones
            
            Map<String, Object> respuesta = new HashMap<>(); // Mapa para respuesta
            respuesta.put("success", true); // Indica éxito
            respuesta.put("sesiones", sesiones); // Lista de sesiones
            respuesta.put("total", sesiones.size()); // Total de sesiones
            
            System.out.println("✅ Se encontraron " + sesiones.size() + " sesiones"); // Log éxito
            return ResponseEntity.ok(respuesta); // Retorna 200 OK
            
        } catch (Exception e) {
            System.out.println("❌ Error obteniendo sesiones: " + e.getMessage()); // Log error
            
            Map<String, Object> error = new HashMap<>(); // Mapa para error
            error.put("success", false); // Indica fallo
            error.put("message", "Error obteniendo sesiones"); // Mensaje de error
            
            return ResponseEntity.badRequest().body(error); // Retorna 400
        }
    }
    
    // DELETE /api/auth/sesiones/{idUsuario} - Cerrar todas las sesiones
    @DeleteMapping("/sesiones/{idUsuario}")
    public ResponseEntity<Map<String, Object>> cerrarTodasSesiones(@PathVariable Long idUsuario) {
        System.out.println("🔚 DELETE /api/auth/sesiones/" + idUsuario); // Log request
        
        try {
            sesionService.cerrarTodasSesionesUsuario(idUsuario); // Cierra todas las sesiones
            tokenService.revocarTodosTokensUsuario(idUsuario); // Revoca todos los tokens
            
            Map<String, Object> respuesta = new HashMap<>(); // Mapa para respuesta
            respuesta.put("success", true); // Indica éxito
            respuesta.put("message", "Todas las sesiones cerradas"); // Mensaje de éxito
            
            System.out.println("✅ Todas las sesiones cerradas para usuario: " + idUsuario); // Log éxito
            return ResponseEntity.ok(respuesta); // Retorna 200 OK
            
        } catch (Exception e) {
            System.out.println("❌ Error cerrando sesiones: " + e.getMessage()); // Log error
            
            Map<String, Object> error = new HashMap<>(); // Mapa para error
            error.put("success", false); // Indica fallo
            error.put("message", "Error cerrando sesiones"); // Mensaje de error
            
            return ResponseEntity.badRequest().body(error); // Retorna 400
        }
    }
    
    // Método privado para limpiar token (quitar Bearer)
    private String limpiarToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7); // Quita "Bearer " del inicio
        }
        return token; // Retorna token sin cambios
    }
    
    // Método privado para obtener IP del cliente
    private String obtenerIpCliente(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For"); // IP si hay proxy
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr(); // IP directa
        }
        return ip; // Retorna IP del cliente
    }
}