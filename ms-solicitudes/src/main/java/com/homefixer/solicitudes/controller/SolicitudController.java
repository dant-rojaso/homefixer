package com.homefixer.solicitudes.controller;

import com.homefixer.solicitudes.assembler.SolicitudModelAssembler;
import com.homefixer.solicitudes.model.EstadoSolicitud;
import com.homefixer.solicitudes.model.Solicitud;
import com.homefixer.solicitudes.service.SolicitudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Controlador REST para gestión de solicitudes
 */
@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
@Tag(name = "Solicitudes", description = "API para gestión de solicitudes de servicios técnicos")
public class SolicitudController {
    
    private final SolicitudService solicitudService;
    private final SolicitudModelAssembler assembler;
    
    /**
     * Obtener todas las solicitudes
     */
    @GetMapping
    @Operation(summary = "Obtener todas las solicitudes", description = "Retorna lista de todas las solicitudes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de solicitudes obtenida exitosamente")
    })
    public CollectionModel<EntityModel<Solicitud>> obtenerTodasLasSolicitudes() {
        List<EntityModel<Solicitud>> solicitudes = solicitudService.obtenerTodasLasSolicitudes()
            .stream()
            .map(assembler::toModel)
            .toList();
        
        return CollectionModel.of(solicitudes)
            .add(linkTo(methodOn(SolicitudController.class).obtenerTodasLasSolicitudes()).withSelfRel());
    }
    
    /**
     * Obtener solicitud por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener solicitud por ID", description = "Retorna una solicitud específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud encontrada"),
        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    public ResponseEntity<EntityModel<Solicitud>> obtenerSolicitudPorId(
        @Parameter(description = "ID de la solicitud") @PathVariable Long id) {
        
        Optional<Solicitud> solicitud = solicitudService.obtenerSolicitudPorId(id);
        
        if (solicitud.isPresent()) {
            return ResponseEntity.ok(assembler.toModel(solicitud.get()));
        }
        
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Crear nueva solicitud
     */
    @PostMapping
    @Operation(summary = "Crear nueva solicitud", description = "Crea una nueva solicitud de servicio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Solicitud creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos")
    })
    public ResponseEntity<EntityModel<Solicitud>> crearSolicitud(
        @Valid @RequestBody Solicitud solicitud) {
        
        Solicitud nuevaSolicitud = solicitudService.crearSolicitud(solicitud);
        
        return ResponseEntity
            .created(linkTo(methodOn(SolicitudController.class).obtenerSolicitudPorId(nuevaSolicitud.getIdSolicitud())).toUri())
            .body(assembler.toModel(nuevaSolicitud));
    }
    
    /**
     * Actualizar solicitud existente
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar solicitud", description = "Actualiza una solicitud existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos")
    })
    public ResponseEntity<EntityModel<Solicitud>> actualizarSolicitud(
        @Parameter(description = "ID de la solicitud") @PathVariable Long id,
        @Valid @RequestBody Solicitud solicitudActualizada) {
        
        try {
            Solicitud solicitud = solicitudService.actualizarSolicitud(id, solicitudActualizada);
            return ResponseEntity.ok(assembler.toModel(solicitud));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Cancelar solicitud
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar solicitud", description = "Cancela una solicitud existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Solicitud cancelada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    public ResponseEntity<Void> cancelarSolicitud(
        @Parameter(description = "ID de la solicitud") @PathVariable Long id) {
        
        try {
            solicitudService.cancelarSolicitud(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Obtener solicitudes por cliente
     */
    @GetMapping("/cliente/{idCliente}")
    @Operation(summary = "Obtener solicitudes por cliente", description = "Retorna solicitudes de un cliente específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitudes del cliente obtenidas exitosamente")
    })
    public CollectionModel<EntityModel<Solicitud>> obtenerSolicitudesPorCliente(
        @Parameter(description = "ID del cliente") @PathVariable Long idCliente) {
        
        List<EntityModel<Solicitud>> solicitudes = solicitudService.obtenerSolicitudesPorCliente(idCliente)
            .stream()
            .map(assembler::toModel)
            .toList();
        
        return CollectionModel.of(solicitudes)
            .add(linkTo(methodOn(SolicitudController.class).obtenerSolicitudesPorCliente(idCliente)).withSelfRel())
            .add(linkTo(methodOn(SolicitudController.class).obtenerTodasLasSolicitudes()).withRel("solicitudes"));
    }
    
    /**
     * Obtener solicitudes por estado
     */
    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener solicitudes por estado", description = "Retorna solicitudes filtradas por estado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitudes filtradas por estado obtenidas exitosamente")
    })
    public CollectionModel<EntityModel<Solicitud>> obtenerSolicitudesPorEstado(
        @Parameter(description = "Estado de las solicitudes") @PathVariable EstadoSolicitud estado) {
        
        List<EntityModel<Solicitud>> solicitudes = solicitudService.obtenerSolicitudesPorEstado(estado)
            .stream()
            .map(assembler::toModel)
            .toList();
        
        return CollectionModel.of(solicitudes)
            .add(linkTo(methodOn(SolicitudController.class).obtenerSolicitudesPorEstado(estado)).withSelfRel())
            .add(linkTo(methodOn(SolicitudController.class).obtenerTodasLasSolicitudes()).withRel("solicitudes"));
    }
    
    /**
     * Asignar técnico a solicitud
     */
    @PutMapping("/{id}/asignar/{idTecnico}")
    @Operation(summary = "Asignar técnico", description = "Asigna un técnico a una solicitud pendiente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Técnico asignado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada"),
        @ApiResponse(responseCode = "400", description = "No se puede asignar técnico a solicitud en este estado")
    })
    public ResponseEntity<EntityModel<Solicitud>> asignarTecnico(
        @Parameter(description = "ID de la solicitud") @PathVariable Long id,
        @Parameter(description = "ID del técnico") @PathVariable Long idTecnico) {
        
        try {
            Solicitud solicitud = solicitudService.asignarTecnico(id, idTecnico);
            return ResponseEntity.ok(assembler.toModel(solicitud));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Cambiar estado de solicitud
     */
    @PutMapping("/{id}/estado/{estado}")
    @Operation(summary = "Cambiar estado", description = "Cambia el estado de una solicitud")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado cambiado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada"),
        @ApiResponse(responseCode = "400", description = "Cambio de estado inválido")
    })
    public ResponseEntity<EntityModel<Solicitud>> cambiarEstado(
        @Parameter(description = "ID de la solicitud") @PathVariable Long id,
        @Parameter(description = "Nuevo estado") @PathVariable EstadoSolicitud estado) {
        
        try {
            Solicitud solicitud = solicitudService.cambiarEstado(id, estado);
            return ResponseEntity.ok(assembler.toModel(solicitud));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}