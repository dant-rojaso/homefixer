package com.homefixer.usuarios.repository;

import com.homefixer.usuarios.model.Cliente; // Modelo Cliente
import org.springframework.data.jpa.repository.JpaRepository; // Repositorio JPA base
import org.springframework.stereotype.Repository; // Marca como repositorio
import java.util.Optional; // Cliente opcional

@Repository // Marca como componente repositorio de Spring
public interface ClienteRepository extends JpaRepository<Cliente, Long> { // Hereda métodos CRUD básicos
    
    Optional<Cliente> findByEmail(String email); // Busca cliente por email único
    
    boolean existsByEmail(String email); // Verifica si existe email (para validación)
}