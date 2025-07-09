package com.homefixer.solicitudes.service;

import com.homefixer.solicitudes.model.EstadoSolicitud;
import com.homefixer.solicitudes.model.Solicitud;
import com.homefixer.solicitudes.repository.SolicitudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para SolicitudService
 */
@ExtendWith(MockitoExtension.class)
class SolicitudServiceTest {
    
    @Mock
    private SolicitudRepository solicitudRepository;
    
    @InjectMocks
    private SolicitudService solicitudService;
    
    private Solicitud solicitudPrueba;
    
    @BeforeEach
    void setUp() {
        solicitudPrueba = new Solicitud();
        solicitudPrueba.setIdSolicitud(1L);
        solicitudPrueba.setIdCliente(1L);
        solicitudPrueba.setTipoServicio("Electricidad");
        solicitudPrueba.setDescripcionProblema("Falla eléctrica en cocina");
        solicitudPrueba.setDireccionServicio("Av. Las Condes 123");
        solicitudPrueba.setFechaSolicitud(LocalDateTime.now());
        solicitudPrueba.setFechaServicio(LocalDate.now().plusDays(1));
        solicitudPrueba.setEstadoSolicitud(EstadoSolicitud.PENDIENTE);
        solicitudPrueba.setPresupuestoEstimado(BigDecimal.valueOf(50000));
    }
    
    @Test
    @DisplayName("Debe obtener todas las solicitudes")
    void debeObtenerTodasLasSolicitudes() {
        // Arrange
        List<Solicitud> solicitudes = Arrays.asList(solicitudPrueba);
        when(solicitudRepository.findAll()).thenReturn(solicitudes);
        
        // Act
        List<Solicitud> resultado = solicitudService.obtenerTodasLasSolicitudes();
        
        // Assert
        assertEquals(1, resultado.size());
        assertEquals(solicitudPrueba.getIdSolicitud(), resultado.get(0).getIdSolicitud());
        verify(solicitudRepository).findAll();
    }
    
    @Test
    @DisplayName("Debe obtener solicitud por ID existente")
    void debeObtenerSolicitudPorIdExistente() {
        // Arrange
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitudPrueba));
        
        // Act
        Optional<Solicitud> resultado = solicitudService.obtenerSolicitudPorId(1L);
        
        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(solicitudPrueba.getIdSolicitud(), resultado.get().getIdSolicitud());
        verify(solicitudRepository).findById(1L);
    }
    
    @Test
    @DisplayName("Debe retornar vacío para ID inexistente")
    void debeRetornarVacioParaIdInexistente() {
        // Arrange
        when(solicitudRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act
        Optional<Solicitud> resultado = solicitudService.obtenerSolicitudPorId(999L);
        
        // Assert
        assertFalse(resultado.isPresent());
        verify(solicitudRepository).findById(999L);
    }
    
    @Test
    @DisplayName("Debe crear solicitud con estado PENDIENTE")
    void debeCrearSolicitudConEstadoPendiente() {
        // Arrange
        Solicitud nuevaSolicitud = new Solicitud();
        nuevaSolicitud.setIdCliente(1L);
        nuevaSolicitud.setTipoServicio("Plomería");
        
        when(solicitudRepository.save(any(Solicitud.class))).thenReturn(solicitudPrueba);
        
        // Act
        Solicitud resultado = solicitudService.crearSolicitud(nuevaSolicitud);
        
        // Assert
        assertEquals(EstadoSolicitud.PENDIENTE, nuevaSolicitud.getEstadoSolicitud());
        assertNotNull(resultado);
        verify(solicitudRepository).save(nuevaSolicitud);
    }
    
    @Test
    @DisplayName("Debe actualizar solicitud existente")
    void debeActualizarSolicitudExistente() {
        // Arrange
        Solicitud solicitudActualizada = new Solicitud();
        solicitudActualizada.setTipoServicio("Plomería");
        solicitudActualizada.setDescripcionProblema("Nueva descripción");
        solicitudActualizada.setDireccionServicio("Nueva dirección");
        solicitudActualizada.setFechaServicio(LocalDate.now().plusDays(3));
        solicitudActualizada.setPresupuestoEstimado(BigDecimal.valueOf(75000));
        solicitudActualizada.setObservaciones("Nuevas observaciones");
        
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitudPrueba));
        when(solicitudRepository.save(any(Solicitud.class))).thenReturn(solicitudPrueba);
        
        // Act
        Solicitud resultado = solicitudService.actualizarSolicitud(1L, solicitudActualizada);
        
        // Assert
        assertNotNull(resultado);
        verify(solicitudRepository).findById(1L);
        verify(solicitudRepository).save(solicitudPrueba);
    }
    
    @Test
    @DisplayName("Debe lanzar excepción al actualizar solicitud inexistente")
    void debeLanzarExcepcionAlActualizarSolicitudInexistente() {
        // Arrange
        when(solicitudRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> solicitudService.actualizarSolicitud(999L, new Solicitud()));
        
        assertEquals("Solicitud no encontrada con ID: 999", exception.getMessage());
        verify(solicitudRepository).findById(999L);
    }
    
    @Test
    @DisplayName("Debe cancelar solicitud existente")
    void debeCancelarSolicitudExistente() {
        // Arrange
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitudPrueba));
        when(solicitudRepository.save(any(Solicitud.class))).thenReturn(solicitudPrueba);
        
        // Act
        solicitudService.cancelarSolicitud(1L);
        
        // Assert
        assertEquals(EstadoSolicitud.CANCELADA, solicitudPrueba.getEstadoSolicitud());
        verify(solicitudRepository).findById(1L);
        verify(solicitudRepository).save(solicitudPrueba);
    }
    
    @Test
    @DisplayName("Debe asignar técnico a solicitud pendiente")
    void debeAsignarTecnicoASolicitudPendiente() {
        // Arrange
        solicitudPrueba.setEstadoSolicitud(EstadoSolicitud.PENDIENTE);
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitudPrueba));
        when(solicitudRepository.save(any(Solicitud.class))).thenReturn(solicitudPrueba);
        
        // Act
        solicitudService.asignarTecnico(1L, 2L);
        
        // Assert
        assertEquals(2L, solicitudPrueba.getIdTecnico());
        assertEquals(EstadoSolicitud.ASIGNADA, solicitudPrueba.getEstadoSolicitud());
        verify(solicitudRepository).save(solicitudPrueba);
    }
    
    @Test
    @DisplayName("Debe fallar al asignar técnico a solicitud no pendiente")
    void debeFallarAlAsignarTecnicoASolicitudNoPendiente() {
        // Arrange
        solicitudPrueba.setEstadoSolicitud(EstadoSolicitud.COMPLETADA);
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitudPrueba));
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> solicitudService.asignarTecnico(1L, 2L));
        
        assertEquals("Solo se pueden asignar técnicos a solicitudes pendientes", exception.getMessage());
    }
    
    @Test
    @DisplayName("Debe cambiar estado con transición válida")
    void debeCambiarEstadoConTransicionValida() {
        // Arrange
        solicitudPrueba.setEstadoSolicitud(EstadoSolicitud.ASIGNADA);
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitudPrueba));
        when(solicitudRepository.save(any(Solicitud.class))).thenReturn(solicitudPrueba);
        
        // Act
        solicitudService.cambiarEstado(1L, EstadoSolicitud.EN_PROCESO);
        
        // Assert
        assertEquals(EstadoSolicitud.EN_PROCESO, solicitudPrueba.getEstadoSolicitud());
        verify(solicitudRepository).save(solicitudPrueba);
    }
    
    @Test
    @DisplayName("Debe fallar con transición de estado inválida")
    void debeFallarConTransicionDeEstadoInvalida() {
        // Arrange
        solicitudPrueba.setEstadoSolicitud(EstadoSolicitud.COMPLETADA);
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitudPrueba));
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> solicitudService.cambiarEstado(1L, EstadoSolicitud.PENDIENTE));
        
        assertTrue(exception.getMessage().contains("Transición de estado inválida"));
    }
    
    @Test
    @DisplayName("Debe obtener solicitudes por cliente")
    void debeObtenerSolicitudesPorCliente() {
        // Arrange
        List<Solicitud> solicitudes = Arrays.asList(solicitudPrueba);
        when(solicitudRepository.findByIdCliente(1L)).thenReturn(solicitudes);
        
        // Act
        List<Solicitud> resultado = solicitudService.obtenerSolicitudesPorCliente(1L);
        
        // Assert
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getIdCliente());
        verify(solicitudRepository).findByIdCliente(1L);
    }
    
    @Test
    @DisplayName("Debe obtener solicitudes por estado")
    void debeObtenerSolicitudesPorEstado() {
        // Arrange
        List<Solicitud> solicitudes = Arrays.asList(solicitudPrueba);
        when(solicitudRepository.findByEstadoSolicitud(EstadoSolicitud.PENDIENTE)).thenReturn(solicitudes);
        
        // Act
        List<Solicitud> resultado = solicitudService.obtenerSolicitudesPorEstado(EstadoSolicitud.PENDIENTE);
        
        // Assert
        assertEquals(1, resultado.size());
        assertEquals(EstadoSolicitud.PENDIENTE, resultado.get(0).getEstadoSolicitud());
        verify(solicitudRepository).findByEstadoSolicitud(EstadoSolicitud.PENDIENTE);
    }
}