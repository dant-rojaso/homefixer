package com.homefixer.autenticacion.repository;

import com.homefixer.autenticacion.model.Token; // Importa entidad Token
import org.springframework.data.jpa.repository.JpaRepository; // Interfaz base
import org.springframework.data.jpa.repository.Query; // Para consultas personalizadas
import org.springframework.data.repository.query.Param; // Para parámetros
import org.springframework.stereotype.Repository; // Anotación repositorio
import java.time.LocalDateTime; // Para fechas
import java.util.List; // Para listas
import java.util.Optional; // Para opcionales

@Repository // Marca como repositorio Spring
public interface TokenRepository extends JpaRepository<Token, Long> {
    // Hereda métodos CRUD básicos
    
    // Buscar token por string de token
    Optional<Token> findByToken(String token); // Busca token específico
    
    // Buscar tokens por usuario
    List<Token> findByIdUsuario(Long idUsuario); // Todos los tokens de un usuario
    
    // Buscar tokens activos de un usuario
    List<Token> findByIdUsuarioAndActivoTrue(Long idUsuario); // Solo tokens válidos
    
    // Buscar tokens por tipo
    List<Token> findByTipo(Token.TipoToken tipo); // Filtrar por tipo de token
    
    // Buscar token válido específico
    Optional<Token> findByTokenAndActivoTrue(String token); // Token activo específico
    
    // Buscar tokens expirados
    @Query("SELECT t FROM Token t WHERE t.fechaExpiracion < :fechaActual AND t.activo = true")
    List<Token> encontrarTokensExpirados(@Param("fechaActual") LocalDateTime fechaActual); // Tokens vencidos
    
    // Buscar tokens de un usuario por tipo
    List<Token> findByIdUsuarioAndTipo(Long idUsuario, Token.TipoToken tipo); // Usuario + tipo específico
    
    // Contar tokens activos por usuario
    long countByIdUsuarioAndActivoTrue(Long idUsuario); // Cuántos tokens activos tiene
    
    // Verificar si existe token
    boolean existsByTokenAndActivoTrue(String token); // true si el token es válido
    
    // Eliminar tokens inactivos antiguos
    void deleteByActivoFalseAndFechaCreacionBefore(LocalDateTime fecha); // Limpieza de tokens viejos
}