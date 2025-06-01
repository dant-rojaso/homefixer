package com.homefixer.asignaciones.controller;

import com.homefixer.asignaciones.model.Asignacion; // Importa entidad Asignacion
import com.homefixer.asignaciones.service.AsignacionService; // IMPORT CORREGIDO - con ruta completa
import lombok.RequiredArgsConstructor; // Constructor automático
import org.springframework.http.ResponseEntity; // Para respuestas HTTP
import org.springframework.web.bind.annotation.*; // Anotaciones REST
import java.util.List; // Para listas
import java.util.Optional; // Para opcionales

@RestController // Controlador REST
@RequestMapping("/api/asignaciones") // URL base
@RequiredArgsConstructor // Constructor automático
public class AsignacionController {
    
    private final AsignacionService asignacionService; // Servicio inyectado
    
    // GET todas las asignaciones
    @GetMapping
    public ResponseEntity<List<Asignacion>> obtenerTodas() {
        System.out.println("📋 GET /api/asignaciones"); // Log request
        List<Asignacion> asignaciones = asignacionService.obtenerTodas(); // Llama servicio
        return ResponseEntity.ok(asignaciones); // Retorna 200 OK
    }
    
    // GET asignación por ID
    @GetMapping("/{id}")
    public ResponseEntity<Asignacion> obtenerPorId(@PathVariable Long id) {
        System.out.println("🔍 GET /api/asignaciones/" + id); // Log request
        
        Optional<Asignacion> asignacion = asignacionService.buscarPorId(id); // Busca asignación
        
        if (asignacion.isPresent()) {
            return ResponseEntity.ok(asignacion.get()); // Retorna 200 OK
        } else {
            return ResponseEntity.notFound().build(); // Retorna 404
        }
    }
    
    // POST crear asignación
    @PostMapping
    public ResponseEntity<Asignacion> crearAsignacion(@RequestBody Asignacion asignacion) {
        System.out.println("✅ POST /api/asignaciones"); // Log request
        
        try {
            Asignacion asignacionCreada = asignacionService.crearAsignacion(asignacion); // Crea asignación
            return ResponseEntity.ok(asignacionCreada); // Retorna 200 OK
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Retorna 400
        }
    }
    
    // GET asignaciones por técnico
    @GetMapping("/tecnico/{idTecnico}")
    public ResponseEntity<List<Asignacion>> buscarPorTecnico(@PathVariable Long idTecnico) {
        System.out.println("🔧 GET /api/asignaciones/tecnico/" + idTecnico); // Log request
        List<Asignacion> asignaciones = asignacionService.buscarPorTecnico(idTecnico); // Busca por técnico
        return ResponseEntity.ok(asignaciones); // Retorna 200 OK
    }
    
    // PUT aceptar asignación
    @PutMapping("/{id}/aceptar")
    public ResponseEntity<Asignacion> aceptarAsignacion(@PathVariable Long id) {
        System.out.println("✅ PUT /api/asignaciones/" + id + "/aceptar"); // Log request
        
        try {
            Asignacion asignacionAceptada = asignacionService.aceptarAsignacion(id); // Acepta asignación
            return ResponseEntity.ok(asignacionAceptada); // Retorna 200 OK
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Retorna 400
        }
    }
    
    // PUT rechazar asignación
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<Asignacion> rechazarAsignacion(@PathVariable Long id, @RequestBody String motivo) {
        System.out.println("❌ PUT /api/asignaciones/" + id + "/rechazar"); // Log request
        
        try {
            Asignacion asignacionRechazada = asignacionService.rechazarAsignacion(id, motivo); // Rechaza asignación
            return ResponseEntity.ok(asignacionRechazada); // Retorna 200 OK
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Retorna 400
        }
    }
}