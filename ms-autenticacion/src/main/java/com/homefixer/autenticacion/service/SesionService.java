package com.homefixer.autenticacion.service;

import com.homefixer.autenticacion.model.Sesion; // Importa entidad Sesion
import com.homefixer.autenticacion.repository.SesionRepository; // Importa repositorio
import lombok.RequiredArgsConstructor; // Constructor automático
import org.springframework.stereotype.Service; // Anotación servicio
import java.time.LocalDateTime; // Para fechas
import java.util.List; // Para listas
import java.util.Optional; // Para opcionales
import java.util.UUID; // Para generar tokens únicos

@Service // Marca como servicio Spring
@RequiredArgsConstructor // Constructor automático
public class SesionService {
    
    private final SesionRepository sesionRepository; // Repositorio inyectado
    
    // Iniciar nueva sesión
    public Sesion iniciarSesion(Long idUsuario, String ipCliente, String userAgent) {
        System.out.println("🚀 Iniciando sesión para usuario: " + idUsuario); // Log inicio
        
        // Cerrar sesiones activas anteriores del usuario
        List<Sesion> sesionesActivas = sesionRepository.findByIdUsuarioAndEstado(idUsuario, Sesion.EstadoSesion.ACTIVA);
        for (Sesion sesionActiva : sesionesActivas) {
            sesionActiva.setEstado(Sesion.EstadoSesion.CERRADA); // Cierra sesión anterior
            sesionActiva.setFechaFin(LocalDateTime.now()); // Marca fecha de cierre
            sesionRepository.save(sesionActiva); // Guarda cambio
        }
        
        // Extraer información del user agent
        String dispositivo = extraerDispositivo(userAgent); // Extrae tipo de dispositivo
        String navegador = extraerNavegador(userAgent); // Extrae navegador
        
        // Crear nueva sesión
        Sesion nuevaSesion = Sesion.builder()
            .idUsuario(idUsuario) // Usuario que inicia sesión
            .tokenSesion(generarTokenSesion()) // Token único de sesión
            .fechaInicio(LocalDateTime.now()) // Fecha actual
            .fechaUltimoAcceso(LocalDateTime.now()) // Última actividad
            .estado(Sesion.EstadoSesion.ACTIVA) // Estado activo
            .ipCliente(ipCliente) // IP del cliente
            .dispositivo(dispositivo) // Tipo de dispositivo
            .navegador(navegador) // Navegador usado
            .build(); // Construye objeto
        
        Sesion sesionGuardada = sesionRepository.save(nuevaSesion); // Guarda en BD
        System.out.println("✅ Sesión iniciada con ID: " + sesionGuardada.getIdSesion()); // Log éxito
        
        return sesionGuardada; // Retorna sesión creada
    }
    
    // Actualizar último acceso de sesión
    public void actualizarUltimoAcceso(String tokenSesion) {
        System.out.println("🔄 Actualizando último acceso para sesión"); // Log actualización
        
        Optional<Sesion> sesionOpt = sesionRepository.findByTokenSesionAndEstado(tokenSesion, Sesion.EstadoSesion.ACTIVA); // Busca sesión activa
        
        if (sesionOpt.isPresent()) {
            Sesion sesion = sesionOpt.get(); // Obtiene sesión
            sesion.setFechaUltimoAcceso(LocalDateTime.now()); // Actualiza último acceso
            sesionRepository.save(sesion); // Guarda cambio
            System.out.println("✅ Último acceso actualizado"); // Log éxito
        } else {
            System.out.println("❌ Sesión no encontrada para actualizar"); // Log error
        }
    }
    
    // Cerrar sesión
    public void cerrarSesion(String tokenSesion) {
        System.out.println("🔚 Cerrando sesión"); // Log cierre
        
        Optional<Sesion> sesionOpt = sesionRepository.findByTokenSesion(tokenSesion); // Busca sesión
        
        if (sesionOpt.isPresent()) {
            Sesion sesion = sesionOpt.get(); // Obtiene sesión
            sesion.setEstado(Sesion.EstadoSesion.CERRADA); // Cambia estado a cerrada
            sesion.setFechaFin(LocalDateTime.now()); // Marca fecha de cierre
            sesionRepository.save(sesion); // Guarda cambio
            System.out.println("✅ Sesión cerrada exitosamente"); // Log éxito
        } else {
            System.out.println("❌ Sesión no encontrada para cerrar"); // Log error
        }
    }
    
    // Validar sesión activa
    public boolean validarSesion(String tokenSesion) {
        System.out.println("🔍 Validando sesión"); // Log validación
        
        Optional<Sesion> sesionOpt = sesionRepository.findByTokenSesionAndEstado(tokenSesion, Sesion.EstadoSesion.ACTIVA); // Busca sesión activa
        
        if (sesionOpt.isPresent()) {
            Sesion sesion = sesionOpt.get(); // Obtiene sesión
            
            // Verificar si la sesión ha estado inactiva por más de 2 horas
            LocalDateTime limite = LocalDateTime.now().minusHours(2); // 2 horas atrás
            if (sesion.getFechaUltimoAcceso().isBefore(limite)) {
                System.out.println("❌ Sesión expirada por inactividad"); // Log error
                sesion.setEstado(Sesion.EstadoSesion.EXPIRADA); // Marca como expirada
                sesionRepository.save(sesion); // Guarda cambio
                return false; // Sesión expirada
            }
            
            System.out.println("✅ Sesión válida"); // Log éxito
            return true; // Sesión válida
        }
        
        System.out.println("❌ Sesión no válida"); // Log error
        return false; // Sesión no válida
    }
    
    // Obtener sesiones de un usuario
    public List<Sesion> obtenerSesionesUsuario(Long idUsuario) {
        System.out.println("📋 Obteniendo sesiones del usuario: " + idUsuario); // Log consulta
        return sesionRepository.findByIdUsuario(idUsuario); // Retorna todas las sesiones del usuario
    }
    
    // Cerrar todas las sesiones de un usuario
    public void cerrarTodasSesionesUsuario(Long idUsuario) {
        System.out.println("🔚 Cerrando todas las sesiones del usuario: " + idUsuario); // Log cierre masivo
        
        List<Sesion> sesionesActivas = sesionRepository.findByIdUsuarioAndEstado(idUsuario, Sesion.EstadoSesion.ACTIVA); // Busca sesiones activas
        
        for (Sesion sesion : sesionesActivas) {
            sesion.setEstado(Sesion.EstadoSesion.REVOCADA); // Marca como revocada
            sesion.setFechaFin(LocalDateTime.now()); // Marca fecha de cierre
            sesionRepository.save(sesion); // Guarda cambio
        }
        
        System.out.println("✅ Cerradas " + sesionesActivas.size() + " sesiones"); // Log éxito
    }
    
    // Limpiar sesiones inactivas
    public void limpiarSesionesInactivas() {
        System.out.println("🧹 Limpiando sesiones inactivas"); // Log limpieza
        
        LocalDateTime limite = LocalDateTime.now().minusHours(24); // 24 horas atrás
        List<Sesion> sesionesInactivas = sesionRepository.encontrarSesionesInactivas(limite); // Busca sesiones inactivas
        
        for (Sesion sesion : sesionesInactivas) {
            sesion.setEstado(Sesion.EstadoSesion.EXPIRADA); // Marca como expirada
            sesionRepository.save(sesion); // Guarda cambio
        }
        
        System.out.println("✅ Limpiadas " + sesionesInactivas.size() + " sesiones inactivas"); // Log éxito
    }
    
    // Método privado para generar token único de sesión
    private String generarTokenSesion() {
        return "SES_" + UUID.randomUUID().toString().replace("-", ""); // Token de sesión con prefijo SES
    }
    
    // Método privado para extraer dispositivo del user agent
    private String extraerDispositivo(String userAgent) {
        if (userAgent == null) return "Desconocido"; // Si no hay user agent
        
        if (userAgent.toLowerCase().contains("mobile")) return "Móvil"; // Dispositivo móvil
        if (userAgent.toLowerCase().contains("tablet")) return "Tablet"; // Tablet
        return "Desktop"; // Por defecto desktop
    }
    
    // Método privado para extraer navegador del user agent
    private String extraerNavegador(String userAgent) {
        if (userAgent == null) return "Desconocido"; // Si no hay user agent
        
        String userAgentLower = userAgent.toLowerCase(); // Convierte a minúsculas
        
        if (userAgentLower.contains("chrome")) return "Chrome"; // Chrome
        if (userAgentLower.contains("firefox")) return "Firefox"; // Firefox
        if (userAgentLower.contains("edge")) return "Edge"; // Edge
        return "Otro"; // Navegador no identificado
    }
}