package com.homefixer.solicitudes.repository;

import com.homefixer.solicitudes.model.Solicitud; // Importa entidad Solicitud
import org.springframework.data.jpa.repository.JpaRepository; // Interfaz base
import org.springframework.data.jpa.repository.Query; // Para consultas personalizadas
import org.springframework.data.repository.query.Param; // Para parámetros
import org.springframework.stereotype.Repository; // Anotación repositorio
import java.util.List; // Para listas

@Repository // Marca como repositorio Spring
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    // Hereda métodos CRUD básicos de JpaRepository
    
    // Buscar solicitudes por cliente
    List<Solicitud> findByIdCliente(Long idCliente); // Todas las solicitudes de un cliente
    
    // Buscar solicitudes por estado
    List<Solicitud> findByEstado(Solicitud.EstadoSolicitud estado); // Filtrar por estado
    
    // Buscar solicitudes por especialidad
    List<Solicitud> findByEspecialidadRequerida(String especialidad); // Por tipo de servicio
    
    // Buscar solicitudes por ciudad
    List<Solicitud> findByCiudad(String ciudad); // En una ciudad específica
    
    // Buscar solicitudes por prioridad
    List<Solicitud> findByPrioridad(Solicitud.PrioridadSolicitud prioridad); // Por urgencia
    
    // Consulta personalizada: solicitudes disponibles para asignar
    @Query("SELECT s FROM Solicitud s WHERE s.estado = 'PENDIENTE' AND s.especialidadRequerida = :especialidad ORDER BY s.prioridad DESC, s.fechaCreacion ASC")
    List<Solicitud> encontrarSolicitudesDisponibles(@Param("especialidad") String especialidad); // Para técnicos disponibles
    
    // Contar solicitudes por estado
    long countByEstado(Solicitud.EstadoSolicitud estado); // Estadísticas por estado
    
    // Buscar solicitudes urgentes
    List<Solicitud> findByPrioridadInOrderByFechaCreacionAsc(List<Solicitud.PrioridadSolicitud> prioridades); // Urgentes primero
}