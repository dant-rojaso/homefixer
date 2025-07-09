package com.homefixer.usuarios.service;

import com.homefixer.usuarios.model.Tecnico; // Modelo Técnico
import com.homefixer.usuarios.repository.TecnicoRepository; // Repositorio Técnico
import org.junit.jupiter.api.*; // Anotaciones JUnit 5
import org.junit.jupiter.api.extension.ExtendWith; // Extensión para Mockito
import org.mockito.InjectMocks; // Inyección de mocks
import org.mockito.Mock; // Mock del repositorio
import org.mockito.junit.jupiter.MockitoExtension; // Extensión Mockito

import java.util.*; // Listas y Optional

import static org.junit.jupiter.api.Assertions.*; // Assertions de JUnit
import static org.mockito.Mockito.*; // Métodos de Mockito

@ExtendWith(MockitoExtension.class) // Habilita Mockito en JUnit 5
class TecnicoServiceTest {

    @Mock // Mock del repositorio
    private TecnicoRepository tecnicoRepository;

    @InjectMocks // Inyecta mocks en el servicio
    private TecnicoService tecnicoService;

    private Tecnico tecnico; // Técnico de prueba

    @BeforeEach // Se ejecuta antes de cada test
    void setUp() {
        tecnico = new Tecnico(); // Crear técnico vacío con constructor sin parámetros
        tecnico.setIdTecnico(1L); // Asignar ID
        tecnico.setNombre("Carlos"); // Asignar nombre
        tecnico.setApellido("González"); // Asignar apellido
        tecnico.setEmail("carlos@email.com"); // Asignar email
        tecnico.setTelefono("987654321"); // Asignar teléfono
        tecnico.setEspecialidad("Electricidad"); // Asignar especialidad
        tecnico.setDisponible(true); // Asignar disponibilidad
    }

    @Test // Marca como test unitario
    void testObtenerTodosLosTecnicos() {
        when(tecnicoRepository.findAll()).thenReturn(List.of(tecnico)); // Mock: devuelve lista con técnico

        var resultado = tecnicoService.obtenerTodosLosTecnicos(); // Ejecuta método

        assertEquals(1, resultado.size()); // Verifica tamaño de lista
        assertEquals("Carlos", resultado.get(0).getNombre()); // Verifica nombre
        verify(tecnicoRepository, times(1)).findAll(); // Verifica llamada al repositorio
    }

    @Test // Marca como test unitario
    void testObtenerTecnicoPorId() {
        when(tecnicoRepository.findById(1L)).thenReturn(Optional.of(tecnico)); // Mock: devuelve técnico

        var resultado = tecnicoService.obtenerTecnicoPorId(1L); // Ejecuta método

        assertTrue(resultado.isPresent()); // Verifica que existe
        assertEquals("Carlos", resultado.get().getNombre()); // Verifica nombre
        verify(tecnicoRepository, times(1)).findById(1L); // Verifica llamada al repositorio
    }

    @Test // Marca como test unitario
    void testObtenerTecnicosDisponibles() {
        when(tecnicoRepository.findByDisponible(true)).thenReturn(List.of(tecnico)); // Mock: devuelve técnicos disponibles

        var resultado = tecnicoService.obtenerTecnicosDisponibles(); // Ejecuta método

        assertEquals(1, resultado.size()); // Verifica tamaño de lista
        assertTrue(resultado.get(0).getDisponible()); // Verifica disponibilidad
        verify(tecnicoRepository, times(1)).findByDisponible(true); // Verifica llamada al repositorio
    }

    @Test // Marca como test unitario
    void testCrearTecnico() {
        when(tecnicoRepository.existsByEmail("carlos@email.com")).thenReturn(false); // Mock: email no existe
        when(tecnicoRepository.save(any(Tecnico.class))).thenReturn(tecnico); // Mock: guarda técnico

        var resultado = tecnicoService.crearTecnico(tecnico); // Ejecuta método

        assertNotNull(resultado); // Verifica que no es null
        assertEquals("Carlos", resultado.getNombre()); // Verifica nombre
        verify(tecnicoRepository, times(1)).save(tecnico); // Verifica llamada al repositorio
    }

    @Test // Marca como test unitario
    void testCrearTecnicoEmailExistente() {
        when(tecnicoRepository.existsByEmail("carlos@email.com")).thenReturn(true); // Mock: email ya existe

        assertThrows(RuntimeException.class, () -> tecnicoService.crearTecnico(tecnico)); // Verifica excepción
        verify(tecnicoRepository, never()).save(any(Tecnico.class)); // Verifica que no se guarda
    }

    @Test // Marca como test unitario
    void testEliminarTecnico() {
        when(tecnicoRepository.existsById(1L)).thenReturn(true); // Mock: técnico existe

        tecnicoService.eliminarTecnico(1L); // Ejecuta método

        verify(tecnicoRepository, times(1)).deleteById(1L); // Verifica llamada al repositorio
    }
}