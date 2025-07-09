package com.homefixer.usuarios.service;

import com.homefixer.usuarios.model.Tecnico; // Modelo Técnico
import com.homefixer.usuarios.repository.TecnicoRepository; // Repositorio Técnico
import lombok.RequiredArgsConstructor; // Inyección por constructor
import org.springframework.stereotype.Service; // Marca como servicio
import java.util.List; // Lista de técnicos
import java.util.Optional; // Técnico opcional

@Service // Marca como servicio de Spring
@RequiredArgsConstructor // Genera constructor con campos final/non-null
public class TecnicoService {
    
    private final TecnicoRepository tecnicoRepository; // Repositorio inyectado
    
    public List<Tecnico> obtenerTodosLosTecnicos() { // Obtiene todos los técnicos
        return tecnicoRepository.findAll(); // Busca todos en BD
    }
    
    public Optional<Tecnico> obtenerTecnicoPorId(Long idTecnico) { // Busca técnico por ID
        return tecnicoRepository.findById(idTecnico); // Busca por ID en BD
    }
    
    public List<Tecnico> obtenerTecnicosDisponibles() { // Obtiene técnicos disponibles
        return tecnicoRepository.findByDisponible(true); // Busca por disponibilidad en BD
    }
    
    public Tecnico crearTecnico(Tecnico tecnico) { // Crea nuevo técnico
        if (tecnicoRepository.existsByEmail(tecnico.getEmail())) { // Verifica email único
            throw new RuntimeException("Ya existe un técnico con este email"); // Error si existe
        }
        return tecnicoRepository.save(tecnico); // Guarda en BD
    }
    
    public Tecnico actualizarTecnico(Long idTecnico, Tecnico tecnicoActualizado) { // Actualiza técnico
        return tecnicoRepository.findById(idTecnico) // Busca técnico existente
            .map(tecnico -> { // Si existe
                tecnico.setNombre(tecnicoActualizado.getNombre()); // Actualiza nombre
                tecnico.setApellido(tecnicoActualizado.getApellido()); // Actualiza apellido
                tecnico.setEmail(tecnicoActualizado.getEmail()); // Actualiza email
                tecnico.setTelefono(tecnicoActualizado.getTelefono()); // Actualiza teléfono
                tecnico.setEspecialidad(tecnicoActualizado.getEspecialidad()); // Actualiza especialidad
                tecnico.setDisponible(tecnicoActualizado.getDisponible()); // Actualiza disponibilidad
                return tecnicoRepository.save(tecnico); // Guarda cambios en BD
            })
            .orElseThrow(() -> new RuntimeException("Técnico no encontrado con id: " + idTecnico)); // Error si no existe
    }
    
    public void eliminarTecnico(Long idTecnico) { // Elimina técnico
        if (!tecnicoRepository.existsById(idTecnico)) { // Verifica si existe
            throw new RuntimeException("Técnico no encontrado con id: " + idTecnico); // Error si no existe
        }
        tecnicoRepository.deleteById(idTecnico); // Elimina de BD
    }
}