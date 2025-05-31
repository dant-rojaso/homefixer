package com.homefixer.usuarios.controller;

import com.homefixer.usuarios.model.Tecnico; // Importa entidad Tecnico
import com.homefixer.usuarios.service.TecnicoService; // Importa servicio
import lombok.RequiredArgsConstructor; // Constructor automático
import lombok.extern.slf4j.Slf4j; // Logger automático
import org.springframework.http.ResponseEntity; // Para respuestas HTTP
import org.springframework.web.bind.annotation.*; // Anotaciones REST
import java.math.BigDecimal; // Para decimales
import java.util.List; // Para listas
import java.util.Optional; // Para opcionales

@RestController // Marca como controlador REST
@RequestMapping("/api/tecnicos") // URL base: /api/tecnicos
@RequiredArgsConstructor // Constructor automático
@Slf4j // Logger automático
public class TecnicoController {
    
    private final TecnicoService tecnicoService; // Servicio inyectado
    
    // GET /api/tecnicos - Obtener todos los técnicos
    @GetMapping
    public ResponseEntity<List<Tecnico>> obtenerTodos() {
        log.info("📋 GET /api/tecnicos - Obteniendo todos los técnicos"); // Log request
        
        List<Tecnico> tecnicos = tecnicoService.obtenerTodos(); // Llama servicio
        
        log.info("✅ Se encontraron {} técnicos", tecnicos.size()); // Log resultado
        return ResponseEntity.ok(tecnicos); // Retorna 200 OK
    }
    
    // GET /api/tecnicos/{id} - Obtener técnico por ID
    @GetMapping("/{id}")
    public ResponseEntity<Tecnico> obtenerPorId(@PathVariable Long id) {
        log.info("🔍 GET /api/tecnicos/{} - Buscando técnico", id); // Log request
        
        Optional<Tecnico> tecnico = tecnicoService.buscarPorId(id); // Busca técnico
        
        if (tecnico.isPresent()) {
            log.info("✅ Técnico encontrado ID: {}", id); // Log éxito
            return ResponseEntity.ok(tecnico.get()); // Retorna 200 OK
        } else {
            log.warn("❌ Técnico no encontrado ID: {}", id); // Log error
            return ResponseEntity.notFound().build(); // Retorna 404
        }
    }
    
    // POST /api/tecnicos - Crear perfil de técnico
    @PostMapping
    public ResponseEntity<Tecnico> crearTecnico(@RequestBody Tecnico tecnico) {
        log.info("✅ POST /api/tecnicos - Creando perfil técnico para usuario: {}", tecnico.getIdUsuario()); // Log request
        
        try {
            Tecnico tecnicoCreado = tecnicoService.crearTecnico(tecnico); // Crea técnico
            log.info("✅ Perfil de técnico creado ID: {}", tecnicoCreado.getIdTecnico()); // Log éxito
            return ResponseEntity.ok(tecnicoCreado); // Retorna 200 OK
        } catch (Exception e) {
            log.error("❌ Error creando técnico: {}", e.getMessage()); // Log error
            return ResponseEntity.badRequest().build(); // Retorna 400
        }
    }
    
    // GET /api/tecnicos/especialidad/{especialidad} - Buscar por especialidad
    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<Tecnico>> buscarPorEspecialidad(@PathVariable String especialidad) {
        log.info("🔧 GET /api/tecnicos/especialidad/{} - Buscando por especialidad", especialidad); // Log request
        
        List<Tecnico> tecnicos = tecnicoService.buscarPorEspecialidad(especialidad); // Busca por especialidad
        
        log.info("✅ Se encontraron {} técnicos de {}", tecnicos.size(), especialidad); // Log resultado
        return ResponseEntity.ok(tecnicos); // Retorna 200 OK
    }
    
    // GET /api/tecnicos/disponibles - Obtener técnicos disponibles
    @GetMapping("/disponibles")
    public ResponseEntity<List<Tecnico>> obtenerDisponibles() {
        log.info("✅ GET /api/tecnicos/disponibles - Obteniendo disponibles"); // Log request
        
        List<Tecnico> disponibles = tecnicoService.obtenerDisponibles(); // Busca disponibles
        
        log.info("✅ Se encontraron {} técnicos disponibles", disponibles.size()); // Log resultado
        return ResponseEntity.ok(disponibles); // Retorna 200 OK
    }
    
    // GET /api/tecnicos/mejores/{especialidad} - Mejores técnicos por especialidad
    @GetMapping("/mejores/{especialidad}")
    public ResponseEntity<List<Tecnico>> obtenerMejores(@PathVariable String especialidad) {
        log.info("⭐ GET /api/tecnicos/mejores/{} - Obteniendo mejores", especialidad); // Log request
        
        List<Tecnico> mejores = tecnicoService.obtenerMejoresTecnicos(especialidad); // Busca mejores
        
        log.info("✅ Se encontraron {} mejores técnicos", mejores.size()); // Log resultado
        return ResponseEntity.ok(mejores); // Retorna 200 OK
    }
    
    // PUT /api/tecnicos/{id}/estado/{estado} - Cambiar estado
    @PutMapping("/{id}/estado/{estado}")
    public ResponseEntity<Tecnico> cambiarEstado(@PathVariable Long id, @PathVariable String estado) {
        log.info("🔄 PUT /api/tecnicos/{}/estado/{} - Cambiando estado", id, estado); // Log request
        
        try {
            Tecnico.EstadoTecnico nuevoEstado = Tecnico.EstadoTecnico.valueOf(estado.toUpperCase()); // Convierte string a enum
            Tecnico tecnicoActualizado = tecnicoService.cambiarEstado(id, nuevoEstado); // Cambia estado
            log.info("✅ Estado cambiado exitosamente"); // Log éxito
            return ResponseEntity.ok(tecnicoActualizado); // Retorna 200 OK
        } catch (Exception e) {
            log.error("❌ Error cambiando estado: {}", e.getMessage()); // Log error
            return ResponseEntity.badRequest().build(); // Retorna 400
        }
    }
    
    // PUT /api/tecnicos/{id}/completar-servicio - Completar servicio
    @PutMapping("/{id}/completar-servicio")
    public ResponseEntity<Tecnico> completarServicio(@PathVariable Long id) {
        log.info("🎯 PUT /api/tecnicos/{}/completar-servicio", id); // Log request
        
        try {
            Tecnico tecnicoActualizado = tecnicoService.completarServicio(id); // Completa servicio
            log.info("✅ Servicio completado exitosamente"); // Log éxito
            return ResponseEntity.ok(tecnicoActualizado); // Retorna 200 OK
        } catch (Exception e) {
            log.error("❌ Error completando servicio: {}", e.getMessage()); // Log error
            return ResponseEntity.badRequest().build(); // Retorna 400
        }
    }
    
    // PUT /api/tecnicos/{id}/calificacion/{calificacion} - Actualizar calificación
    @PutMapping("/{id}/calificacion/{calificacion}")
    public ResponseEntity<Tecnico> actualizarCalificacion(@PathVariable Long id, @PathVariable String calificacion) {
        log.info("⭐ PUT /api/tecnicos/{}/calificacion/{}", id, calificacion); // Log request
        
        try {
            BigDecimal nuevaCalificacion = new BigDecimal(calificacion); // Convierte string a BigDecimal
            Tecnico tecnicoActualizado = tecnicoService.actualizarCalificacion(id, nuevaCalificacion); // Actualiza calificación
            log.info("✅ Calificación actualizada exitosamente"); // Log éxito
            return ResponseEntity.ok(tecnicoActualizado); // Retorna 200 OK
        } catch (Exception e) {
            log.error("❌ Error actualizando calificación: {}", e.getMessage()); // Log error
            return ResponseEntity.badRequest().build(); // Retorna 400
        }
    }
}