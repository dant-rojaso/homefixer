package com.homefixer.usuarios.service;

import com.homefixer.usuarios.model.Usuario; // Importa entidad Usuario
import com.homefixer.usuarios.repository.UsuarioRepository; // Importa repositorio
import lombok.RequiredArgsConstructor; // Lombok para constructor automático
import lombok.extern.slf4j.Slf4j; // Para logging
import org.springframework.stereotype.Service; // Marca como servicio
import java.time.LocalDateTime; // Para fechas
import java.util.List; // Para listas
import java.util.Optional; // Para opcionales

@Service // Marca esta clase como servicio de Spring
@RequiredArgsConstructor // Lombok: constructor automático para campos final
@Slf4j // Lombok: añade logger automáticamente
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository; // Inyección de dependencia automática
    
    // Crear nuevo usuario
    public Usuario crearUsuario(Usuario usuario) {
        log.info("✅ Creando nuevo usuario: {}", usuario.getEmail()); // Log de inicio
        
        // Validar que el email no exista
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            log.error("❌ Email ya existe: {}", usuario.getEmail()); // Log de error
            throw new RuntimeException("El email ya está registrado"); // Excepción simple
        }
        
        usuario.setFechaRegistro(LocalDateTime.now()); // Establece fecha actual
        usuario.setActivo(true); // Usuario activo por defecto
        
        Usuario usuarioGuardado = usuarioRepository.save(usuario); // Guarda en BD
        log.info("✅ Usuario creado exitosamente con ID: {}", usuarioGuardado.getIdUsuario()); // Log de éxito
        
        return usuarioGuardado; // Retorna usuario guardado
    }
    
    // Buscar usuario por ID
    public Optional<Usuario> buscarPorId(Long id) {
        log.info("🔍 Buscando usuario por ID: {}", id); // Log de búsqueda
        return usuarioRepository.findById(id); // Busca en BD
    }
    
    // Buscar todos los usuarios
    public List<Usuario> obtenerTodos() {
        log.info("📋 Obteniendo todos los usuarios"); // Log de consulta
        return usuarioRepository.findAll(); // Retorna todos
    }
    
    // Login básico
    public Optional<Usuario> login(String email, String password) {
        log.info("🔐 Intento de login para email: {}", email); // Log de login
        
        Optional<Usuario> usuario = usuarioRepository.findByEmailAndPassword(email, password); // Busca credenciales
        
        if (usuario.isPresent()) {
            log.info("✅ Login exitoso para: {}", email); // Log de éxito
        } else {
            log.warn("❌ Login fallido para: {}", email); // Log de fallo
        }
        
        return usuario; // Retorna usuario si existe
    }
    
    // Buscar usuarios por tipo
    public List<Usuario> buscarPorTipo(Usuario.TipoUsuario tipo) {
        log.info("🔍 Buscando usuarios de tipo: {}", tipo); // Log de búsqueda
        return usuarioRepository.findByTipo(tipo); // Filtra por tipo
    }
    
    // Actualizar usuario
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        log.info("📝 Actualizando usuario ID: {}", id); // Log de actualización
        
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado")); // Busca o lanza excepción
        
        // Actualiza campos permitidos
        usuario.setNombre(usuarioActualizado.getNombre()); // Actualiza nombre
        usuario.setTelefono(usuarioActualizado.getTelefono()); // Actualiza teléfono
        
        Usuario guardado = usuarioRepository.save(usuario); // Guarda cambios
        log.info("✅ Usuario actualizado exitosamente"); // Log de éxito
        
        return guardado; // Retorna usuario actualizado
    }
    
    // Desactivar usuario (no eliminar)
    public void desactivarUsuario(Long id) {
        log.info("🔒 Desactivando usuario ID: {}", id); // Log de desactivación
        
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado")); // Busca usuario
        
        usuario.setActivo(false); // Marca como inactivo
        usuarioRepository.save(usuario); // Guarda cambio
        
        log.info("✅ Usuario desactivado exitosamente"); // Log de éxito
    }
}