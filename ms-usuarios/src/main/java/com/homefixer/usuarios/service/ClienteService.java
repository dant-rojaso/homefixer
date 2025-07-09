package com.homefixer.usuarios.service;

import com.homefixer.usuarios.model.Cliente; // Modelo Cliente
import com.homefixer.usuarios.repository.ClienteRepository; // Repositorio Cliente
import lombok.RequiredArgsConstructor; // Inyección por constructor
import org.springframework.stereotype.Service; // Marca como servicio
import java.util.List; // Lista de clientes
import java.util.Optional; // Cliente opcional

@Service // Marca como servicio de Spring
@RequiredArgsConstructor // Genera constructor con campos final/non-null
public class ClienteService {
    
    private final ClienteRepository clienteRepository; // Repositorio inyectado
    
    public List<Cliente> obtenerTodosLosClientes() { // Obtiene todos los clientes
        return clienteRepository.findAll(); // Busca todos en BD
    }
    
    public Optional<Cliente> obtenerClientePorId(Long idCliente) { // Busca cliente por ID
        return clienteRepository.findById(idCliente); // Busca por ID en BD
    }
    
    public Cliente crearCliente(Cliente cliente) { // Crea nuevo cliente
        if (clienteRepository.existsByEmail(cliente.getEmail())) { // Verifica email único
            throw new RuntimeException("Ya existe un cliente con este email"); // Error si existe
        }
        return clienteRepository.save(cliente); // Guarda en BD
    }
    
    public Cliente actualizarCliente(Long idCliente, Cliente clienteActualizado) { // Actualiza cliente
        return clienteRepository.findById(idCliente) // Busca cliente existente
            .map(cliente -> { // Si existe
                cliente.setNombre(clienteActualizado.getNombre()); // Actualiza nombre
                cliente.setApellido(clienteActualizado.getApellido()); // Actualiza apellido
                cliente.setEmail(clienteActualizado.getEmail()); // Actualiza email
                cliente.setTelefono(clienteActualizado.getTelefono()); // Actualiza teléfono
                cliente.setDireccion(clienteActualizado.getDireccion()); // Actualiza dirección
                return clienteRepository.save(cliente); // Guarda cambios en BD
            })
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + idCliente)); // Error si no existe
    }
    
    public void eliminarCliente(Long idCliente) { // Elimina cliente
        if (!clienteRepository.existsById(idCliente)) { // Verifica si existe
            throw new RuntimeException("Cliente no encontrado con id: " + idCliente); // Error si no existe
        }
        clienteRepository.deleteById(idCliente); // Elimina de BD
    }
}