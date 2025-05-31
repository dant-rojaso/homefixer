package com.homefixer.usuarios.service;

import com.homefixer.usuarios.model.Tecnico; // Importa entidad Tecnico
import com.homefixer.usuarios.repository.TecnicoRepository; // Importa repositorio
import lombok.RequiredArgsConstructor; // Constructor automático
import lombok.extern.slf4j.Slf4j; // Logger automático
import org.springframework.stereotype.Service; // Anotación servicio
import java.math.BigDecimal; // Para decimales
import java.time.LocalDateTime; // Para fechas
import java.util.List; // Para listas
import java.util.Optional; // Para opcionales

@Service // Marca como servicio Spring
@RequiredArgsConstructor // Constructor automático para dependencias
@Slf4j // Logger automático
public class TecnicoService {
    
    private final TecnicoRepository tecnicoRepository; // Repositorio inyectado
    
    // Crear perfil de técnico
    public Tecnico crearTecnico(Tecnico tecnico) {
        log.info("✅ Creando perfil de técnico para usuario ID: {}", tecnico.getIdUsuario()); // Log inicio
        
        // Verificar que no exista perfil de técnico para este usuario
        if (tecnicoRepository.existsByIdUsuario(tecnico.getIdUsuario())) {
            log.error("❌ Ya existe perfil de técnico para usuario: {}", tecnico.getIdUsuario()); // Log error
            throw new RuntimeException("El usuario ya tiene perfil de técnico"); // Excepción
        }
        
        // Valores por defecto para nuevo técnico
        tecnico.setEstado(Tecnico.EstadoTecnico.DISPONIBLE); // Disponible por defecto
        tecnico.setCalificacionPromedio(BigDecimal.valueOf(5.0)); // Inicia con 5 estrellas
        tecnico.setServiciosCompletados(0); // Empieza con 0 servicios
        
        Tecnico tecnicoGuardado = tecnicoRepository.save(tecnico); // Guarda en BD
        log.info("✅ Perfil de técnico creado con ID: {}", tecnicoGuardado.getIdTecnico()); // Log éxito
        
        return tecnicoGuardado; // Retorna técnico creado
    }
    
    // Buscar técnico por ID
    public Optional<Tecnico> buscarPorId(Long id) {
        log.info("🔍 Buscando técnico por ID: {}", id); // Log búsqueda
        return tecnicoRepository.findById(id); // Busca en BD
    }
    
    // Buscar técnico por ID de usuario
    public Optional<Tecnico> buscarPorIdUsuario(Long idUsuario) {
        log.info("🔍 Buscando técnico por ID usuario: {}", idUsuario); // Log búsqueda
        return tecnicoRepository.findByIdUsuario(idUsuario); // Busca por FK
    }
    
    // Obtener todos los técnicos
    public List<Tecnico> obtenerTodos() {
        log.info("📋 Obteniendo todos los técnicos"); // Log consulta
        return tecnicoRepository.findAll(); // Retorna todos
    }
    
    // Buscar técnicos por especialidad
    public List<Tecnico> buscarPorEspecialidad(String especialidad) {
        log.info("🔧 Buscando técnicos de especialidad: {}", especialidad); // Log búsqueda
        return tecnicoRepository.findByEspecialidad(especialidad); // Filtra por especialidad
    }
    
    // Buscar técnicos disponibles
    public List<Tecnico> obtenerDisponibles() {
        log.info("✅ Obteniendo técnicos disponibles"); // Log consulta
        return tecnicoRepository.findByEstado(Tecnico.EstadoTecnico.DISPONIBLE); // Solo disponibles
    }
    
    // Buscar mejores técnicos de una especialidad
    public List<Tecnico> obtenerMejoresTecnicos(String especialidad) {
        log.info("⭐ Buscando mejores técnicos de: {}", especialidad); // Log búsqueda
        BigDecimal calificacionMinima = BigDecimal.valueOf(4.0); // Mínimo 4 estrellas
        return tecnicoRepository.encontrarMejoresTecnicosDisponibles(especialidad, calificacionMinima); // Consulta personalizada
    }
    
    // Cambiar estado de técnico
    public Tecnico cambiarEstado(Long idTecnico, Tecnico.EstadoTecnico nuevoEstado) {
        log.info("🔄 Cambiando estado de técnico ID {} a: {}", idTecnico, nuevoEstado); // Log cambio
        
        Tecnico tecnico = tecnicoRepository.findById(idTecnico)
            .orElseThrow(() -> new RuntimeException("Técnico no encontrado")); // Busca técnico
        
        tecnico.setEstado(nuevoEstado); // Cambia estado
        
        Tecnico guardado = tecnicoRepository.save(tecnico); // Guarda cambio
        log.info("✅ Estado cambiado exitosamente"); // Log éxito
        
        return guardado; // Retorna técnico actualizado
    }
    
    // Completar servicio (incrementa contador)
    public Tecnico completarServicio(Long idTecnico) {
        log.info("🎯 Completando servicio para técnico ID: {}", idTecnico); // Log completar
        
        Tecnico tecnico = tecnicoRepository.findById(idTecnico)
            .orElseThrow(() -> new RuntimeException("Técnico no encontrado")); // Busca técnico
        
        tecnico.setServiciosCompletados(tecnico.getServiciosCompletados() + 1); // Incrementa contador
        tecnico.setFechaUltimoServicio(LocalDateTime.now()); // Actualiza fecha
        tecnico.setEstado(Tecnico.EstadoTecnico.DISPONIBLE); // Vuelve a disponible
        
        Tecnico guardado = tecnicoRepository.save(tecnico); // Guarda cambios
        log.info("✅ Servicio completado. Total servicios: {}", guardado.getServiciosCompletados()); // Log éxito
        
        return guardado; // Retorna técnico actualizado
    }
    
    // Actualizar calificación promedio
    public Tecnico actualizarCalificacion(Long idTecnico, BigDecimal nuevaCalificacion) {
        log.info("⭐ Actualizando calificación de técnico ID: {} a {}", idTecnico, nuevaCalificacion); // Log actualización
        
        Tecnico tecnico = tecnicoRepository.findById(idTecnico)
            .orElseThrow(() -> new RuntimeException("Técnico no encontrado")); // Busca técnico
        
        tecnico.setCalificacionPromedio(nuevaCalificacion); // Actualiza calificación
        
        Tecnico guardado = tecnicoRepository.save(tecnico); // Guarda cambio
        log.info("✅ Calificación actualizada exitosamente"); // Log éxito
        
        return guardado; // Retorna técnico actualizado
    }
}