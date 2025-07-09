package com.homefixer.maestrias.controller;

import com.homefixer.maestrias.assembler.MaestriaModelAssembler;
import com.homefixer.maestrias.model.*;
import com.homefixer.maestrias.service.MaestriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/maestrias")
@RequiredArgsConstructor
@Tag(name = "Maestrías", description = "Gestión de maestrías y certificaciones de técnicos")
public class MaestriaController {
    
    private final MaestriaService maestriaService;
    private final MaestriaModelAssembler modelAssembler;
    
    // 1. Calcular nueva reputación
    @PostMapping("/reputacion/calcular/{idTecnico}")
    @Operation(summary = "Calcular nueva reputación", description = "Calcula la nueva reputación de un técnico basada en valoraciones")
    @ApiResponse(responseCode = "200", description = "Reputación calculada exitosamente")
    public ResponseEntity<EntityModel<Reputacion>> calcularReputacion(
            @Parameter(description = "ID del técnico") @PathVariable Long idTecnico,
            @Parameter(description = "Categoría del servicio") @RequestParam CategoriaServicio categoria,
            @Parameter(description = "Nueva valoración recibida") @RequestParam BigDecimal valoracion) {
        
        Reputacion reputacion = maestriaService.calcularNuevaReputacion(idTecnico, categoria, valoracion);
        return ResponseEntity.ok(modelAssembler.toModelReputacion(reputacion));
    }
    
    // 2. Subir certificación
    @PostMapping("/certificaciones")
    @Operation(summary = "Subir certificación", description = "Permite a un técnico subir una nueva certificación")
    @ApiResponse(responseCode = "201", description = "Certificación subida exitosamente")
    public ResponseEntity<EntityModel<Certificacion>> subirCertificacion(
            @Parameter(description = "Datos de la certificación") @RequestBody CertificacionRequest request) {
        
        Certificacion certificacion = maestriaService.subirCertificacion(
            request.getIdTecnico(),
            request.getCategoria(),
            request.getTipoCertificacion(),
            request.getObservaciones()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(modelAssembler.toModelCertificacion(certificacion));
    }
    
    // 3. Validar certificación
    @PutMapping("/certificaciones/{id}/validar")
    @Operation(summary = "Validar certificación", description = "Aprobar o rechazar una certificación pendiente")
    @ApiResponse(responseCode = "200", description = "Certificación validada exitosamente")
    public ResponseEntity<EntityModel<Certificacion>> validarCertificacion(
            @Parameter(description = "ID de la certificación") @PathVariable Long id,
            @Parameter(description = "Datos de validación") @RequestBody ValidacionRequest request) {
        
        Certificacion certificacion = maestriaService.validarCertificacion(
            id, 
            request.isAprobada(), 
            request.getObservaciones()
        );
        
        return ResponseEntity.ok(modelAssembler.toModelCertificacion(certificacion));
    }
    
    // 4. Obtener insignias de un técnico
    @GetMapping("/insignias/{idTecnico}")
    @Operation(summary = "Obtener insignias", description = "Obtiene todas las insignias de maestría de un técnico")
    @ApiResponse(responseCode = "200", description = "Insignias obtenidas exitosamente")
    public ResponseEntity<CollectionModel<EntityModel<InsigniaMaestria>>> obtenerInsignias(
            @Parameter(description = "ID del técnico") @PathVariable Long idTecnico) {
        
        List<InsigniaMaestria> insignias = maestriaService.obtenerInsigniasTecnico(idTecnico);
        return ResponseEntity.ok(modelAssembler.toCollectionModelInsignias(insignias));
    }
    
    // 5. Obtener reputación de un técnico
    @GetMapping("/reputacion/{idTecnico}")
    @Operation(summary = "Obtener reputación", description = "Obtiene la reputación de un técnico por categorías")
    @ApiResponse(responseCode = "200", description = "Reputación obtenida exitosamente")
    public ResponseEntity<CollectionModel<EntityModel<Reputacion>>> obtenerReputacion(
            @Parameter(description = "ID del técnico") @PathVariable Long idTecnico) {
        
        List<Reputacion> reputaciones = maestriaService.obtenerReputacionTecnico(idTecnico);
        return ResponseEntity.ok(modelAssembler.toCollectionModelReputaciones(reputaciones));
    }
    
    // 6. Obtener técnicos con maestría en una categoría
    @GetMapping("/tecnicos/categoria/{categoria}")
    @Operation(summary = "Técnicos con maestría", description = "Obtiene técnicos que tienen maestría en una categoría específica")
    @ApiResponse(responseCode = "200", description = "Técnicos obtenidos exitosamente")
    public ResponseEntity<CollectionModel<EntityModel<InsigniaMaestria>>> obtenerTecnicosConMaestria(
            @Parameter(description = "Categoría del servicio") @PathVariable CategoriaServicio categoria) {
        
        List<InsigniaMaestria> insignias = maestriaService.obtenerTecnicosConMaestria(categoria);
        return ResponseEntity.ok(modelAssembler.toCollectionModelInsignias(insignias));
    }
    
    // DTOs para requests
    public static class CertificacionRequest {
        private Long idTecnico;
        private CategoriaServicio categoria;
        private String tipoCertificacion;
        private String observaciones;
        
        // Getters y setters
        public Long getIdTecnico() { return idTecnico; }
        public void setIdTecnico(Long idTecnico) { this.idTecnico = idTecnico; }
        public CategoriaServicio getCategoria() { return categoria; }
        public void setCategoria(CategoriaServicio categoria) { this.categoria = categoria; }
        public String getTipoCertificacion() { return tipoCertificacion; }
        public void setTipoCertificacion(String tipoCertificacion) { this.tipoCertificacion = tipoCertificacion; }
        public String getObservaciones() { return observaciones; }
        public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    }
    
    public static class ValidacionRequest {
        private boolean aprobada;
        private String observaciones;
        
        // Getters y setters
        public boolean isAprobada() { return aprobada; }
        public void setAprobada(boolean aprobada) { this.aprobada = aprobada; }
        public String getObservaciones() { return observaciones; }
        public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    }
}