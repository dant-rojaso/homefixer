package com.homefixer.asignaciones.repository;

import com.homefixer.asignaciones.model.Asignacion; // Importa entidad Asignacion
import org.springframework.data.jpa.repository.JpaRepository; // Interfaz base
import org.springframework.data.jpa.repository.Query; // Para consultas personalizadas
import org.springframework.data.repository.query.Param; // Para parámetros
import org.springframework.stereotype.Repository; // Anotación repositorio
import java.util.List; // Para listas
import java.util.Optional; // Para opcionales

@Repository // Marca como repositorio Spring
public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {
    // Hereda métodos CRUD básicos
    
    // Buscar asignación por solicitud (relación 1:1)
    Optional<Asignacion> findByIdSolicitud(Long idSolicitud); // Una solicitud tiene máximo una asignación
    
    // Buscar asignaciones por técnico
    List<Asignacion> findByIdTecnico(Long idTecnico); // Todas las asignaciones de un técnico
    
    // Buscar asignaciones por cliente
    List<Asignacion> findByIdCliente(Long idCliente); // Todas las asignaciones de un cliente
    
    // Buscar asignaciones por estado
    List<Asignacion> findByEstado(Asignacion.EstadoAsignacion estado); // Filtrar por estado
    
    // Buscar asignaciones activas de un técnico
    List<Asignacion> findByIdTecnicoAndEstadoIn(Long idTecnico, List<Asignacion.EstadoAsignacion> estados); // Técnico ocupado
    
    // Buscar asignaciones pendientes para un técnico
    List<Asignacion> findByIdTecnicoAndEstado(Long idTecnico, Asignacion.EstadoAsignacion estado); // Propuestas pendientes
    
    // Consulta personalizada: asignaciones activas del técnico
    @Query("SELECT a FROM Asignacion a WHERE a.idTecnico = :idTecnico AND a.estado IN ('ACEPTADA', 'EN_CAMINO', 'EN_SERVICIO')")
    List<Asignacion> encontrarAsignacionesActivas(@Param("idTecnico") Long idTecnico); // Trabajos actuales
    
    // Consulta personalizada: historial de asignaciones completadas
    @Query("SELECT a FROM Asignacion a WHERE a.estado = 'COMPLETADA' ORDER BY a.fechaFinServicio DESC")
    List<Asignacion> obtenerHistorialCompletadas(); // Trabajos terminados
    
    // Contar asignaciones por estado
    long countByEstado(Asignacion.EstadoAsignacion estado); // Estadísticas por estado
    
    // Verificar si técnico tiene asignaciones activas
    boolean existsByIdTecnicoAndEstadoIn(Long idTecnico, List<Asignacion.EstadoAsignacion> estados); // ¿Está ocupado?
    
    // Verificar si solicitud ya tiene asignación
    boolean existsByIdSolicitud(Long idSolicitud); // ¿Ya fue asignada?
}