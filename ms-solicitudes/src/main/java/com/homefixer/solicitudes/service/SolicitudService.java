package com.homefixer.solicitudes.service;

import com.homefixer.solicitudes.model.EstadoSolicitud;
import com.homefixer.solicitudes.model.Solicitud;
import com.homefixer.solicitudes.repository.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la lógica de negocio de solicitudes
 */
@Service
@RequiredArgsConstructor
public class SolicitudService {
    
    private final SolicitudRepository solicitudRepository;
    
    /**
     * Obtener todas las solicitudes
     */
    public List<Solicitud> obtenerTodasLasSolicitudes() {
        return solicitudRepository.findAll();
    }
    
    /**
     * Obtener solicitud por ID
     */
    public Optional<Solicitud> obtenerSolicitudPorId(Long id) {
        return solicitudRepository.findById(id);
    }
    
    /**
     * Crear nueva solicitud
     */
    @Transactional
    public Solicitud crearSolicitud(Solicitud solicitud) {
        // Establecer estado inicial
        solicitud.setEstadoSolicitud(EstadoSolicitud.PENDIENTE);
        return solicitudRepository.save(solicitud);
    }
    
    /**
     * Actualizar solicitud existente
     */
    @Transactional
    public Solicitud actualizarSolicitud(Long id, Solicitud solicitudActualizada) {
        Optional<Solicitud> solicitudExistente = solicitudRepository.findById(id);
        
        if (solicitudExistente.isPresent()) {
            Solicitud solicitud = solicitudExistente.get();
            solicitud.setTipoServicio(solicitudActualizada.getTipoServicio());
            solicitud.setDescripcionProblema(solicitudActualizada.getDescripcionProblema());
            solicitud.setDireccionServicio(solicitudActualizada.getDireccionServicio());
            solicitud.setFechaServicio(solicitudActualizada.getFechaServicio());
            solicitud.setPresupuestoEstimado(solicitudActualizada.getPresupuestoEstimado());
            solicitud.setObservaciones(solicitudActualizada.getObservaciones());
            
            return solicitudRepository.save(solicitud);
        }
        
        throw new RuntimeException("Solicitud no encontrada con ID: " + id);
    }
    
    /**
     * Cancelar solicitud
     */
    @Transactional
    public void cancelarSolicitud(Long id) {
        Optional<Solicitud> solicitud = solicitudRepository.findById(id);
        
        if (solicitud.isPresent()) {
            Solicitud s = solicitud.get();
            s.setEstadoSolicitud(EstadoSolicitud.CANCELADA);
            solicitudRepository.save(s);
        } else {
            throw new RuntimeException("Solicitud no encontrada con ID: " + id);
        }
    }
    
    /**
     * Asignar técnico a solicitud
     */
    @Transactional
    public Solicitud asignarTecnico(Long idSolicitud, Long idTecnico) {
        Optional<Solicitud> solicitudOpt = solicitudRepository.findById(idSolicitud);
        
        if (solicitudOpt.isPresent()) {
            Solicitud solicitud = solicitudOpt.get();
            
            // Verificar que la solicitud esté en estado PENDIENTE
            if (solicitud.getEstadoSolicitud() != EstadoSolicitud.PENDIENTE) {
                throw new RuntimeException("Solo se pueden asignar técnicos a solicitudes pendientes");
            }
            
            solicitud.setIdTecnico(idTecnico);
            solicitud.setEstadoSolicitud(EstadoSolicitud.ASIGNADA);
            
            return solicitudRepository.save(solicitud);
        }
        
        throw new RuntimeException("Solicitud no encontrada con ID: " + idSolicitud);
    }
    
    /**
     * Cambiar estado de solicitud
     */
    @Transactional
    public Solicitud cambiarEstado(Long id, EstadoSolicitud nuevoEstado) {
        Optional<Solicitud> solicitudOpt = solicitudRepository.findById(id);
        
        if (solicitudOpt.isPresent()) {
            Solicitud solicitud = solicitudOpt.get();
            
            // Validar transición de estado
            if (esTransicionValida(solicitud.getEstadoSolicitud(), nuevoEstado)) {
                solicitud.setEstadoSolicitud(nuevoEstado);
                return solicitudRepository.save(solicitud);
            } else {
                throw new RuntimeException("Transición de estado inválida de " + 
                    solicitud.getEstadoSolicitud() + " a " + nuevoEstado);
            }
        }
        
        throw new RuntimeException("Solicitud no encontrada con ID: " + id);
    }
    
    /**
     * Obtener solicitudes por cliente
     */
    public List<Solicitud> obtenerSolicitudesPorCliente(Long idCliente) {
        return solicitudRepository.findByIdCliente(idCliente);
    }
    
    /**
     * Obtener solicitudes por estado
     */
    public List<Solicitud> obtenerSolicitudesPorEstado(EstadoSolicitud estado) {
        return solicitudRepository.findByEstadoSolicitud(estado);
    }
    
    /**
     * Validar transición de estado
     */
    private boolean esTransicionValida(EstadoSolicitud estadoActual, EstadoSolicitud nuevoEstado) {
        return switch (estadoActual) {
            case PENDIENTE -> nuevoEstado == EstadoSolicitud.ASIGNADA || nuevoEstado == EstadoSolicitud.CANCELADA;
            case ASIGNADA -> nuevoEstado == EstadoSolicitud.EN_PROCESO || nuevoEstado == EstadoSolicitud.CANCELADA;
            case EN_PROCESO -> nuevoEstado == EstadoSolicitud.COMPLETADA || nuevoEstado == EstadoSolicitud.CANCELADA;
            case COMPLETADA, CANCELADA -> false; // Estados finales
        };
    }
}