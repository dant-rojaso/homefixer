package com.homefixer.autenticacion.repository;

import com.homefixer.autenticacion.model.Sesion; // Importa entidad Sesion
import org.springframework.data.jpa.repository.JpaRepository; // Interfaz base
import org.springframework.data.jpa.repository.Query; // Para consultas personalizadas
import org.springframework.data.repository.query.Param; // Para parámetros
import org.springframework.stereotype.Repository; // Anotación repositorio
import java.time.LocalDateTime; // Para fechas
import java.util.List; // Para listas
import java.util.Optional; // Para opcionales

@Repository // Marca como repositorio Spring
public interface SesionRepository extends JpaRepository<Sesion, Long> {
    // Hereda operaciones CRUD básicas
    
    // Buscar sesión por token
    Optional<Sesion> findByTokenSesion(String tokenSesion); // Sesión específica por token
    
    // Buscar sesiones por usuario
    List<Sesion> findByIdUsuario(Long idUsuario); // Todas las sesiones de un usuario
    
    // Buscar sesiones activas de un usuario
    List<Sesion> findByIdUsuarioAndEstado(Long idUsuario, Sesion.EstadoSesion estado); // Por usuario y estado
    
    // Buscar sesión activa por token
    Optional<Sesion> findByTokenSesionAndEstado(String tokenSesion, Sesion.EstadoSesion estado); // Token + estado
    
    // Buscar sesiones por estado
    List<Sesion> findByEstado(Sesion.EstadoSesion estado); // Filtrar por estado
    
    // Consulta personalizada: sesiones activas que necesitan renovación
    @Query("SELECT s FROM Sesion s WHERE s.estado = 'ACTIVA' AND s.fechaUltimoAcceso < :fechaLimite")
    List<Sesion> encontrarSesionesInactivas(@Param("fechaLimite") LocalDateTime fechaLimite); // Sesiones sin actividad
    
    // Sesiones por IP
    List<Sesion> findByIpCliente(String ipCliente); // Todas las sesiones desde una IP
    
    // Contar sesiones activas por usuario
    long countByIdUsuarioAndEstado(Long idUsuario, Sesion.EstadoSesion estado); // Cuántas sesiones activas
    
    // Verificar si usuario tiene sesión activa
    boolean existsByIdUsuarioAndEstado(Long idUsuario, Sesion.EstadoSesion estado); // true si tiene sesión activa
    
    // Buscar última sesión de usuario
    Optional<Sesion> findTopByIdUsuarioOrderByFechaInicioDesc(Long idUsuario); // Sesión más reciente
}