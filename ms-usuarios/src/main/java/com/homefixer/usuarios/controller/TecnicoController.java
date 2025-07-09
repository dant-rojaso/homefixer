package com.homefixer.usuarios.controller;

import com.homefixer.usuarios.model.Tecnico; // Modelo Técnico
import com.homefixer.usuarios.service.TecnicoService; // Servicio Técnico
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
@RequestMapping("/api/tecnicos") // Mapeo base de URLs
@Tag(name = "Técnicos", description = "API para gestión de técnicos") // Documentación Swagger
@RequiredArgsConstructor // Genera constructor con campos final
public class TecnicoController {
    
    private final TecnicoService tecnicoService; // Servicio inyectado
    
    @GetMapping // GET /api/tecnicos
    @Operation(summary = "Obtener todos los técnicos") // Documentación del endpoint
    public ResponseEntity<CollectionModel<EntityModel<Tecnico>>> obtenerTodosLosTecnicos() {
        var tecnicos = tecnicoService.obtenerTodosLosTecnicos() // Obtiene lista de técnicos
            .stream() // Convierte a stream
            .map(tecnico -> EntityModel.of(tecnico) // Envuelve cada técnico en EntityModel
                .add(linkTo(methodOn(TecnicoController.class).obtenerTecnicoPorId(tecnico.getIdTecnico())).withSelfRel()) // Link self
                .add(linkTo(methodOn(TecnicoController.class).obtenerTodosLosTecnicos()).withRel("tecnicos"))) // Link collection
            .collect(Collectors.toList()); // Colecta resultados
        
        return ResponseEntity.ok( // Respuesta HTTP 200
            CollectionModel.of(tecnicos) // Colección con enlaces
                .add(linkTo(methodOn(TecnicoController.class).obtenerTodosLosTecnicos()).withSelfRel()) // Link self collection
        );
    }
    
    @GetMapping("/disponibles") // GET /api/tecnicos/disponibles
    @Operation(summary = "Obtener técnicos disponibles") // Documentación del endpoint
    public ResponseEntity<CollectionModel<EntityModel<Tecnico>>> obtenerTecnicosDisponibles() {
        var tecnicos = tecnicoService.obtenerTecnicosDisponibles() // Obtiene técnicos disponibles
            .stream() // Convierte a stream
            .map(tecnico -> EntityModel.of(tecnico) // Envuelve cada técnico en EntityModel
                .add(linkTo(methodOn(TecnicoController.class).obtenerTecnicoPorId(tecnico.getIdTecnico())).withSelfRel()) // Link self
                .add(linkTo(methodOn(TecnicoController.class).obtenerTodosLosTecnicos()).withRel("tecnicos"))) // Link collection
            .collect(Collectors.toList()); // Colecta resultados
        
        return ResponseEntity.ok( // Respuesta HTTP 200
            CollectionModel.of(tecnicos) // Colección con enlaces
                .add(linkTo(methodOn(TecnicoController.class).obtenerTecnicosDisponibles()).withSelfRel()) // Link self collection
        );
    }
    
    @GetMapping("/{idTecnico}") // GET /api/tecnicos/{id}
    @Operation(summary = "Obtener técnico por ID") // Documentación del endpoint
    public ResponseEntity<EntityModel<Tecnico>> obtenerTecnicoPorId(@PathVariable Long idTecnico) {
        return tecnicoService.obtenerTecnicoPorId(idTecnico) // Busca técnico por ID
            .map(tecnico -> EntityModel.of(tecnico) // Si existe, envuelve en EntityModel
                .add(linkTo(methodOn(TecnicoController.class).obtenerTecnicoPorId(idTecnico)).withSelfRel()) // Link self
                .add(linkTo(methodOn(TecnicoController.class).obtenerTodosLosTecnicos()).withRel("tecnicos"))) // Link collection
            .map(ResponseEntity::ok) // Convierte a ResponseEntity 200
            .orElse(ResponseEntity.notFound().build()); // Si no existe, devuelve 404
    }
    
    @PostMapping // POST /api/tecnicos
    @Operation(summary = "Crear nuevo técnico") // Documentación del endpoint
    public ResponseEntity<EntityModel<Tecnico>> crearTecnico(@Valid @RequestBody Tecnico tecnico) {
        var nuevoTecnico = tecnicoService.crearTecnico(tecnico); // Crea nuevo técnico
        return ResponseEntity.status(HttpStatus.CREATED) // Respuesta HTTP 201
            .body(EntityModel.of(nuevoTecnico) // Envuelve técnico creado
                .add(linkTo(methodOn(TecnicoController.class).obtenerTecnicoPorId(nuevoTecnico.getIdTecnico())).withSelfRel()) // Link self
                .add(linkTo(methodOn(TecnicoController.class).obtenerTodosLosTecnicos()).withRel("tecnicos"))); // Link collection
    }
    
    @PutMapping("/{idTecnico}") // PUT /api/tecnicos/{id}
    @Operation(summary = "Actualizar técnico") // Documentación del endpoint
    public ResponseEntity<EntityModel<Tecnico>> actualizarTecnico(@PathVariable Long idTecnico, @Valid @RequestBody Tecnico tecnico) {
        try {
            var tecnicoActualizado = tecnicoService.actualizarTecnico(idTecnico, tecnico); // Actualiza técnico
            return ResponseEntity.ok(EntityModel.of(tecnicoActualizado) // Respuesta HTTP 200
                .add(linkTo(methodOn(TecnicoController.class).obtenerTecnicoPorId(idTecnico)).withSelfRel()) // Link self
                .add(linkTo(methodOn(TecnicoController.class).obtenerTodosLosTecnicos()).withRel("tecnicos"))); // Link collection
        } catch (RuntimeException e) { // Si no existe
            return ResponseEntity.notFound().build(); // Devuelve HTTP 404
        }
    }
    
    @DeleteMapping("/{idTecnico}") // DELETE /api/tecnicos/{id}
    @Operation(summary = "Eliminar técnico") // Documentación del endpoint
    public ResponseEntity<Void> eliminarTecnico(@PathVariable Long idTecnico) {
        try {
            tecnicoService.eliminarTecnico(idTecnico); // Elimina técnico
            return ResponseEntity.noContent().build(); // Respuesta HTTP 204
        } catch (RuntimeException e) { // Si no existe
            return ResponseEntity.notFound().build(); // Devuelve HTTP 404
        }
    }
}