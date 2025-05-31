package com.homefixer.usuarios.repository;

import com.homefixer.usuarios.model.Cliente; // Importa entidad Cliente
import org.springframework.data.jpa.repository.JpaRepository; // Interfaz base
import org.springframework.data.jpa.repository.Query; // Para consultas JPQL
import org.springframework.data.repository.query.Param; // Para parámetros
import org.springframework.stereotype.Repository; // Anotación de repositorio
import java.util.List; // Para listas
import java.util.Optional; // Para resultados opcionales

@Repository // Marca como repositorio de Spring
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Hereda métodos básicos de JpaRepository
    
    // Buscar cliente por ID de usuario (relación 1:1)
    Optional<Cliente> findByIdUsuario(Long idUsuario); // Cliente específico por usuario
    
    // Buscar clientes por ciudad
    List<Cliente> findByCiudad(String ciudad); // Todos los clientes de una ciudad
    
    // Buscar clientes por región
    List<Cliente> findByRegion(String region); // Clientes de una región específica
    
    // Buscar clientes por tipo (REGULAR o PREMIUM)
    List<Cliente> findByTipoCliente(Cliente.TipoCliente tipoCliente); // Filtrar por tipo
    
    // Buscar clientes con más de X servicios contratados
    List<Cliente> findByServiciosContratadosGreaterThan(Integer cantidad); // Clientes frecuentes
    
    // Consulta personalizada: clientes premium con más de X servicios
    @Query("SELECT c FROM Cliente c WHERE c.tipoCliente = 'PREMIUM' AND c.serviciosContratados >= :minServicios")
    List<Cliente> encontrarClientesPremiumActivos(@Param("minServicios") Integer minServicios); // Clientes VIP
    
    // Contar clientes por tipo
    long countByTipoCliente(Cliente.TipoCliente tipoCliente); // Estadísticas por tipo
    
    // Verificar si existe cliente para un usuario
    boolean existsByIdUsuario(Long idUsuario); // true si ya tiene perfil de cliente
}