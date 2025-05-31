package com.homefixer.usuarios.repository;

import com.homefixer.usuarios.model.Tecnico; // Importa entidad Tecnico
import org.springframework.data.jpa.repository.JpaRepository; // Interfaz base
import org.springframework.data.jpa.repository.Query; // Para consultas personalizadas
import org.springframework.data.repository.query.Param; // Para parámetros
import org.springframework.stereotype.Repository; // Anotación repositorio
import java.math.BigDecimal; // Para decimales
import java.util.List; // Para listas
import java.util.Optional; // Para opcionales

@Repository // Marca como repositorio Spring
public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {
    // Hereda operaciones CRUD básicas
    
    // Buscar técnico por ID de usuario
    Optional<Tecnico> findByIdUsuario(Long idUsuario); // Técnico específico por usuario
    
    // Buscar técnicos por especialidad
    List<Tecnico> findByEspecialidad(String especialidad); // Ej: todos los plomeros
    
    // Buscar técnicos disponibles solamente
    List<Tecnico> findByEstado(Tecnico.EstadoTecnico estado); // DISPONIBLE, OCUPADO, INACTIVO
    
    // Buscar técnicos con calificación mayor a X
    List<Tecnico> findByCalificacionPromedioGreaterThanEqual(BigDecimal calificacionMinima); // Mejor calificados
    
    // Buscar técnicos con tarifa menor o igual a X
    List<Tecnico> findByTarifaHoraLessThanEqual(BigDecimal tarifaMaxima); // Dentro del presupuesto
    
    // Buscar técnicos con experiencia mínima
    List<Tecnico> findByExperienciaAnosGreaterThanEqual(Integer anosMinimos); // Técnicos experimentados
    
    // Consulta personalizada: técnicos disponibles de una especialidad con buena calificación
    @Query("SELECT t FROM Tecnico t WHERE t.especialidad = :especialidad AND t.estado = 'DISPONIBLE' AND t.calificacionPromedio >= :calificacionMin ORDER BY t.calificacionPromedio DESC")
    List<Tecnico> encontrarMejoresTecnicosDisponibles(@Param("especialidad") String especialidad, @Param("calificacionMin") BigDecimal calificacionMin); // Los mejores disponibles
    
    // Contar técnicos por especialidad
    long countByEspecialidad(String especialidad); // Cuántos hay por especialidad
    
    // Verificar si existe técnico para un usuario
    boolean existsByIdUsuario(Long idUsuario); // true si ya tiene perfil de técnico
}