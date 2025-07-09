package com.homefixer.maestrias.config;

import com.github.javafaker.Faker;
import com.homefixer.maestrias.model.*;
import com.homefixer.maestrias.repository.CertificacionRepository;
import com.homefixer.maestrias.repository.InsigniaMaestriaRepository;
import com.homefixer.maestrias.repository.ReputacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    
    private final ReputacionRepository reputacionRepository;
    private final CertificacionRepository certificacionRepository;
    private final InsigniaMaestriaRepository insigniaRepository;
    private final Faker faker = new Faker();
    private final Random random = new Random();
    
    @Override
    public void run(String... args) {
        if (reputacionRepository.count() == 0) {
            cargarDatosPrueba();
        }
    }
    
    private void cargarDatosPrueba() {
        System.out.println("Cargando datos de prueba para maestrías...");
        
        // Cargar reputaciones
        cargarReputaciones();
        
        // Cargar certificaciones
        cargarCertificaciones();
        
        // Cargar insignias de maestría
        cargarInsignias();
        
        System.out.println("Datos de prueba cargados exitosamente");
    }
    
    private void cargarReputaciones() {
        // Generar reputaciones para técnicos (IDs 1-20)
        for (int i = 1; i <= 20; i++) {
            // Cada técnico tendrá reputación en 2-4 categorías aleatorias
            int numCategorias = random.nextInt(3) + 2; // 2-4 categorías
            List<CategoriaServicio> categoriasSeleccionadas = java.util.Arrays.asList(
                CategoriaServicio.values()).subList(0, Math.min(numCategorias, CategoriaServicio.values().length));
            java.util.Collections.shuffle(categoriasSeleccionadas);
            
            for (CategoriaServicio categoria : categoriasSeleccionadas) {
                Reputacion reputacion = new Reputacion();
                reputacion.setIdTecnico((long) i);
                reputacion.setCategoria(categoria);
                
                // Generar nivel de reputación realista (1.0 - 5.0)
                double nivel = 1.0 + (random.nextDouble() * 4.0);
                reputacion.setNivelReputacion(BigDecimal.valueOf(nivel)
                    .setScale(2, RoundingMode.HALF_UP));
                
                // Generar número de valoraciones recibidas
                reputacion.setValoracionesRecibidas(random.nextInt(50) + 5); // 5-54 valoraciones
                
                reputacion.setFechaActualizacion(LocalDateTime.now()
                    .minusDays(random.nextInt(365))); // Última actualización en el último año
                
                reputacionRepository.save(reputacion);
            }
        }
        
        System.out.println("Reputaciones cargadas: " + reputacionRepository.count());
    }
    
    private void cargarCertificaciones() {
        List<String> tiposCertificaciones = Arrays.asList(
            "Certificación Básica",
            "Certificación Intermedia", 
            "Certificación Avanzada",
            "Especialización Técnica",
            "Certificación Internacional"
        );
        
        List<EstadoCertificacion> estados = Arrays.asList(EstadoCertificacion.values());
        
        // Generar 30 certificaciones
        for (int i = 1; i <= 30; i++) {
            Certificacion certificacion = new Certificacion();
            
            // Asignar a técnico aleatorio (1-20)
            certificacion.setIdTecnico((long) (random.nextInt(20) + 1));
            
            // Categoría aleatoria
            certificacion.setCategoria(faker.options().option(CategoriaServicio.class));
            
            // Tipo de certificación aleatorio
            certificacion.setTipoCertificacion(faker.options().option(tiposCertificaciones.toArray(new String[0])));
            
            // Estado aleatorio
            certificacion.setEstado(faker.options().option(estados.toArray(new EstadoCertificacion[0])));
            
            // Fecha de subida en los últimos 6 meses
            certificacion.setFechaSubida(LocalDateTime.now()
                .minusDays(random.nextInt(180)));
            
            // Si está aprobada o rechazada, agregar fecha de validación
            if (certificacion.getEstado() != EstadoCertificacion.PENDIENTE) {
                certificacion.setFechaValidacion(certificacion.getFechaSubida()
                    .plusDays(random.nextInt(30) + 1)); // Validada 1-30 días después
                
                if (certificacion.getEstado() == EstadoCertificacion.APROBADA) {
                    certificacion.setObservaciones("Certificación aprobada. Cumple con todos los requisitos.");
                } else {
                    certificacion.setObservaciones("Certificación rechazada. " + 
                        faker.lorem().sentence(8));
                }
            } else {
                certificacion.setObservaciones("Certificación en proceso de revisión.");
            }
            
            certificacionRepository.save(certificacion);
        }
        
        System.out.println("Certificaciones cargadas: " + certificacionRepository.count());
    }
    
    private void cargarInsignias() {
        List<NivelMaestria> niveles = Arrays.asList(NivelMaestria.values());
        
        // Obtener certificaciones aprobadas para generar insignias
        List<Certificacion> certificacionesAprobadas = certificacionRepository
            .findByEstado(EstadoCertificacion.APROBADA);
        
        // Generar insignias para algunas certificaciones aprobadas (70% probabilidad)
        for (Certificacion cert : certificacionesAprobadas) {
            if (random.nextDouble() < 0.7) { // 70% probabilidad de tener insignia
                
                // Verificar si ya tiene insignia en esta categoría
                List<InsigniaMaestria> insigniasExistentes = insigniaRepository
                    .findByIdTecnicoAndCategoriaAndActivaTrue(cert.getIdTecnico(), cert.getCategoria());
                
                if (insigniasExistentes.isEmpty()) {
                    InsigniaMaestria insignia = new InsigniaMaestria();
                    insignia.setIdTecnico(cert.getIdTecnico());
                    insignia.setCategoria(cert.getCategoria());
                    
                    // Nivel basado en la reputación del técnico
                    Optional<Reputacion> reputacionOpt = reputacionRepository
                        .findByIdTecnicoAndCategoria(cert.getIdTecnico(), cert.getCategoria());
                    
                    if (reputacionOpt.isPresent()) {
                        BigDecimal nivelRep = reputacionOpt.get().getNivelReputacion();
                        if (nivelRep.compareTo(BigDecimal.valueOf(4.8)) >= 0) {
                            insignia.setNivelMaestria(NivelMaestria.DIAMANTE);
                        } else if (nivelRep.compareTo(BigDecimal.valueOf(4.5)) >= 0) {
                            insignia.setNivelMaestria(NivelMaestria.ORO);
                        } else if (nivelRep.compareTo(BigDecimal.valueOf(4.0)) >= 0) {
                            insignia.setNivelMaestria(NivelMaestria.PLATA);
                        } else {
                            insignia.setNivelMaestria(NivelMaestria.BRONCE);
                        }
                    } else {
                        insignia.setNivelMaestria(NivelMaestria.BRONCE);
                    }
                    
                    // Fecha de obtención cercana a la fecha de aprobación de certificación
                    insignia.setFechaObtencion(cert.getFechaValidacion()
                        .plusDays(random.nextInt(7) + 1)); // 1-7 días después de la aprobación
                    
                    insignia.setActiva(true);
                    
                    insigniaRepository.save(insignia);
                }
            }
        }
        
        System.out.println("Insignias de maestría cargadas: " + insigniaRepository.count());
    }
}
