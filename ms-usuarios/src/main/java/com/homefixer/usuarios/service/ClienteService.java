package com.homefixer.usuarios.service;

import com.homefixer.usuarios.model.Cliente; // Importa entidad Cliente
import com.homefixer.usuarios.repository.ClienteRepository; // Importa repositorio
import lombok.RequiredArgsConstructor; // Constructor automático
import lombok.extern.slf4j.Slf4j; // Logger automático
import org.springframework.stereotype.Service; // Anotación de servicio
import java.util.List; // Para listas
import java.util.Optional; // Para opcionales

@Service // Marca como servicio Spring
@RequiredArgsConstructor // Constructor automático para dependencias
@Slf4j // Logger automático
public class ClienteService {
    
    private final ClienteRepository clienteRepository; // Repositorio inyectado automáticamente
    
    // Crear perfil de cliente
    public Cliente crearCliente(Cliente cliente) {
        log.info("✅ Creando perfil de cliente para usuario ID: {}", cliente.getIdUsuario()); // Log inicio
        
        // Verificar que no exista ya un perfil de cliente para este usuario
        if (clienteRepository.existsByIdUsuario(cliente.getIdUsuario())) {
            log.error("❌ Ya existe perfil de cliente para usuario: {}", cliente.getIdUsuario()); // Log error
            throw new RuntimeException("El usuario ya tiene perfil de cliente"); // Excepción
        }
        
        cliente.setTipoCliente(Cliente.TipoCliente.REGULAR); // Cliente regular por defecto
        cliente.setServiciosContratados(0); // Empieza con 0 servicios
        
        Cliente clienteGuardado = clienteRepository.save(cliente); // Guarda en BD
        log.info("✅ Perfil de cliente creado con ID: {}", clienteGuardado.getIdCliente()); // Log éxito
        
        return clienteGuardado; // Retorna cliente creado
    }
    
    // Buscar cliente por ID
    public Optional<Cliente> buscarPorId(Long id) {
        log.info("🔍 Buscando cliente por ID: {}", id); // Log búsqueda
        return clienteRepository.findById(id); // Busca en BD
    }
    
    // Buscar cliente por ID de usuario
    public Optional<Cliente> buscarPorIdUsuario(Long idUsuario) {
        log.info("🔍 Buscando cliente por ID usuario: {}", idUsuario); // Log búsqueda
        return clienteRepository.findByIdUsuario(idUsuario); // Busca por FK
    }
    
    // Obtener todos los clientes
    public List<Cliente> obtenerTodos() {
        log.info("📋 Obteniendo todos los clientes"); // Log consulta
        return clienteRepository.findAll(); // Retorna todos
    }
    
    // Buscar clientes por ciudad
    public List<Cliente> buscarPorCiudad(String ciudad) {
        log.info("🏙️ Buscando clientes en ciudad: {}", ciudad); // Log búsqueda
        return clienteRepository.findByCiudad(ciudad); // Filtra por ciudad
    }
    
    // Buscar clientes premium
    public List<Cliente> obtenerClientesPremium() {
        log.info("💎 Obteniendo clientes premium"); // Log consulta
        return clienteRepository.findByTipoCliente(Cliente.TipoCliente.PREMIUM); // Solo premium
    }
    
    // Incrementar servicios contratados
    public Cliente incrementarServicios(Long idCliente) {
        log.info("📈 Incrementando servicios para cliente ID: {}", idCliente); // Log incremento
        
        Cliente cliente = clienteRepository.findById(idCliente)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado")); // Busca o falla
        
        cliente.setServiciosContratados(cliente.getServiciosContratados() + 1); // Suma uno
        
        // Promocionar a premium si tiene 5 o más servicios
        if (cliente.getServiciosContratados() >= 5 && cliente.getTipoCliente() == Cliente.TipoCliente.REGULAR) {
            cliente.setTipoCliente(Cliente.TipoCliente.PREMIUM); // Promociona a premium
            log.info("🎉 Cliente promocionado a PREMIUM!"); // Log promoción
        }
        
        Cliente guardado = clienteRepository.save(cliente); // Guarda cambios
        log.info("✅ Servicios actualizados a: {}", guardado.getServiciosContratados()); // Log éxito
        
        return guardado; // Retorna cliente actualizado
    }
    
    // Actualizar cliente
    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {
        log.info("📝 Actualizando cliente ID: {}", id); // Log actualización
        
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado")); // Busca cliente
        
        // Actualiza campos editables
        cliente.setDireccion(clienteActualizado.getDireccion()); // Nueva dirección
        cliente.setCiudad(clienteActualizado.getCiudad()); // Nueva ciudad
        cliente.setRegion(clienteActualizado.getRegion()); // Nueva región
        
        Cliente guardado = clienteRepository.save(cliente); // Guarda cambios
        log.info("✅ Cliente actualizado exitosamente"); // Log éxito
        
        return guardado; // Retorna cliente actualizado
    }
}