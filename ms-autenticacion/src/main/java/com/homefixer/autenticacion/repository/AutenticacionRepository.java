package com.homefixer.autenticacion.repository;

import com.homefixer.autenticacion.model.Autenticacion;
import com.homefixer.autenticacion.model.TipoUsuario;
import com.homefixer.autenticacion.model.EstadoSesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutenticacionRepository extends JpaRepository<Autenticacion, Long> {
    
    Optional<Autenticacion> findByEmail(String email);
    
    Optional<Autenticacion> findByTokenSesion(String tokenSesion);
    
    List<Autenticacion> findByTipoUsuario(TipoUsuario tipoUsuario);
    
    List<Autenticacion> findByEstadoSesion(EstadoSesion estadoSesion);
    
    @Query("SELECT a FROM Autenticacion a WHERE a.idUsuario = :idUsuario")
    Optional<Autenticacion> findByIdUsuario(@Param("idUsuario") Long idUsuario);
    
    boolean existsByEmail(String email);
}