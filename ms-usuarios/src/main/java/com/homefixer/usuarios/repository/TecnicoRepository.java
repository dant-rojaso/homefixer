package com.homefixer.usuarios.repository;

import com.homefixer.usuarios.model.Tecnico; // Modelo Técnico
import org.springframework.data.jpa.repository.JpaRepository; // Repositorio JPA base
import org.springframework.stereotype.Repository; // Marca como repositorio
import java.util.List; // Lista de técnicos
import java.util.Optional; // Técnico opcional

@Repository // Marca como componente repositorio de Spring
public interface TecnicoRepository extends JpaRepository<Tecnico, Long> { // Hereda métodos CRUD básicos
    
    Optional<Tecnico> findByEmail(String email); // Busca técnico por email único
    
    List<Tecnico> findByDisponible(Boolean disponible); // Filtra técnicos por disponibilidad
    
    List<Tecnico> findByEspecialidad(String especialidad); // Filtra técnicos por especialidad
    
    boolean existsByEmail(String email); // Verifica si existe email (para validación)
}