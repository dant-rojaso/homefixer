package com.homefixer.maestrias.assembler;

import com.homefixer.maestrias.controller.MaestriaController;
import com.homefixer.maestrias.model.Certificacion;
import com.homefixer.maestrias.model.EstadoCertificacion;
import com.homefixer.maestrias.model.InsigniaMaestria;
import com.homefixer.maestrias.model.Reputacion;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MaestriaModelAssembler {
    
    // Assembler para Reputación
    public EntityModel<Reputacion> toModelReputacion(Reputacion reputacion) {
        return EntityModel.of(reputacion)
            .add(linkTo(methodOn(MaestriaController.class)
                .obtenerReputacion(reputacion.getIdTecnico())).withSelfRel())
            .add(linkTo(methodOn(MaestriaController.class)
                .obtenerInsignias(reputacion.getIdTecnico())).withRel("insignias"))
            .add(linkTo(methodOn(MaestriaController.class)
                .calcularReputacion(reputacion.getIdTecnico(), reputacion.getCategoria(), null))
                .withRel("calcular-reputacion"));
    }
    
    // Assembler para Certificación
    public EntityModel<Certificacion> toModelCertificacion(Certificacion certificacion) {
        EntityModel<Certificacion> model = EntityModel.of(certificacion);
        
        // Enlaces básicos
        model.add(linkTo(methodOn(MaestriaController.class)
            .obtenerReputacion(certificacion.getIdTecnico())).withRel("reputacion"));
        
        // Enlaces condicionales según estado
        if (certificacion.getEstado() == EstadoCertificacion.PENDIENTE) {
            model.add(linkTo(methodOn(MaestriaController.class)
                .validarCertificacion(certificacion.getIdCertificacion(), null))
                .withRel("validar"));
        }
        
        if (certificacion.getEstado() == EstadoCertificacion.APROBADA) {
            model.add(linkTo(methodOn(MaestriaController.class)
                .obtenerInsignias(certificacion.getIdTecnico())).withRel("insignias"));
        }
        
        return model;
    }
    
    // Assembler para Insignia de Maestría
    public EntityModel<InsigniaMaestria> toModelInsignia(InsigniaMaestria insignia) {
        return EntityModel.of(insignia)
            .add(linkTo(methodOn(MaestriaController.class)
                .obtenerInsignias(insignia.getIdTecnico())).withSelfRel())
            .add(linkTo(methodOn(MaestriaController.class)
                .obtenerReputacion(insignia.getIdTecnico())).withRel("reputacion"))
            .add(linkTo(methodOn(MaestriaController.class)
                .obtenerTecnicosConMaestria(insignia.getCategoria())).withRel("tecnicos-categoria"));
    }
    
    // Collection models
    public CollectionModel<EntityModel<Reputacion>> toCollectionModelReputaciones(List<Reputacion> reputaciones) {
        List<EntityModel<Reputacion>> reputacionModels = reputaciones.stream()
            .map(this::toModelReputacion)
            .toList();
        
        return CollectionModel.of(reputacionModels);
    }
    
    public CollectionModel<EntityModel<InsigniaMaestria>> toCollectionModelInsignias(List<InsigniaMaestria> insignias) {
        List<EntityModel<InsigniaMaestria>> insigniaModels = insignias.stream()
            .map(this::toModelInsignia)
            .toList();
        
        return CollectionModel.of(insigniaModels);
    }
}
