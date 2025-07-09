package com.homefixer.solicitudes.repository;

import com.homefixer.solicitudes.model.EstadoSolicitud;
import com.homefixer.solicitudes.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para operaciones CRUD de solicitudes
 */
@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    
    /**
     * Buscar solicitudes por cliente
     */
    List<Solicitud> findByIdCliente(Long idCliente);
    
    /**
     * Buscar solicitudes por estado
     */
    List<Solicitud> findByEstadoSolicitud(EstadoSolicitud estado);
    
    /**
     * Buscar solicitudes por técnico
     */
    List<Solicitud> findByIdTecnico(Long idTecnico);
    
    /**
     * Buscar solicitudes por tipo de servicio
     */
    List<Solicitud> findByTipoServicio(String tipoServicio);
    
    /**
     * Verificar si existe una solicitud para un cliente específico
     */
    boolean existsByIdCliente(Long idCliente);
}