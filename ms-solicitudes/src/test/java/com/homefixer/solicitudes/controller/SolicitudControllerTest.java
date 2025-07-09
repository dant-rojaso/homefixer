package com.homefixer.solicitudes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homefixer.solicitudes.assembler.SolicitudModelAssembler;
import com.homefixer.solicitudes.model.EstadoSolicitud;
import com.homefixer.solicitudes.model.Solicitud;
import com.homefixer.solicitudes.service.SolicitudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas unitarias para SolicitudController
 */
@WebMvcTest(SolicitudController.class)
class SolicitudControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private SolicitudService solicitudService;
    
    @MockBean
    private SolicitudModelAssembler assembler;
    
    @Autowired
    private ObjectMapper objectMapper;
    
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
    @DisplayName("GET /api/solicitudes - Debe retornar todas las solicitudes")
    void debeRetornarTodasLasSolicitudes() throws Exception {
        // Arrange
        when(solicitudService.obtenerTodasLasSolicitudes()).thenReturn(Arrays.asList(solicitudPrueba));
        
        // Act & Assert
        mockMvc.perform(get("/api/solicitudes"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    
    @Test
    @DisplayName("GET /api/solicitudes/{id} - Debe retornar solicitud existente")
    void debeRetornarSolicitudExistente() throws Exception {
        // Arrange
        when(solicitudService.obtenerSolicitudPorId(1L)).thenReturn(Optional.of(solicitudPrueba));
        
        // Act & Assert
        mockMvc.perform(get("/api/solicitudes/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    
    @Test
    @DisplayName("GET /api/solicitudes/{id} - Debe retornar 404 para solicitud inexistente")
    void debeRetornar404ParaSolicitudInexistente() throws Exception {
        // Arrange
        when(solicitudService.obtenerSolicitudPorId(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        mockMvc.perform(get("/api/solicitudes/999"))
            .andExpect(status().isNotFound());
    }
    
    @Test
    @DisplayName("POST /api/solicitudes - Debe crear nueva solicitud")
    void debeCrearNuevaSolicitud() throws Exception {
        // Arrange
        when(solicitudService.crearSolicitud(any(Solicitud.class))).thenReturn(solicitudPrueba);
        
        String solicitudJson = objectMapper.writeValueAsString(solicitudPrueba);
        
        // Act & Assert
        mockMvc.perform(post("/api/solicitudes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(solicitudJson))
            .andExpect(status().isCreated());
    }
    
    @Test
    @DisplayName("PUT /api/solicitudes/{id} - Debe actualizar solicitud existente")
    void debeActualizarSolicitudExistente() throws Exception {
        // Arrange
        when(solicitudService.actualizarSolicitud(any(Long.class), any(Solicitud.class))).thenReturn(solicitudPrueba);
        
        String solicitudJson = objectMapper.writeValueAsString(solicitudPrueba);
        
        // Act & Assert
        mockMvc.perform(put("/api/solicitudes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(solicitudJson))
            .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("DELETE /api/solicitudes/{id} - Debe cancelar solicitud")
    void debeCancelarSolicitud() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/solicitudes/1"))
            .andExpect(status().isNoContent());
    }
    
    @Test
    @DisplayName("GET /api/solicitudes/cliente/{idCliente} - Debe retornar solicitudes por cliente")
    void debeRetornarSolicitudesPorCliente() throws Exception {
        // Arrange
        when(solicitudService.obtenerSolicitudesPorCliente(1L)).thenReturn(Arrays.asList(solicitudPrueba));
        
        // Act & Assert
        mockMvc.perform(get("/api/solicitudes/cliente/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    
    @Test
    @DisplayName("GET /api/solicitudes/estado/{estado} - Debe retornar solicitudes por estado")
    void debeRetornarSolicitudesPorEstado() throws Exception {
        // Arrange
        when(solicitudService.obtenerSolicitudesPorEstado(EstadoSolicitud.PENDIENTE))
            .thenReturn(Arrays.asList(solicitudPrueba));
        
        // Act & Assert
        mockMvc.perform(get("/api/solicitudes/estado/PENDIENTE"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    
    @Test
    @DisplayName("PUT /api/solicitudes/{id}/asignar/{idTecnico} - Debe asignar técnico")
    void debeAsignarTecnico() throws Exception {
        // Arrange
        when(solicitudService.asignarTecnico(1L, 2L)).thenReturn(solicitudPrueba);
        
        // Act & Assert
        mockMvc.perform(put("/api/solicitudes/1/asignar/2"))
            .andExpect(status().isOk());
    }
}