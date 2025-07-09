package com.homefixer.maestrias.service;

import com.homefixer.maestrias.model.*;
import com.homefixer.maestrias.repository.CertificacionRepository;
import com.homefixer.maestrias.repository.InsigniaMaestriaRepository;
import com.homefixer.maestrias.repository.ReputacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas del servicio de maestrías")
class MaestriaServiceTest {
    
    @Mock
    private ReputacionRepository reputacionRepository;
    
    @Mock
    private CertificacionRepository certificacionRepository;
    
    @Mock
    private InsigniaMaestriaRepository insigniaRepository;
    
    @InjectMocks
    private MaestriaService maestriaService;
    
    private Reputacion reputacionMock;
    private Certificacion certificacionMock;
    private InsigniaMaestria insigniaMock;
    
    @BeforeEach
    void setUp() {
        // Configurar mocks base
        reputacionMock = new Reputacion();
        reputacionMock.setIdReputacion(1L);
        reputacionMock.setIdTecnico(1L);
        reputacionMock.setCategoria(CategoriaServicio.ELECTRICIDAD);
        reputacionMock.setNivelReputacion(BigDecimal.valueOf(4.5));
        reputacionMock.setValoracionesRecibidas(10);
        
        certificacionMock = new Certificacion();
        certificacionMock.setIdCertificacion(1L);
        certificacionMock.setIdTecnico(1L);
        certificacionMock.setCategoria(CategoriaServicio.ELECTRICIDAD);
        certificacionMock.setTipoCertificacion("Certificación Básica");
        certificacionMock.setEstado(EstadoCertificacion.PENDIENTE);
        
        insigniaMock = new InsigniaMaestria();
        insigniaMock.setIdInsignia(1L);
        insigniaMock.setIdTecnico(1L);
        insigniaMock.setCategoria(CategoriaServicio.ELECTRICIDAD);
        insigniaMock.setNivelMaestria(NivelMaestria.BRONCE);
        insigniaMock.setActiva(true);
    }
    
    @Test
    @DisplayName("Debe calcular nueva reputación para técnico existente")
    void debeCalcularNuevaReputacionTecnicoExistente() {
        // Given
        when(reputacionRepository.findByIdTecnicoAndCategoria(1L, CategoriaServicio.ELECTRICIDAD))
            .thenReturn(Optional.of(reputacionMock));
        when(reputacionRepository.save(any(Reputacion.class))).thenReturn(reputacionMock);
        
        // When
        Reputacion resultado = maestriaService.calcularNuevaReputacion(
            1L, CategoriaServicio.ELECTRICIDAD, BigDecimal.valueOf(5.0));
        
        // Then
        assertNotNull(resultado);
        verify(reputacionRepository).save(any(Reputacion.class));
        assertEquals(11, reputacionMock.getValoracionesRecibidas());
    }
    
    @Test
    @DisplayName("Debe crear nueva reputación para técnico nuevo")
    void debeCrearNuevaReputacionTecnicoNuevo() {
        // Given
        when(reputacionRepository.findByIdTecnicoAndCategoria(1L, CategoriaServicio.ELECTRICIDAD))
            .thenReturn(Optional.empty());
        when(reputacionRepository.save(any(Reputacion.class))).thenReturn(reputacionMock);
        
        // When
        Reputacion resultado = maestriaService.calcularNuevaReputacion(
            1L, CategoriaServicio.ELECTRICIDAD, BigDecimal.valueOf(4.0));
        
        // Then
        assertNotNull(resultado);
        verify(reputacionRepository).save(any(Reputacion.class));
    }
    
    @Test
    @DisplayName("Debe permitir subir certificación con reputación suficiente")
    void debePermitirSubirCertificacionConReputacionSuficiente() {
        // Given
        when(reputacionRepository.findByIdTecnicoAndCategoria(1L, CategoriaServicio.ELECTRICIDAD))
            .thenReturn(Optional.of(reputacionMock));
        when(certificacionRepository.save(any(Certificacion.class))).thenReturn(certificacionMock);
        
        // When
        Certificacion resultado = maestriaService.subirCertificacion(
            1L, CategoriaServicio.ELECTRICIDAD, "Certificación Avanzada", "Sin observaciones");
        
        // Then
        assertNotNull(resultado);
        verify(certificacionRepository).save(any(Certificacion.class));
    }
    
    @Test
    @DisplayName("Debe rechazar subir certificación con reputación insuficiente")
    void debeRechazarSubirCertificacionConReputacionInsuficiente() {
        // Given
        reputacionMock.setNivelReputacion(BigDecimal.valueOf(3.5)); // Menor a 4.0
        when(reputacionRepository.findByIdTecnicoAndCategoria(1L, CategoriaServicio.ELECTRICIDAD))
            .thenReturn(Optional.of(reputacionMock));
        
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            maestriaService.subirCertificacion(
                1L, CategoriaServicio.ELECTRICIDAD, "Certificación Avanzada", "Sin observaciones");
        });
        
        assertTrue(exception.getMessage().contains("Nivel de reputación insuficiente"));
        verify(certificacionRepository, never()).save(any(Certificacion.class));
    }
    
    @Test
    @DisplayName("Debe validar certificación como aprobada")
    void debeValidarCertificacionComoAprobada() {
        // Given
        when(certificacionRepository.findById(1L)).thenReturn(Optional.of(certificacionMock));
        when(certificacionRepository.save(any(Certificacion.class))).thenReturn(certificacionMock);
        when(insigniaRepository.findByIdTecnicoAndCategoriaAndActivaTrue(1L, CategoriaServicio.ELECTRICIDAD))
            .thenReturn(Arrays.asList());
        when(insigniaRepository.save(any(InsigniaMaestria.class))).thenReturn(insigniaMock);
        
        // When
        Certificacion resultado = maestriaService.validarCertificacion(1L, true, "Aprobada correctamente");
        
        // Then
        assertNotNull(resultado);
        assertEquals(EstadoCertificacion.APROBADA, certificacionMock.getEstado());
        verify(certificacionRepository).save(any(Certificacion.class));
        verify(insigniaRepository).save(any(InsigniaMaestria.class)); // Debe crear insignia
    }
    
    @Test
    @DisplayName("Debe validar certificación como rechazada")
    void debeValidarCertificacionComoRechazada() {
        // Given
        when(certificacionRepository.findById(1L)).thenReturn(Optional.of(certificacionMock));
        when(certificacionRepository.save(any(Certificacion.class))).thenReturn(certificacionMock);
        
        // When
        Certificacion resultado = maestriaService.validarCertificacion(1L, false, "Documentación incompleta");
        
        // Then
        assertNotNull(resultado);
        assertEquals(EstadoCertificacion.RECHAZADA, certificacionMock.getEstado());
        verify(certificacionRepository).save(any(Certificacion.class));
        verify(insigniaRepository, never()).save(any(InsigniaMaestria.class)); // No debe crear insignia
    }
    
    @Test
    @DisplayName("Debe obtener insignias de un técnico")
    void debeObtenerInsigniasDeTecnico() {
        // Given
        List<InsigniaMaestria> insignias = Arrays.asList(insigniaMock);
        when(insigniaRepository.findByIdTecnicoAndActivaTrue(1L)).thenReturn(insignias);
        
        // When
        List<InsigniaMaestria> resultado = maestriaService.obtenerInsigniasTecnico(1L);
        
        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(insigniaMock, resultado.get(0));
    }
    
    @Test
    @DisplayName("Debe obtener reputación de un técnico")
    void debeObtenerReputacionDeTecnico() {
        // Given
        List<Reputacion> reputaciones = Arrays.asList(reputacionMock);
        when(reputacionRepository.findByIdTecnico(1L)).thenReturn(reputaciones);
        
        // When
        List<Reputacion> resultado = maestriaService.obtenerReputacionTecnico(1L);
        
        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(reputacionMock, resultado.get(0));
    }
    
    @Test
    @DisplayName("Debe obtener técnicos con maestría en una categoría")
    void debeObtenerTecnicosConMaestriaEnCategoria() {
        // Given
        List<InsigniaMaestria> insignias = Arrays.asList(insigniaMock);
        when(insigniaRepository.findByCategoriaAndActivaTrue(CategoriaServicio.ELECTRICIDAD))
            .thenReturn(insignias);
        
        // When
        List<InsigniaMaestria> resultado = maestriaService.obtenerTecnicosConMaestria(CategoriaServicio.ELECTRICIDAD);
        
        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(insigniaMock, resultado.get(0));
    }
    
    @Test
    @DisplayName("Debe lanzar excepción al validar certificación inexistente")
    void debeLanzarExcepcionAlValidarCertificacionInexistente() {
        // Given
        when(certificacionRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            maestriaService.validarCertificacion(999L, true, "Observaciones");
        });
        
        assertTrue(exception.getMessage().contains("Certificación no encontrada"));
    }
    
    @Test
    @DisplayName("No debe crear insignia duplicada para técnico")
    void noDebeCrearInsigniaDuplicadaParaTecnico() {
        // Given
        when(certificacionRepository.findById(1L)).thenReturn(Optional.of(certificacionMock));
        when(certificacionRepository.save(any(Certificacion.class))).thenReturn(certificacionMock);
        when(insigniaRepository.findByIdTecnicoAndCategoriaAndActivaTrue(1L, CategoriaServicio.ELECTRICIDAD))
            .thenReturn(Arrays.asList(insigniaMock)); // Ya tiene insignia
        
        // When
        maestriaService.validarCertificacion(1L, true, "Aprobada correctamente");
        
        // Then
        verify(insigniaRepository, never()).save(any(InsigniaMaestria.class)); // No debe crear nueva
    }
}