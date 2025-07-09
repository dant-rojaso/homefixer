package com.homefixer.maestrias.repository;

import com.homefixer.maestrias.model.CategoriaServicio;
import com.homefixer.maestrias.model.Reputacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReputacionRepository extends JpaRepository<Reputacion, Long> {
    
    // Buscar reputación por técnico y categoría
    Optional<Reputacion> findByIdTecnicoAndCategoria(Long idTecnico, CategoriaServicio categoria);
    
    // Obtener todas las reputaciones de un técnico
    List<Reputacion> findByIdTecnico(Long idTecnico);
    
    // Técnicos con reputación superior a un nivel en una categoría
    @Query("SELECT r FROM Reputacion r WHERE r.categoria = :categoria AND r.nivelReputacion >= :nivelMinimo")
    List<Reputacion> findByCategoriaAndNivelReputacionGreaterThanEqual(
        @Param("categoria") CategoriaServicio categoria, 
        @Param("nivelMinimo") BigDecimal nivelMinimo
    );
}