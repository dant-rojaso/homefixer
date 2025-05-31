package com.homefixer.usuarios.controller;

import com.homefixer.usuarios.model.Usuario; // Importa entidad Usuario
import com.homefixer.usuarios.service.UsuarioService; // Importa servicio
import lombok.RequiredArgsConstructor; // Constructor automático
import lombok.extern.slf4j.Slf4j; // Logger automático
import org.springframework.http.ResponseEntity; // Para respuestas HTTP
import org.springframework.web.bind.annotation.*; // Anotaciones REST
import java.util.List; // Para listas
import java.util.Optional; // Para opcionales

@RestController // Marca como controlador REST
@RequestMapping("/api/usuarios") // URL base para todos los endpoints
@RequiredArgsConstructor // Constructor automático para dependencias
@Slf4j // Logger automático
public class UsuarioController {
    
    private final UsuarioService usuarioService; // Servicio inyectado automáticamente
    
    // GET /api/usuarios - Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos() {
        log.info("📋 GET /api/usuarios - Obteniendo todos los usuarios"); // Log request
        
        List<Usuario> usuarios = usuarioService.obtenerTodos(); // Llama al servicio
        
        log.info("✅ Se encontraron {} usuarios", usuarios.size()); // Log resultado
        return ResponseEntity.ok(usuarios); // Retorna 200 OK con lista
    }
    
    // GET /api/usuarios/{id} - Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        log.info("🔍 GET /api/usuarios/{} - Buscando usuario", id); // Log request
        
        Optional<Usuario> usuario = usuarioService.buscarPorId(id); // Busca usuario
        
        if (usuario.isPresent()) {
            log.info("✅ Usuario encontrado: {}", usuario.get().getEmail()); // Log éxito
            return ResponseEntity.ok(usuario.get()); // Retorna 200 OK con usuario
        } else {
            log.warn("❌ Usuario no encontrado con ID: {}", id); // Log error
            return ResponseEntity.notFound().build(); // Retorna 404 Not Found
        }
    }
    
    // POST /api/usuarios - Crear nuevo usuario
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        log.info("✅ POST /api/usuarios - Creando usuario: {}", usuario.getEmail()); // Log request
        
        try {
            Usuario usuarioCreado = usuarioService.crearUsuario(usuario); // Crea usuario
            log.info("✅ Usuario creado exitosamente con ID: {}", usuarioCreado.getIdUsuario()); // Log éxito
            return ResponseEntity.ok(usuarioCreado); // Retorna 200 OK con usuario creado
        } catch (Exception e) {
            log.error("❌ Error creando usuario: {}", e.getMessage()); // Log error
            return ResponseEntity.badRequest().build(); // Retorna 400 Bad Request
        }
    }
    
    // POST /api/usuarios/login - Login básico
    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody Usuario loginData) {
        log.info("🔐 POST /api/usuarios/login - Intento de login: {}", loginData.getEmail()); // Log request
        
        Optional<Usuario> usuario = usuarioService.login(loginData.getEmail(), loginData.getPassword()); // Intenta login
        
        if (usuario.isPresent()) {
            log.info("✅ Login exitoso para: {}", loginData.getEmail()); // Log éxito
            return ResponseEntity.ok(usuario.get()); // Retorna 200 OK con usuario
        } else {
            log.warn("❌ Login fallido para: {}", loginData.getEmail()); // Log fallo
            return ResponseEntity.badRequest().build(); // Retorna 400 Bad Request
        }
    }
    
    // GET /api/usuarios/tipo/{tipo} - Buscar por tipo
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Usuario>> buscarPorTipo(@PathVariable String tipo) {
        log.info("🔍 GET /api/usuarios/tipo/{} - Buscando por tipo", tipo); // Log request
        
        try {
            Usuario.TipoUsuario tipoUsuario = Usuario.TipoUsuario.valueOf(tipo.toUpperCase()); // Convierte string a enum
            List<Usuario> usuarios = usuarioService.buscarPorTipo(tipoUsuario); // Busca por tipo
            
            log.info("✅ Se encontraron {} usuarios de tipo {}", usuarios.size(), tipo); // Log resultado
            return ResponseEntity.ok(usuarios); // Retorna 200 OK con lista
        } catch (Exception e) {
            log.error("❌ Tipo de usuario inválido: {}", tipo); // Log error
            return ResponseEntity.badRequest().build(); // Retorna 400 Bad Request
        }
    }
    
    // PUT /api/usuarios/{id} - Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        log.info("📝 PUT /api/usuarios/{} - Actualizando usuario", id); // Log request
        
        try {
            Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuario); // Actualiza usuario
            log.info("✅ Usuario actualizado exitosamente"); // Log éxito
            return ResponseEntity.ok(usuarioActualizado); // Retorna 200 OK con usuario actualizado
        } catch (Exception e) {
            log.error("❌ Error actualizando usuario: {}", e.getMessage()); // Log error
            return ResponseEntity.badRequest().build(); // Retorna 400 Bad Request
        }
    }
    
    // DELETE /api/usuarios/{id} - Desactivar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarUsuario(@PathVariable Long id) {
        log.info("🔒 DELETE /api/usuarios/{} - Desactivando usuario", id); // Log request
        
        try {
            usuarioService.desactivarUsuario(id); // Desactiva usuario
            log.info("✅ Usuario desactivado exitosamente"); // Log éxito
            return ResponseEntity.ok().build(); // Retorna 200 OK sin contenido
        } catch (Exception e) {
            log.error("❌ Error desactivando usuario: {}", e.getMessage()); // Log error
            return ResponseEntity.badRequest().build(); // Retorna 400 Bad Request
        }
    }
}