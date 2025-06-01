package com.homefixer.solicitudes.controller;

import com.homefixer.solicitudes.model.Solicitud; // Importa entidad Solicitud
import com.homefixer.solicitudes.service.SolicitudService; // Importa servicio
import lombok.RequiredArgsConstructor; // Constructor automático
import lombok.extern.slf4j.Slf4j; // Logger automático
import org.springframework.http.ResponseEntity; // Para respuestas HTTP
import org.springframework.web.bind.annotation.*; // Anotaciones REST
import java.util.List; // Para listas
import java.util.Optional; // Para opcionales

@RestController // Marca como controlador REST
@RequestMapping("/api/solicitudes") // URL base: /api/solicitudes
@RequiredArgsConstructor // Constructor automático
@Slf4j // Logger automático
public class SolicitudController {
    
    private final SolicitudService solicitudService; // Servicio inyectado
    
    // GET /api/solicitudes - Obtener todas las solicitudes
    @GetMapping
    public ResponseEntity<List<Solicitud>> obtenerTodas() {
        log.info("📋 GET /api/solicitudes - Obteniendo todas las solicitudes"); // Log request
        
        List<Solicitud> solicitudes = solicitudService.obtenerTodas(); // Llama servicio
        
        log.info("✅ Se encontraron {} solicitudes", solicitudes.size()); // Log resultado
        return ResponseEntity.ok(solicitudes); // Retorna 200 OK
    }
    
    // GET /api/solicitudes/{id} - Obtener solicitud por ID
    @GetMapping("/{id}")
    public ResponseEntity<Solicitud> obtenerPorId(@PathVariable Long id) {
        log.info("🔍 GET /api/solicitudes/{} - Buscando solicitud", id); // Log request
        
        Optional<Solicitud> solicitud = solicitudService.buscarPorId(id); // Busca solicitud
        
        if (solicitud.isPresent()) {
            log.info("✅ Solicitud encontrada ID: {}", id); // Log éxito
            return ResponseEntity.ok(solicitud.get()); // Retorna 200 OK
        } else {
            log.warn("❌ Solicitud no encontrada ID: {}", id); // Log error
            return ResponseEntity.notFound().build(); // Retorna 404
        }
    }
    
    // POST /api/solicitudes - Crear nueva solicitud
    @PostMapping
    public ResponseEntity<Solicitud> crearSolicitud(@RequestBody Solicitud solicitud) {
        log.info("✅ POST /api/solicitudes - Creando solicitud para cliente: {}", solicitud.getIdCliente()); // Log request
        
        try {
            Solicitud solicitudCreada = solicitudService.crearSolicitud(solicitud); // Crea solicitud
            log.info("✅ Solicitud creada con ID: {}", solicitudCreada.getIdSolicitud()); // Log éxito
            return ResponseEntity.ok(solicitudCreada); // Retorna 200 OK
        } catch (Exception e) {
            log.error("❌ Error creando solicitud: {}", e.getMessage()); // Log error
            return ResponseEntity.badRequest().build(); // Retorna 400
        }
    }
    
    // GET /api/solicitudes/cliente/{idCliente} - Solicitudes de un cliente
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Solicitud>> buscarPorCliente(@PathVariable Long idCliente) {
        log.info("🔍 GET /api/solicitudes/cliente/{} - Solicitudes del cliente", idCliente); // Log request
        
        List<Solicitud> solicitudes = solicitudService.buscarPorCliente(idCliente); // Busca por cliente
        
        log.info("✅ Se encontraron {} solicitudes del cliente {}", solicitudes.size(), idCliente); // Log resultado
        return ResponseEntity.ok(solicitudes); // Retorna 200 OK
    }
    
    // GET /api/solicitudes/estado/{estado} - Solicitudes por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Solicitud>> buscarPorEstado(@PathVariable String estado) {
        log.info("🔍 GET /api/solicitudes/estado/{} - Buscando por estado", estado); // Log request
        
        try {
            Solicitud.EstadoSolicitud estadoEnum = Solicitud.EstadoSolicitud.valueOf(estado.toUpperCase()); // Convierte a enum
            List<Solicitud> solicitudes = solicitudService.buscarPorEstado(estadoEnum); // Busca por estado
            
            log.info("✅ Se encontraron {} solicitudes con estado {}", solicitudes.size(), estado); // Log resultado
            return ResponseEntity.ok(solicitudes); // Retorna 200 OK
        } catch (Exception e) {
            log.error("❌ Estado inválido: {}", estado); // Log error
            return ResponseEntity.badRequest().build(); // Retorna 400
        }
    }
    
    // GET /api/solicitudes/disponibles/{especialidad} - Solicitudes disponibles
    @GetMapping("/disponibles/{especialidad}")
    public ResponseEntity<List<Solicitud>> obtenerDisponibles(@PathVariable String especialidad) {
        log.info("🎯 GET /api/solicitudes/disponibles/{} - Solicitudes disponibles", especialidad); // Log request
        
        List<Solicitud> disponibles = solicitudService.obtenerDisponibles(especialidad); // Busca disponibles
        
        log.info("✅ Se encontraron {} solicitudes disponibles de {}", disponibles.size(), especialidad); // Log resultado
        return ResponseEntity.ok(disponibles); // Retorna 200 OK
    }
    
    // PUT /api/solicitudes/{id}/estado/{estado} - Cambiar estado
    @PutMapping("/{id}/estado/{estado}")
    public ResponseEntity<Solicitud> cambiarEstado(@PathVariable Long id, @PathVariable String estado) {
        log.info("🔄 PUT /api/solicitudes/{}/estado/{} - Cambiando estado", id, estado); // Log request
        
        try {
            Solicitud.EstadoSolicitud nuevoEstado = Solicitud.EstadoSolicitud.valueOf(estado.toUpperCase()); // Convierte a enum
            Solicitud solicitudActualizada = solicitudService.cambiarEstado(id, nuevoEstado); // Cambia estado
            log.info("✅ Estado cambiado exitosamente"); // Log éxito
            return ResponseEntity.ok(solicitudActualizada); // Retorna 200 OK
        } catch (Exception e) {
            log.error("❌ Error cambiando estado: {}", e.getMessage()); // Log error
            return ResponseEntity.badRequest().build(); // Retorna 400
        }
    }
    
    // PUT /api/solicitudes/{id} - Actualizar solicitud
    @PutMapping("/{id}")
    public ResponseEntity<Solicitud> actualizarSolicitud(@PathVariable Long id, @RequestBody Solicitud solicitud) {
        log.info("📝 PUT /api/solicitudes/{} - Actualizando solicitud", id); // Log request
        
        try {
            Solicitud solicitudActualizada = solicitudService.actualizarSolicitud(id, solicitud); // Actualiza solicitud
            log.info("✅ Solicitud actualizada exitosamente"); // Log éxito
            return ResponseEntity.ok(solicitudActualizada); // Retorna 200 OK
        } catch (Exception e) {
            log.error("❌ Error actualizando solicitud: {}", e.getMessage()); // Log error
            return ResponseEntity.badRequest().build(); // Retorna 400
        }
    }
    
    // PUT /api/solicitudes/{id}/cancelar - Cancelar solicitud
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Solicitud> cancelarSolicitud(@PathVariable Long id) {
        log.info("❌ PUT /api/solicitudes/{}/cancelar - Cancelando solicitud", id); // Log request
        
        try {
            Solicitud solicitudCancelada = solicitudService.cancelarSolicitud(id); // Cancela solicitud
            log.info("✅ Solicitud cancelada exitosamente"); // Log éxito
            return ResponseEntity.ok(solicitudCancelada); // Retorna 200 OK
        } catch (Exception e) {
            log.error("❌ Error cancelando solicitud: {}", e.getMessage()); // Log error
            return ResponseEntity.badRequest().build(); // Retorna 400
        }
    }
}