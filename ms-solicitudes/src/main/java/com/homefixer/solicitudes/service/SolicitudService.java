package com.homefixer.solicitudes.service;

import com.homefixer.solicitudes.model.Solicitud; // Importa entidad Solicitud
import com.homefixer.solicitudes.repository.SolicitudRepository; // Importa repositorio
import lombok.RequiredArgsConstructor; // Constructor automático
import lombok.extern.slf4j.Slf4j; // Logger automático
import org.springframework.stereotype.Service; // Anotación servicio
import java.time.LocalDateTime; // Para fechas
import java.util.List; // Para listas
import java.util.Optional; // Para opcionales

@Service // Marca como servicio Spring
@RequiredArgsConstructor // Constructor automático para dependencias
@Slf4j // Logger automático
public class SolicitudService {
    
    private final SolicitudRepository solicitudRepository; // Repositorio inyectado
    
    // Crear nueva solicitud
    public Solicitud crearSolicitud(Solicitud solicitud) {
        log.info("✅ Creando nueva solicitud para cliente: {}", solicitud.getIdCliente()); // Log inicio
        
        // Establecer valores por defecto
        solicitud.setEstado(Solicitud.EstadoSolicitud.PENDIENTE); // Nueva solicitud pendiente
        solicitud.setFechaCreacion(LocalDateTime.now()); // Fecha actual
        
        // Si no especifica prioridad, poner MEDIA por defecto
        if (solicitud.getPrioridad() == null) {
            solicitud.setPrioridad(Solicitud.PrioridadSolicitud.MEDIA); // Prioridad normal
        }
        
        Solicitud solicitudGuardada = solicitudRepository.save(solicitud); // Guarda en BD
        log.info("✅ Solicitud creada con ID: {}", solicitudGuardada.getIdSolicitud()); // Log éxito
        
        return solicitudGuardada; // Retorna solicitud creada
    }
    
    // Buscar solicitud por ID
    public Optional<Solicitud> buscarPorId(Long id) {
        log.info("🔍 Buscando solicitud por ID: {}", id); // Log búsqueda
        return solicitudRepository.findById(id); // Busca en BD
    }
    
    // Obtener todas las solicitudes
    public List<Solicitud> obtenerTodas() {
        log.info("📋 Obteniendo todas las solicitudes"); // Log consulta
        return solicitudRepository.findAll(); // Retorna todas
    }
    
    // Buscar solicitudes por cliente
    public List<Solicitud> buscarPorCliente(Long idCliente) {
        log.info("🔍 Buscando solicitudes del cliente: {}", idCliente); // Log búsqueda
        return solicitudRepository.findByIdCliente(idCliente); // Filtra por cliente
    }
    
    // Buscar solicitudes por estado
    public List<Solicitud> buscarPorEstado(Solicitud.EstadoSolicitud estado) {
        log.info("🔍 Buscando solicitudes con estado: {}", estado); // Log búsqueda
        return solicitudRepository.findByEstado(estado); // Filtra por estado
    }
    
    // Buscar solicitudes disponibles para técnicos
    public List<Solicitud> obtenerDisponibles(String especialidad) {
        log.info("🎯 Obteniendo solicitudes disponibles para: {}", especialidad); // Log búsqueda
        return solicitudRepository.encontrarSolicitudesDisponibles(especialidad); // Consulta personalizada
    }
    
    // Cambiar estado de solicitud
    public Solicitud cambiarEstado(Long idSolicitud, Solicitud.EstadoSolicitud nuevoEstado) {
        log.info("🔄 Cambiando estado de solicitud {} a: {}", idSolicitud, nuevoEstado); // Log cambio
        
        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada")); // Busca o falla
        
        solicitud.setEstado(nuevoEstado); // Cambia estado
        
        Solicitud guardada = solicitudRepository.save(solicitud); // Guarda cambio
        log.info("✅ Estado cambiado exitosamente"); // Log éxito
        
        return guardada; // Retorna solicitud actualizada
    }
    
    // Actualizar solicitud
    public Solicitud actualizarSolicitud(Long id, Solicitud solicitudActualizada) {
        log.info("📝 Actualizando solicitud ID: {}", id); // Log actualización
        
        Solicitud solicitud = solicitudRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada")); // Busca solicitud
        
        // Actualiza campos editables
        solicitud.setTitulo(solicitudActualizada.getTitulo()); // Nuevo título
        solicitud.setDescripcion(solicitudActualizada.getDescripcion()); // Nueva descripción
        solicitud.setPresupuestoEstimado(solicitudActualizada.getPresupuestoEstimado()); // Nuevo presupuesto
        solicitud.setObservaciones(solicitudActualizada.getObservaciones()); // Nuevas observaciones
        
        Solicitud guardada = solicitudRepository.save(solicitud); // Guarda cambios
        log.info("✅ Solicitud actualizada exitosamente"); // Log éxito
        
        return guardada; // Retorna solicitud actualizada
    }
    
    // Cancelar solicitud
    public Solicitud cancelarSolicitud(Long idSolicitud) {
        log.info("❌ Cancelando solicitud ID: {}", idSolicitud); // Log cancelación
        
        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada")); // Busca solicitud
        
        // Solo se puede cancelar si está pendiente o asignada
        if (solicitud.getEstado() == Solicitud.EstadoSolicitud.COMPLETADA) {
            throw new RuntimeException("No se puede cancelar una solicitud completada"); // Error si ya terminó
        }
        
        solicitud.setEstado(Solicitud.EstadoSolicitud.CANCELADA); // Marca como cancelada
        
        Solicitud guardada = solicitudRepository.save(solicitud); // Guarda cambio
        log.info("✅ Solicitud cancelada exitosamente"); // Log éxito
        
        return guardada; // Retorna solicitud cancelada
    }
}