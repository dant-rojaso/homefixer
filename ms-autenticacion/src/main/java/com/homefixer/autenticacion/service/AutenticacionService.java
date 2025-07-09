package com.homefixer.autenticacion.service;

import com.homefixer.autenticacion.model.Autenticacion;
import com.homefixer.autenticacion.model.TipoUsuario;
import com.homefixer.autenticacion.model.EstadoSesion;
import com.homefixer.autenticacion.repository.AutenticacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AutenticacionService {
    
    @Autowired
    private AutenticacionRepository autenticacionRepository;
    
    // Obtener todas las autenticaciones
    public List<Autenticacion> obtenerTodasLasAutenticaciones() {
        return autenticacionRepository.findAll();
    }
    
    // Obtener autenticación por ID
    public Optional<Autenticacion> obtenerAutenticacionPorId(Long id) {
        return autenticacionRepository.findById(id);
    }
    
    // Crear nueva autenticación
    public Autenticacion crearAutenticacion(Autenticacion autenticacion) {
        if (autenticacionRepository.existsByEmail(autenticacion.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        return autenticacionRepository.save(autenticacion);
    }
    
    // Actualizar autenticación
    public Optional<Autenticacion> actualizarAutenticacion(Long id, Autenticacion autenticacionActualizada) {
        return autenticacionRepository.findById(id)
                .map(autenticacion -> {
                    autenticacion.setContrasena(autenticacionActualizada.getContrasena());
                    autenticacion.setObservaciones(autenticacionActualizada.getObservaciones());
                    return autenticacionRepository.save(autenticacion);
                });
    }
    
    // Eliminar autenticación
    public boolean eliminarAutenticacion(Long id) {
        if (autenticacionRepository.existsById(id)) {
            autenticacionRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Login de usuario
    public Optional<Autenticacion> iniciarSesion(String email, String contrasena) {
        Optional<Autenticacion> autenticacion = autenticacionRepository.findByEmail(email);
        if (autenticacion.isPresent() && autenticacion.get().getContrasena().equals(contrasena)) {
            Autenticacion auth = autenticacion.get();
            auth.setUltimoLogin(LocalDateTime.now());
            auth.setEstadoSesion(EstadoSesion.ACTIVA);
            auth.setTokenSesion(UUID.randomUUID().toString());
            auth.setFechaExpiracion(LocalDateTime.now().plusHours(24));
            return Optional.of(autenticacionRepository.save(auth));
        }
        return Optional.empty();
    }
    
    // Cerrar sesión
    public boolean cerrarSesion(String tokenSesion) {
        Optional<Autenticacion> autenticacion = autenticacionRepository.findByTokenSesion(tokenSesion);
        if (autenticacion.isPresent()) {
            Autenticacion auth = autenticacion.get();
            auth.setEstadoSesion(EstadoSesion.INACTIVA);
            auth.setTokenSesion(null);
            auth.setFechaExpiracion(null);
            autenticacionRepository.save(auth);
            return true;
        }
        return false;
    }
    
    // Filtrar por tipo de usuario
    public List<Autenticacion> obtenerPorTipoUsuario(TipoUsuario tipoUsuario) {
        return autenticacionRepository.findByTipoUsuario(tipoUsuario);
    }
    
    // Filtrar por estado de sesión
    public List<Autenticacion> obtenerPorEstadoSesion(EstadoSesion estadoSesion) {
        return autenticacionRepository.findByEstadoSesion(estadoSesion);
    }
    
    // Validar token de sesión
    public boolean validarToken(String tokenSesion) {
        Optional<Autenticacion> autenticacion = autenticacionRepository.findByTokenSesion(tokenSesion);
        if (autenticacion.isPresent()) {
            Autenticacion auth = autenticacion.get();
            if (auth.getFechaExpiracion().isAfter(LocalDateTime.now())) {
                return true;
            } else {
                // Token expirado
                auth.setEstadoSesion(EstadoSesion.EXPIRADA);
                autenticacionRepository.save(auth);
            }
        }
        return false;
    }
}