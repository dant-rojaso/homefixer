package com.homefixer.usuarios.repository;

import com.homefixer.usuarios.model.Usuario; // Importa la entidad Usuario
import org.springframework.data.jpa.repository.JpaRepository; // Interfaz base de Spring Data
import org.springframework.data.jpa.repository.Query; // Para consultas personalizadas
import org.springframework.data.repository.query.Param; // Para parámetros en consultas
import org.springframework.stereotype.Repository; // Marca como repositorio
import java.util.List; // Para listas de resultados
import java.util.Optional; // Para resultados que pueden ser nulos

@Repository // Marca esta interfaz como repositorio de Spring
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // JpaRepository ya incluye métodos básicos: save, findAll, findById, delete
    
    // Buscar usuario por email (único)
    Optional<Usuario> findByEmail(String email); // Retorna Optional porque puede no existir
    
    // Buscar usuarios por tipo (CLIENTE o TECNICO)
    List<Usuario> findByTipo(Usuario.TipoUsuario tipo); // Lista de usuarios del mismo tipo
    
    // Buscar usuarios activos solamente
    List<Usuario> findByActivoTrue(); // Solo usuarios con activo = true
    
    // Buscar por email y password (para login básico)
    Optional<Usuario> findByEmailAndPassword(String email, String password); // Login simple
    
    // Consulta personalizada: buscar por nombre que contenga texto
    @Query("SELECT u FROM Usuario u WHERE u.nombre LIKE %:nombre%") // JPQL query
    List<Usuario> buscarPorNombreContiene(@Param("nombre") String nombre); // Busca nombres similares
    
    // Contar usuarios por tipo
    long countByTipo(Usuario.TipoUsuario tipo); // Cuenta cuántos hay de cada tipo
    
    // Verificar si existe email (para validaciones)
    boolean existsByEmail(String email); // true si el email ya está registrado
}