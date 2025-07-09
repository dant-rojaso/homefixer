package com.homefixer.autenticacion.controller;

import com.homefixer.autenticacion.model.Autenticacion;
import com.homefixer.autenticacion.model.TipoUsuario;
import com.homefixer.autenticacion.model.EstadoSesion;
import com.homefixer.autenticacion.service.AutenticacionService;
import com.homefixer.autenticacion.assembler.AutenticacionModelAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/autenticacion")
@Tag(name = "Autenticación", description = "API para gestión de autenticación de usuarios")
public class AutenticacionController {
    
    @Autowired
    private AutenticacionService autenticacionService;
    
    @Autowired
    private AutenticacionModelAssembler autenticacionAssembler;
    
    @GetMapping
    @Operation(summary = "Obtener todas las autenticaciones")
    public CollectionModel<EntityModel<Autenticacion>> obtenerTodasLasAutenticaciones() {
        List<Autenticacion> autenticaciones = autenticacionService.obtenerTodasLasAutenticaciones();
        return autenticacionAssembler.toCollectionModel(autenticaciones);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener autenticación por ID")
    public ResponseEntity<EntityModel<Autenticacion>> obtenerAutenticacionPorId(@PathVariable Long id) {
        Optional<Autenticacion> autenticacion = autenticacionService.obtenerAutenticacionPorId(id);
        return autenticacion.map(auth -> ResponseEntity.ok(autenticacionAssembler.toModel(auth)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Crear nueva autenticación")
    public ResponseEntity<EntityModel<Autenticacion>> crearAutenticacion(@Valid @RequestBody Autenticacion autenticacion) {
        try {
            Autenticacion nuevaAutenticacion = autenticacionService.crearAutenticacion(autenticacion);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(autenticacionAssembler.toModel(nuevaAutenticacion));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar autenticación")
    public ResponseEntity<EntityModel<Autenticacion>> actualizarAutenticacion(
            @PathVariable Long id, @Valid @RequestBody Autenticacion autenticacion) {
        Optional<Autenticacion> autenticacionActualizada = autenticacionService.actualizarAutenticacion(id, autenticacion);
        return autenticacionActualizada.map(auth -> ResponseEntity.ok(autenticacionAssembler.toModel(auth)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar autenticación")
    public ResponseEntity<Void> eliminarAutenticacion(@PathVariable Long id) {
        if (autenticacionService.eliminarAutenticacion(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión")
    public ResponseEntity<EntityModel<Autenticacion>> iniciarSesion(@RequestBody Map<String, String> credenciales) {
        String email = credenciales.get("email");
        String contrasena = credenciales.get("contrasena");
        
        Optional<Autenticacion> autenticacion = autenticacionService.iniciarSesion(email, contrasena);
        return autenticacion.map(auth -> ResponseEntity.ok(autenticacionAssembler.toModel(auth)))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    
    @PostMapping("/logout")
    @Operation(summary = "Cerrar sesión")
    public ResponseEntity<Void> cerrarSesion(@RequestBody Map<String, String> token) {
        String tokenSesion = token.get("tokenSesion");
        if (autenticacionService.cerrarSesion(tokenSesion)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
    
    @GetMapping("/tipo/{tipoUsuario}")
    @Operation(summary = "Filtrar por tipo de usuario")
    public CollectionModel<EntityModel<Autenticacion>> obtenerPorTipoUsuario(@PathVariable TipoUsuario tipoUsuario) {
        List<Autenticacion> autenticaciones = autenticacionService.obtenerPorTipoUsuario(tipoUsuario);
        return autenticacionAssembler.toCollectionModel(autenticaciones);
    }
    
    @GetMapping("/estado/{estadoSesion}")
    @Operation(summary = "Filtrar por estado de sesión")
    public CollectionModel<EntityModel<Autenticacion>> obtenerPorEstadoSesion(@PathVariable EstadoSesion estadoSesion) {
        List<Autenticacion> autenticaciones = autenticacionService.obtenerPorEstadoSesion(estadoSesion);
        return autenticacionAssembler.toCollectionModel(autenticaciones);
    }
    
    @GetMapping("/validar/{tokenSesion}")
    @Operation(summary = "Validar token de sesión")
    public ResponseEntity<Map<String, Boolean>> validarToken(@PathVariable String tokenSesion) {
        boolean esValido = autenticacionService.validarToken(tokenSesion);
        return ResponseEntity.ok(Map.of("valido", esValido));
    }
}
