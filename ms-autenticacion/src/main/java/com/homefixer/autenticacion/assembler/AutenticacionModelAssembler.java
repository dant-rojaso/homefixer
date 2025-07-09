package com.homefixer.autenticacion.assembler;

import com.homefixer.autenticacion.model.Autenticacion;
import com.homefixer.autenticacion.model.EstadoSesion;
import com.homefixer.autenticacion.controller.AutenticacionController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class AutenticacionModelAssembler implements RepresentationModelAssembler<Autenticacion, EntityModel<Autenticacion>> {
    
    @Override
    public EntityModel<Autenticacion> toModel(Autenticacion autenticacion) {
        EntityModel<Autenticacion> autenticacionModel = EntityModel.of(autenticacion);
        
        // Enlaces base
        autenticacionModel.add(linkTo(methodOn(AutenticacionController.class)
                .obtenerAutenticacionPorId(autenticacion.getIdAutenticacion())).withSelfRel());
        autenticacionModel.add(linkTo(methodOn(AutenticacionController.class)
                .obtenerTodasLasAutenticaciones()).withRel("autenticaciones"));
        
        // Enlaces condicionales según estado
        if (autenticacion.getEstadoSesion() == EstadoSesion.INACTIVA) {
            autenticacionModel.add(linkTo(methodOn(AutenticacionController.class)
                    .iniciarSesion(null)).withRel("iniciar-sesion"));
        }
        
        if (autenticacion.getEstadoSesion() == EstadoSesion.ACTIVA) {
            autenticacionModel.add(linkTo(methodOn(AutenticacionController.class)
                    .cerrarSesion(null)).withRel("cerrar-sesion"));
        }
        
        return autenticacionModel;
    }
}
