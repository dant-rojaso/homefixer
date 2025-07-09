package com.homefixer.usuarios.service;

import com.homefixer.usuarios.model.Cliente; // Modelo Cliente
import com.homefixer.usuarios.repository.ClienteRepository; // Repositorio Cliente
import org.junit.jupiter.api.*; // Anotaciones JUnit 5
import org.junit.jupiter.api.extension.ExtendWith; // Extensión para Mockito
import org.mockito.InjectMocks; // Inyección de mocks
import org.mockito.Mock; // Mock del repositorio
import org.mockito.junit.jupiter.MockitoExtension; // Extensión Mockito

import java.util.*; // Listas y Optional

import static org.junit.jupiter.api.Assertions.*; // Assertions de JUnit
import static org.mockito.Mockito.*; // Métodos de Mockito

@ExtendWith(MockitoExtension.class) // Habilita Mockito en JUnit 5
class ClienteServiceTest {

    @Mock // Mock del repositorio
    private ClienteRepository clienteRepository;

    @InjectMocks // Inyecta mocks en el servicio
    private ClienteService clienteService;

    private Cliente cliente; // Cliente de prueba

    @BeforeEach // Se ejecuta antes de cada test
    void setUp() {
        cliente = new Cliente(); // Crear cliente vacío
        cliente.setIdCliente(1L); // Asignar ID
        cliente.setNombre("Juan"); // Asignar nombre
        cliente.setApellido("Pérez"); // Asignar apellido
        cliente.setEmail("juan@email.com"); // Asignar email
        cliente.setTelefono("987654321"); // Asignar teléfono
        cliente.setDireccion("Av. Principal 123"); // Asignar dirección
    }

    @Test // Marca como test unitario
    void testObtenerTodosLosClientes() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente)); // Mock: devuelve lista con cliente

        var resultado = clienteService.obtenerTodosLosClientes(); // Ejecuta método

        assertEquals(1, resultado.size()); // Verifica tamaño de lista
        assertEquals("Juan", resultado.get(0).getNombre()); // Verifica nombre
        verify(clienteRepository, times(1)).findAll(); // Verifica llamada al repositorio
    }

    @Test // Marca como test unitario
    void testObtenerClientePorId() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente)); // Mock: devuelve cliente

        var resultado = clienteService.obtenerClientePorId(1L); // Ejecuta método

        assertTrue(resultado.isPresent()); // Verifica que existe
        assertEquals("Juan", resultado.get().getNombre()); // Verifica nombre
        verify(clienteRepository, times(1)).findById(1L); // Verifica llamada al repositorio
    }

    @Test // Marca como test unitario
    void testCrearCliente() {
        when(clienteRepository.existsByEmail("juan@email.com")).thenReturn(false); // Mock: email no existe
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente); // Mock: guarda cliente

        var resultado = clienteService.crearCliente(cliente); // Ejecuta método

        assertNotNull(resultado); // Verifica que no es null
        assertEquals("Juan", resultado.getNombre()); // Verifica nombre
        verify(clienteRepository, times(1)).save(cliente); // Verifica llamada al repositorio
    }

    @Test // Marca como test unitario
    void testCrearClienteEmailExistente() {
        when(clienteRepository.existsByEmail("juan@email.com")).thenReturn(true); // Mock: email ya existe

        assertThrows(RuntimeException.class, () -> clienteService.crearCliente(cliente)); // Verifica excepción
        verify(clienteRepository, never()).save(any(Cliente.class)); // Verifica que no se guarda
    }

    @Test // Marca como test unitario
    void testEliminarCliente() {
        when(clienteRepository.existsById(1L)).thenReturn(true); // Mock: cliente existe

        clienteService.eliminarCliente(1L); // Ejecuta método

        verify(clienteRepository, times(1)).deleteById(1L); // Verifica llamada al repositorio
    }
}