package com.homefixer.asignaciones.service;

import com.homefixer.asignaciones.model.Asignacion; // Importa entidad
import com.homefixer.asignaciones.repository.AsignacionRepository; // Importa repositorio
import lombok.RequiredArgsConstructor; // Constructor automático
import org.springframework.stereotype.Service; // IMPORTANTE: Anotación @Service
import java.math.BigDecimal; // Para decimales
import java.time.LocalDateTime; // Para fechas
import java.util.List; // Para listas
import java.util.Optional; // Para opcionales

@Service // ESTA ANOTACIÓN ES OBLIGATORIA
@RequiredArgsConstructor // Constructor automático
public class AsignacionService {
    
    private final AsignacionRepository asignacionRepository; // Repositorio inyectado
    
    // Crear nueva asignación
    public Asignacion crearAsignacion(Asignacion asignacion) {
        System.out.println("✅ Creando asignación"); // Log
        
        asignacion.setEstado(Asignacion.EstadoAsignacion.PROPUESTA); // Estado inicial
        asignacion.setFechaAsignacion(LocalDateTime.now()); // Fecha actual
        asignacion.setDistanciaKm(BigDecimal.valueOf(5.0)); // Distancia fija para prueba
        asignacion.setTiempoEstimadoMinutos(30); // Tiempo fijo para prueba
        
        return asignacionRepository.save(asignacion); // Guarda en BD
    }
    
    // Buscar por ID
    public Optional<Asignacion> buscarPorId(Long id) {
        return asignacionRepository.findById(id); // Busca en BD
    }
    
    // Obtener todas
    public List<Asignacion> obtenerTodas() {
        return asignacionRepository.findAll(); // Retorna todas
    }
    
    // Buscar por técnico
    public List<Asignacion> buscarPorTecnico(Long idTecnico) {
        return asignacionRepository.findByIdTecnico(idTecnico); // Filtra por técnico
    }
    
    // Aceptar asignación
    public Asignacion aceptarAsignacion(Long idAsignacion) {
        Asignacion asignacion = asignacionRepository.findById(idAsignacion)
            .orElseThrow(() -> new RuntimeException("Asignación no encontrada")); // Busca
        
        asignacion.setEstado(Asignacion.EstadoAsignacion.ACEPTADA); // Cambia estado
        asignacion.setFechaAceptacion(LocalDateTime.now()); // Marca fecha
        
        return asignacionRepository.save(asignacion); // Guarda cambio
    }
    
    // Rechazar asignación
    public Asignacion rechazarAsignacion(Long idAsignacion, String motivo) {
        Asignacion asignacion = asignacionRepository.findById(idAsignacion)
            .orElseThrow(() -> new RuntimeException("Asignación no encontrada")); // Busca
        
        asignacion.setEstado(Asignacion.EstadoAsignacion.RECHAZADA); // Cambia estado
        asignacion.setMotivoRechazo(motivo); // Guarda motivo
        
        return asignacionRepository.save(asignacion); // Guarda cambio
    }
}