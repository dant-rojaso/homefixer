package com.homefixer.usuarios.controller;

import com.homefixer.usuarios.model.Cliente; // Modelo Cliente
import com.homefixer.usuarios.service.ClienteService; // Servicio Cliente
import io.swagger.v3.oas.annotations.Operation; // Documentación Swagger
import io.swagger.v3.oas.annotations.tags.Tag; // Agrupación Swagger
import jakarta.validation.Valid; // Validación de entrada
import lombok.RequiredArgsConstructor; // Inyección por constructor
import org.springframework.hateoas.*; // HATEOAS para navegabilidad
import org.springframework.http.*; // HTTP status y responses
import org.springframework.web.bind.annotation.*; // Anotaciones REST

import java.util.stream.Collectors; // Transformación de streams

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*; // Enlaces HATEOAS

@RestController // Marca como controlador REST
@RequestMapping("/api/clientes") // Mapeo base de URLs
@Tag(name = "Clientes", description = "API para gestión de clientes") // Documentación Swagger
@RequiredArgsConstructor // Genera constructor con campos final
public class ClienteController {
    
    private final ClienteService clienteService; // Servicio inyectado
    
    @GetMapping // GET /api/clientes
    @Operation(summary = "Obtener todos los clientes") // Documentación del endpoint
    public ResponseEntity<CollectionModel<EntityModel<Cliente>>> obtenerTodosLosClientes() {
        var clientes = clienteService.obtenerTodosLosClientes() // Obtiene lista de clientes
            .stream() // Convierte a stream
            .map(cliente -> EntityModel.of(cliente) // Envuelve cada cliente en EntityModel
                .add(linkTo(methodOn(ClienteController.class).obtenerClientePorId(cliente.getIdCliente())).withSelfRel()) // Link self
                .add(linkTo(methodOn(ClienteController.class).obtenerTodosLosClientes()).withRel("clientes"))) // Link collection
            .collect(Collectors.toList()); // Colecta resultados
        
        return ResponseEntity.ok( // Respuesta HTTP 200
            CollectionModel.of(clientes) // Colección con enlaces
                .add(linkTo(methodOn(ClienteController.class).obtenerTodosLosClientes()).withSelfRel()) // Link self collection
        );
    }
    
    @GetMapping("/{idCliente}") // GET /api/clientes/{id}
    @Operation(summary = "Obtener cliente por ID") // Documentación del endpoint
    public ResponseEntity<EntityModel<Cliente>> obtenerClientePorId(@PathVariable Long idCliente) {
        return clienteService.obtenerClientePorId(idCliente) // Busca cliente por ID
            .map(cliente -> EntityModel.of(cliente) // Si existe, envuelve en EntityModel
                .add(linkTo(methodOn(ClienteController.class).obtenerClientePorId(idCliente)).withSelfRel()) // Link self
                .add(linkTo(methodOn(ClienteController.class).obtenerTodosLosClientes()).withRel("clientes"))) // Link collection
            .map(ResponseEntity::ok) // Convierte a ResponseEntity 200
            .orElse(ResponseEntity.notFound().build()); // Si no existe, devuelve 404
    }
    
    @PostMapping // POST /api/clientes
    @Operation(summary = "Crear nuevo cliente") // Documentación del endpoint
    public ResponseEntity<EntityModel<Cliente>> crearCliente(@Valid @RequestBody Cliente cliente) {
        var nuevoCliente = clienteService.crearCliente(cliente); // Crea nuevo cliente
        return ResponseEntity.status(HttpStatus.CREATED) // Respuesta HTTP 201
            .body(EntityModel.of(nuevoCliente) // Envuelve cliente creado
                .add(linkTo(methodOn(ClienteController.class).obtenerClientePorId(nuevoCliente.getIdCliente())).withSelfRel()) // Link self
                .add(linkTo(methodOn(ClienteController.class).obtenerTodosLosClientes()).withRel("clientes"))); // Link collection
    }
    
    @PutMapping("/{idCliente}") // PUT /api/clientes/{id}
    @Operation(summary = "Actualizar cliente") // Documentación del endpoint
    public ResponseEntity<EntityModel<Cliente>> actualizarCliente(@PathVariable Long idCliente, @Valid @RequestBody Cliente cliente) {
        try {
            var clienteActualizado = clienteService.actualizarCliente(idCliente, cliente); // Actualiza cliente
            return ResponseEntity.ok(EntityModel.of(clienteActualizado) // Respuesta HTTP 200
                .add(linkTo(methodOn(ClienteController.class).obtenerClientePorId(idCliente)).withSelfRel()) // Link self
                .add(linkTo(methodOn(ClienteController.class).obtenerTodosLosClientes()).withRel("clientes"))); // Link collection
        } catch (RuntimeException e) { // Si no existe
            return ResponseEntity.notFound().build(); // Devuelve HTTP 404
        }
    }
    
    @DeleteMapping("/{idCliente}") // DELETE /api/clientes/{id}
    @Operation(summary = "Eliminar cliente") // Documentación del endpoint
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long idCliente) {
        try {
            clienteService.eliminarCliente(idCliente); // Elimina cliente
            return ResponseEntity.noContent().build(); // Respuesta HTTP 204
        } catch (RuntimeException e) { // Si no existe
            return ResponseEntity.notFound().build(); // Devuelve HTTP 404
        }
    }
}