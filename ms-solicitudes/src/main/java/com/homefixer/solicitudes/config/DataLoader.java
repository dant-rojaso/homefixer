package com.homefixer.solicitudes.config;

import com.github.javafaker.Faker;
import com.homefixer.solicitudes.model.EstadoSolicitud;
import com.homefixer.solicitudes.model.Solicitud;
import com.homefixer.solicitudes.repository.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Carga datos de prueba al iniciar la aplicación
 */
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    
    private final SolicitudRepository solicitudRepository;
    private final Faker faker = new Faker();
    private final Random random = new Random();
    
    // Tipos de servicios disponibles
    private final String[] tiposServicio = {
        "Electricidad", "Plomería", "Carpintería", "Pintura", 
        "Refrigeración", "Cerrajería", "Jardinería", "Limpieza"
    };
    
    @Override
    public void run(String... args) throws Exception {
        // Solo cargar datos si la base está vacía
        if (solicitudRepository.count() == 0) {
            cargarSolicitudesDePrueba();
        }
    }
    
    /**
     * Genera 20 solicitudes de prueba con datos realistas
     */
    private void cargarSolicitudesDePrueba() {
        for (int i = 1; i <= 20; i++) {
            Solicitud solicitud = new Solicitud();
            
            // ID de cliente (asumiendo que existen clientes del 1 al 10)
            solicitud.setIdCliente((long) (random.nextInt(10) + 1));
            
            // Tipo de servicio aleatorio
            solicitud.setTipoServicio(tiposServicio[random.nextInt(tiposServicio.length)]);
            
            // Descripción del problema
            solicitud.setDescripcionProblema(generarDescripcionProblema(solicitud.getTipoServicio()));
            
            // Dirección del servicio
            solicitud.setDireccionServicio(generarDireccion());
            
            // Fecha de solicitud (entre hoy y hace 30 días)
            solicitud.setFechaSolicitud(LocalDateTime.now().minusDays(random.nextInt(30)));
            
            // Fecha de servicio (entre mañana y 15 días)
            solicitud.setFechaServicio(LocalDate.now().plusDays(random.nextInt(15) + 1));
            
            // Estado aleatorio
            EstadoSolicitud[] estados = EstadoSolicitud.values();
            EstadoSolicitud estado = estados[random.nextInt(estados.length)];
            solicitud.setEstadoSolicitud(estado);
            
            // Si está asignada o en proceso, necesita técnico
            if (estado == EstadoSolicitud.ASIGNADA || estado == EstadoSolicitud.EN_PROCESO || estado == EstadoSolicitud.COMPLETADA) {
                solicitud.setIdTecnico((long) (random.nextInt(5) + 1)); // Técnicos del 1 al 5
            }
            
            // Presupuesto estimado
            solicitud.setPresupuestoEstimado(BigDecimal.valueOf(random.nextInt(100000) + 10000));
            
            // Observaciones ocasionales
            if (random.nextBoolean()) {
                solicitud.setObservaciones(generarObservaciones());
            }
            
            solicitudRepository.save(solicitud);
        }
        
        System.out.println("✅ Cargadas 20 solicitudes de prueba exitosamente");
    }
    
    /**
     * Genera descripción del problema según tipo de servicio
     */
    private String generarDescripcionProblema(String tipoServicio) {
        return switch (tipoServicio) {
            case "Electricidad" -> faker.options().option(
                "Falla en instalación eléctrica de cocina",
                "Cortocircuito en tablero principal",
                "Luces que parpadean constantemente",
                "Enchufe que no funciona en dormitorio",
                "Instalación de ventilador de techo"
            );
            case "Plomería" -> faker.options().option(
                "Fuga de agua en cañería del baño",
                "WC que no descarga correctamente",
                "Llave de cocina gotea constantemente",
                "Destape de cañería obstruida",
                "Instalación de nuevo lavamanos"
            );
            case "Carpintería" -> faker.options().option(
                "Reparación de puerta que no cierra bien",
                "Construcción de mueble a medida",
                "Arreglo de ventana atascada",
                "Instalación de estanterías",
                "Reparación de piso laminado"
            );
            case "Pintura" -> faker.options().option(
                "Pintura completa de dormitorio",
                "Retoque de paredes con humedad",
                "Pintura de fachada exterior",
                "Aplicación de barniz en muebles",
                "Pintura decorativa en sala"
            );
            default -> "Servicio de " + tipoServicio.toLowerCase() + " requerido";
        };
    }
    
    /**
     * Genera dirección realista para Santiago
     */
    private String generarDireccion() {
        String[] calles = {
            "Av. Las Condes", "Av. Providencia", "Av. Apoquindo", "Av. Vitacura",
            "Calle Huérfanos", "Calle Estado", "Av. Libertador", "Av. Grecia"
        };
        
        String[] comunas = {
            "Las Condes", "Providencia", "Vitacura", "Ñuñoa",
            "Santiago Centro", "La Reina", "Macul", "San Miguel"
        };
        
        String calle = faker.options().option(calles);
        int numero = faker.number().numberBetween(100, 9999);
        String comuna = faker.options().option(comunas);
        
        return calle + " " + numero + ", " + comuna + ", Santiago";
    }
    
    /**
     * Genera observaciones opcionales
     */
    private String generarObservaciones() {
        return faker.options().option(
            "Disponible solo en horario de mañana",
            "Urgente - problema crítico",
            "Preferible fin de semana",
            "Acceso por portería principal",
            "Contactar antes de llegar",
            "Material incluido en presupuesto",
            "Segunda revisión necesaria"
        );
    }
}