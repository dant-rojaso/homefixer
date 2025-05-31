package com.homefixer.usuarios.controller;

import com.homefixer.usuarios.model.Cliente; // Importa entidad Cliente
import com.homefixer.usuarios.service.ClienteService; // Importa servicio
import lombok.RequiredArgsConstructor; // Constructor automático
import lombok.extern.slf4j.Slf4j; // Logger automático
import org.springframework.http.ResponseEntity; // Para respuestas HTTP
import org.springframework.web.bind.annotation.*; // Anotaciones REST
import java.util.List; // Para listas
import java.util.Optional; // Para opcionales

@RestController // Marca como controlador REST
@RequestMapping("/api/clientes") // URL base: /api/clientes
@RequiredArgsConstructor // Constructor automático
@Slf4j // Logger automático
public class ClienteController {
    
    private final ClienteService clienteService; // Servicio inyectado
    
    // GET /api/clientes - Obtener todos los clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> obtenerTodos() {
        log.info("📋 GET /api/clientes - Obteniendo todos los clientes"); // Log request
        
        List<Cliente> clientes = clienteService.obtenerTodos(); // Llama servicio
        
        log.info("✅ Se encontraron {} clientes", clientes.size()); // Log resultado
        return ResponseEntity.ok(clientes); // Retorna 200 OK
    }
    
    // GET /api/clientes/{id} - Obtener cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable Long id) {
        log.info("🔍 GET /api/clientes/{} - Buscando cliente", id); // Log request
        
        Optional<Cliente> cliente = clienteService.buscarPorId(id); // Busca cliente
        
        if (cliente.isPresent()) {
            log.info("✅ Cliente encontrado ID: {}", id); // Log éxito
            return ResponseEntity.ok(cliente.get()); // Retorna 200 OK
        } else {
            log.warn("❌ Cliente no encontrado ID: {}", id); // Log error
            return ResponseEntity.notFound().build(); // Retorna 404
        }
    }
    
    // POST /api/clientes - Crear perfil de cliente
    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente cliente) {
        log.info("✅ POST /api/clientes - Creando perfil cliente para usuario: {}", cliente.getIdUsuario()); // Log request
        
        try {
            Cliente clienteCreado = clienteService.crearCliente(cliente); // Crea cliente
            log.info("✅ Perfil de cliente creado ID: {}", clienteCreado.getIdCliente()); // Log éxito
            return ResponseEntity.ok(clienteCreado); // Retorna 200 OK
        } catch (Exception e) {
            log.error("❌ Error creando cliente: {}", e.getMessage()); // Log error
            return ResponseEntity.badRequest().build(); // Retorna 400
        }
    }
    
    // GET /api/clientes/usuario/{idUsuario} - Buscar cliente por ID usuario
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Cliente> buscarPorUsuario(@PathVariable Long idUsuario) {
        log.info("🔍 GET /api/clientes/usuario/{} - Buscando por usuario", idUsuario); // Log request
        
        Optional<Cliente> cliente = clienteService.buscarPorIdUsuario(idUsuario); // Busca por usuario
        
        if (cliente.isPresent()) {
            log.info("✅ Cliente encontrado para usuario: {}", idUsuario); // Log éxito
            return ResponseEntity.ok(cliente.get()); // Retorna 200 OK
        } else {
            log.warn("❌ No se encontró cliente para usuario: {}", idUsuario); // Log error
            return ResponseEntity.notFound().build(); // Retorna 404
        }
    }
    
    // GET /api/clientes/ciudad/{ciudad} - Buscar clientes por ciudad
    @GetMapping("/ciudad/{ciudad}")
    public ResponseEntity<List<Cliente>> buscarPorCiudad(@PathVariable String ciudad) {
        log.info("🏙️ GET /api/clientes/ciudad/{} - Buscando por ciudad", ciudad); // Log request
        
        List<Cliente> clientes = clienteService.buscarPorCiudad(ciudad); // Busca por ciudad
        
        log.info("✅ Se encontraron {} clientes en {}", clientes.size(), ciudad); // Log resultado
        return ResponseEntity.ok(clientes); // Retorna 200 OK
    }
    
    // GET /api/clientes/premium - Obtener clientes premium
    @GetMapping("/premium")
    public ResponseEntity<List<Cliente>> obtenerPremium() {
        log.info("💎 GET /api/clientes/premium - Obteniendo clientes premium"); // Log request
        
        List<Cliente> clientesPremium = clienteService.obtenerClientesPremium(); // Busca premium
        
        log.info("✅ Se encontraron {} clientes premium", clientesPremium.size()); // Log resultado
        return ResponseEntity.ok(clientesPremium); // Retorna 200 OK
    }
    
    // PUT /api/clientes/{id}/incrementar-servicios - Incrementar servicios
    @PutMapping("/{id}/incrementar-servicios")
    public ResponseEntity<Cliente> incrementarServicios(@PathVariable Long id) {
        log.info("📈 PUT /api/clientes/{}/incrementar-servicios", id); // Log request
        
        try {
            Cliente clienteActualizado = clienteService.incrementarServicios(id); // Incrementa servicios
            log.info("✅ Servicios incrementados para cliente: {}", id); // Log éxito
            return ResponseEntity.ok(clienteActualizado); // Retorna 200 OK
        } catch (Exception e) {
            log.error("❌ Error incrementando servicios: {}", e.getMessage()); // Log error
            return ResponseEntity.badRequest().build(); // Retorna 400
        }
    }
    
    // PUT /api/clientes/{id} - Actualizar cliente
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        log.info("📝 PUT /api/clientes/{} - Actualizando cliente", id); // Log request
        
        try {
            Cliente clienteActualizado = clienteService.actualizarCliente(id, cliente); // Actualiza cliente
            log.info("✅ Cliente actualizado exitosamente"); // Log éxito
            return ResponseEntity.ok(clienteActualizado); // Retorna 200 OK
        } catch (Exception e) {
            log.error("❌ Error actualizando cliente: {}", e.getMessage()); // Log error
            return ResponseEntity.badRequest().build(); // Retorna 400
        }
    }
}