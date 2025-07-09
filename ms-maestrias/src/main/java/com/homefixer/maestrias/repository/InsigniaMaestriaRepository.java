package com.homefixer.maestrias.repository;

import com.homefixer.maestrias.model.CategoriaServicio;
import com.homefixer.maestrias.model.InsigniaMaestria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsigniaMaestriaRepository extends JpaRepository<InsigniaMaestria, Long> {
    
    // Buscar insignias por técnico
    List<InsigniaMaestria> findByIdTecnico(Long idTecnico);
    
    // Buscar insignias activas por técnico
    List<InsigniaMaestria> findByIdTecnicoAndActivaTrue(Long idTecnico);
    
    // Buscar técnicos con insignia en una categoría específica
    List<InsigniaMaestria> findByCategoriaAndActivaTrue(CategoriaServicio categoria);
    
    // Buscar insignia específica por técnico y categoría
    List<InsigniaMaestria> findByIdTecnicoAndCategoriaAndActivaTrue(Long idTecnico, CategoriaServicio categoria);
}