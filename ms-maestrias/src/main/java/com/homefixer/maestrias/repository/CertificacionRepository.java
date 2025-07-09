package com.homefixer.maestrias.repository;

import com.homefixer.maestrias.model.CategoriaServicio;
import com.homefixer.maestrias.model.Certificacion;
import com.homefixer.maestrias.model.EstadoCertificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificacionRepository extends JpaRepository<Certificacion, Long> {
    
    // Buscar certificaciones por técnico
    List<Certificacion> findByIdTecnico(Long idTecnico);
    
    // Buscar certificaciones por estado
    List<Certificacion> findByEstado(EstadoCertificacion estado);
    
    // Buscar certificaciones por técnico y categoría
    List<Certificacion> findByIdTecnicoAndCategoria(Long idTecnico, CategoriaServicio categoria);
    
    // Buscar certificaciones aprobadas por técnico y categoría
    List<Certificacion> findByIdTecnicoAndCategoriaAndEstado(
        Long idTecnico, 
        CategoriaServicio categoria, 
        EstadoCertificacion estado
    );
}