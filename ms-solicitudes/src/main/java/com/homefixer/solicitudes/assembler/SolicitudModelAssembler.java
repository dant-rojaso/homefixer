package com.homefixer.solicitudes.assembler;

import com.homefixer.solicitudes.controller.SolicitudController;
import com.homefixer.solicitudes.model.EstadoSolicitud;
import com.homefixer.solicitudes.model.Solicitud;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Assembler para añadir enlaces HATEOAS a las solicitudes
 */
@Component
public class SolicitudModelAssembler implements RepresentationModelAssembler<Solicitud, EntityModel<Solicitud>> {
    
    @Override
    public EntityModel<Solicitud> toModel(Solicitud solicitud) {
        EntityModel<Solicitud> solicitudModel = EntityModel.of(solicitud);
        
        // Enlaces base
        solicitudModel.add(linkTo(methodOn(SolicitudController.class)
            .obtenerSolicitudPorId(solicitud.getIdSolicitud())).withSelfRel());
        
        solicitudModel.add(linkTo(methodOn(SolicitudController.class)
            .obtenerTodasLasSolicitudes()).withRel("solicitudes"));
        
        // Enlaces condicionales según estado
        switch (solicitud.getEstadoSolicitud()) {
            case PENDIENTE -> {
                // Puede asignar técnico
                solicitudModel.add(linkTo(methodOn(SolicitudController.class)
                    .asignarTecnico(solicitud.getIdSolicitud(), null)).withRel("asignar-tecnico"));
                
                // Puede cancelar
                solicitudModel.add(linkTo(methodOn(SolicitudController.class)
                    .cancelarSolicitud(solicitud.getIdSolicitud())).withRel("cancelar"));
            }
            case ASIGNADA -> {
                // Puede iniciar trabajo
                solicitudModel.add(linkTo(methodOn(SolicitudController.class)
                    .cambiarEstado(solicitud.getIdSolicitud(), EstadoSolicitud.EN_PROCESO))
                    .withRel("iniciar-trabajo"));
                
                // Puede cancelar
                solicitudModel.add(linkTo(methodOn(SolicitudController.class)
                    .cancelarSolicitud(solicitud.getIdSolicitud())).withRel("cancelar"));
            }
            case EN_PROCESO -> {
                // Puede completar
                solicitudModel.add(linkTo(methodOn(SolicitudController.class)
                    .cambiarEstado(solicitud.getIdSolicitud(), EstadoSolicitud.COMPLETADA))
                    .withRel("completar"));
                
                // Puede cancelar
                solicitudModel.add(linkTo(methodOn(SolicitudController.class)
                    .cancelarSolicitud(solicitud.getIdSolicitud())).withRel("cancelar"));
            }
            case COMPLETADA, CANCELADA -> {
                // Estados finales - solo enlaces informativos
                solicitudModel.add(linkTo(methodOn(SolicitudController.class)
                    .obtenerSolicitudesPorCliente(solicitud.getIdCliente()))
                    .withRel("solicitudes-cliente"));
            }
        }
        
        // Enlace para ver todas las solicitudes del cliente
        solicitudModel.add(linkTo(methodOn(SolicitudController.class)
            .obtenerSolicitudesPorCliente(solicitud.getIdCliente()))
            .withRel("solicitudes-cliente"));
        
        return solicitudModel;
    }
}