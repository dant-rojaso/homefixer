package com.homefixer.maestrias.service;

import com.homefixer.maestrias.model.*;
import com.homefixer.maestrias.repository.CertificacionRepository;
import com.homefixer.maestrias.repository.InsigniaMaestriaRepository;
import com.homefixer.maestrias.repository.ReputacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MaestriaService {
    
    private final ReputacionRepository reputacionRepository;
    private final CertificacionRepository certificacionRepository;
    private final InsigniaMaestriaRepository insigniaRepository;
    
    // Calcular nueva reputación basada en valoraciones
    @Transactional
    public Reputacion calcularNuevaReputacion(Long idTecnico, CategoriaServicio categoria, 
                                             BigDecimal nuevaValoracion) {
        Optional<Reputacion> reputacionExistente = reputacionRepository
            .findByIdTecnicoAndCategoria(idTecnico, categoria);
        
        Reputacion reputacion;
        if (reputacionExistente.isPresent()) {
            reputacion = reputacionExistente.get();
            // Calcular nueva reputación promedio
            BigDecimal totalValoraciones = reputacion.getNivelReputacion()
                .multiply(BigDecimal.valueOf(reputacion.getValoracionesRecibidas()))
                .add(nuevaValoracion);
            int nuevasCantidad = reputacion.getValoracionesRecibidas() + 1;
            BigDecimal nuevoPromedio = totalValoraciones.divide(BigDecimal.valueOf(nuevasCantidad), 2, 
                java.math.RoundingMode.HALF_UP);
            
            reputacion.setNivelReputacion(nuevoPromedio);
            reputacion.setValoracionesRecibidas(nuevasCantidad);
        } else {
            reputacion = new Reputacion();
            reputacion.setIdTecnico(idTecnico);
            reputacion.setCategoria(categoria);
            reputacion.setNivelReputacion(nuevaValoracion);
            reputacion.setValoracionesRecibidas(1);
        }
        
        reputacion = reputacionRepository.save(reputacion);
        
        // Verificar si puede desbloquear nueva certificación
        verificarDesbloquearCertificacion(idTecnico, categoria, reputacion.getNivelReputacion());
        
        return reputacion;
    }
    
    // Subir nueva certificación
    @Transactional
    public Certificacion subirCertificacion(Long idTecnico, CategoriaServicio categoria, 
                                          String tipoCertificacion, String observaciones) {
        // Verificar si el técnico tiene nivel suficiente
        Optional<Reputacion> reputacion = reputacionRepository
            .findByIdTecnicoAndCategoria(idTecnico, categoria);
        
        if (reputacion.isEmpty() || reputacion.get().getNivelReputacion().compareTo(BigDecimal.valueOf(4.0)) < 0) {
            throw new RuntimeException("Nivel de reputación insuficiente para subir certificación");
        }
        
        Certificacion certificacion = new Certificacion();
        certificacion.setIdTecnico(idTecnico);
        certificacion.setCategoria(categoria);
        certificacion.setTipoCertificacion(tipoCertificacion);
        certificacion.setObservaciones(observaciones);
        certificacion.setEstado(EstadoCertificacion.PENDIENTE);
        
        return certificacionRepository.save(certificacion);
    }
    
    // Validar certificación
    @Transactional
    public Certificacion validarCertificacion(Long idCertificacion, boolean aprobada, String observaciones) {
        Certificacion certificacion = certificacionRepository.findById(idCertificacion)
            .orElseThrow(() -> new RuntimeException("Certificación no encontrada"));
        
        if (aprobada) {
            certificacion.setEstado(EstadoCertificacion.APROBADA);
            certificacion.setFechaValidacion(LocalDateTime.now());
            certificacion.setObservaciones(observaciones);
            
            // Otorgar insignia de maestría
            otorgarInsigniaMaestria(certificacion.getIdTecnico(), certificacion.getCategoria());
        } else {
            certificacion.setEstado(EstadoCertificacion.RECHAZADA);
            certificacion.setFechaValidacion(LocalDateTime.now());
            certificacion.setObservaciones(observaciones);
        }
        
        return certificacionRepository.save(certificacion);
    }
    
    // Obtener insignias de un técnico
    public List<InsigniaMaestria> obtenerInsigniasTecnico(Long idTecnico) {
        return insigniaRepository.findByIdTecnicoAndActivaTrue(idTecnico);
    }
    
    // Obtener reputación de un técnico
    public List<Reputacion> obtenerReputacionTecnico(Long idTecnico) {
        return reputacionRepository.findByIdTecnico(idTecnico);
    }
    
    // Obtener técnicos con maestría en una categoría
    public List<InsigniaMaestria> obtenerTecnicosConMaestria(CategoriaServicio categoria) {
        return insigniaRepository.findByCategoriaAndActivaTrue(categoria);
    }
    
    // Método privado para verificar desbloqueo de certificación
    private void verificarDesbloquearCertificacion(Long idTecnico, CategoriaServicio categoria, 
                                                  BigDecimal nivelReputacion) {
        // Si el nivel es 4.0 o superior, puede subir certificación
        if (nivelReputacion.compareTo(BigDecimal.valueOf(4.0)) >= 0) {
            // Lógica adicional si se requiere notificar al técnico
            System.out.println("Técnico " + idTecnico + " puede subir certificación en " + categoria);
        }
    }
    
    // Método privado para otorgar insignia de maestría
    private void otorgarInsigniaMaestria(Long idTecnico, CategoriaServicio categoria) {
        // Verificar si ya tiene insignia activa en esta categoría
        List<InsigniaMaestria> insigniasExistentes = insigniaRepository
            .findByIdTecnicoAndCategoriaAndActivaTrue(idTecnico, categoria);
        
        if (insigniasExistentes.isEmpty()) {
            InsigniaMaestria insignia = new InsigniaMaestria();
            insignia.setIdTecnico(idTecnico);
            insignia.setCategoria(categoria);
            insignia.setNivelMaestria(NivelMaestria.BRONCE); // Nivel inicial
            insignia.setActiva(true);
            
            insigniaRepository.save(insignia);
        }
    }
}
