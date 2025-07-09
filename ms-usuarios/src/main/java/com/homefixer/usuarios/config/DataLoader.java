package com.homefixer.usuarios.config;

import com.github.javafaker.Faker; // Librería para generar datos falsos
import com.homefixer.usuarios.model.Cliente; // Modelo Cliente
import com.homefixer.usuarios.model.Tecnico; // Modelo Técnico
import com.homefixer.usuarios.repository.ClienteRepository; // Repositorio Cliente
import com.homefixer.usuarios.repository.TecnicoRepository; // Repositorio Técnico
import lombok.RequiredArgsConstructor; // Inyección por constructor
import lombok.extern.slf4j.Slf4j; // Logger
import org.springframework.boot.CommandLineRunner; // Se ejecuta al iniciar la app
import org.springframework.stereotype.Component; // Marca como componente de Spring

import java.util.Locale; // Configuración regional

@Component // Marca como componente de Spring
@RequiredArgsConstructor // Genera constructor con campos final
@Slf4j // Genera logger automáticamente
public class DataLoader implements CommandLineRunner { // Se ejecuta al iniciar la aplicación

    private final ClienteRepository clienteRepository; // Repositorio de clientes inyectado
    private final TecnicoRepository tecnicoRepository; // Repositorio de técnicos inyectado
    private final Faker faker = new Faker(Locale.forLanguageTag("es")); // Faker en español (forma moderna)

    @Override // Sobrescribe método de CommandLineRunner
    public void run(String... args) { // Se ejecuta automáticamente al iniciar
        log.info("Iniciando carga de datos con Faker..."); // Log de inicio
        
        cargarClientes(); // Carga clientes de prueba
        cargarTecnicos(); // Carga técnicos de prueba
        
        log.info("Carga de datos completada exitosamente"); // Log de finalización
    }

    private void cargarClientes() { // Método para cargar clientes
        if (clienteRepository.count() == 0) { // Solo carga si no hay datos
            log.info("Creando clientes de prueba..."); // Log informativo
            
            for (int i = 1; i <= 10; i++) { // Crea 10 clientes
                Cliente cliente = new Cliente(); // Cliente vacío
                cliente.setNombre(faker.name().firstName()); // Nombre aleatorio
                cliente.setApellido(faker.name().lastName()); // Apellido aleatorio
                cliente.setEmail(faker.internet().emailAddress()); // Email único
                cliente.setTelefono(generarTelefono()); // Teléfono chileno válido
                cliente.setDireccion(faker.address().streetAddress() + ", " + faker.address().city()); // Dirección
                
                clienteRepository.save(cliente); // Guarda en base de datos
                log.debug("Cliente creado: {} {}", cliente.getNombre(), cliente.getApellido()); // Log detallado
            }
            
            log.info("{} clientes creados exitosamente", clienteRepository.count()); // Log resumen
        } else {
            log.info("Ya existen clientes en la base de datos, omitiendo carga"); // Log si ya hay datos
        }
    }

    private void cargarTecnicos() { // Método para cargar técnicos
        if (tecnicoRepository.count() == 0) { // Solo carga si no hay datos
            log.info("Creando técnicos de prueba..."); // Log informativo
            
            String[] especialidades = { // Array de especialidades
                "Electricidad", "Plomería", "Carpintería", "Pintura", 
                "Cerrajería", "Gasfitería", "Climatización", "Jardinería"
            };
            
            for (int i = 1; i <= 15; i++) { // Crea 15 técnicos
                Tecnico tecnico = new Tecnico(); // Técnico vacío
                tecnico.setNombre(faker.name().firstName()); // Nombre aleatorio
                tecnico.setApellido(faker.name().lastName()); // Apellido aleatorio
                tecnico.setEmail(faker.internet().emailAddress()); // Email único
                tecnico.setTelefono(generarTelefono()); // Teléfono chileno válido
                tecnico.setEspecialidad(especialidades[faker.random().nextInt(especialidades.length)]); // Especialidad aleatoria
                tecnico.setDisponible(faker.bool().bool()); // Disponibilidad aleatoria
                
                tecnicoRepository.save(tecnico); // Guarda en base de datos
                log.debug("Tecnico creado: {} {} - {}", tecnico.getNombre(), tecnico.getApellido(), tecnico.getEspecialidad()); // Log detallado
            }
            
            log.info("{} tecnicos creados exitosamente", tecnicoRepository.count()); // Log resumen
        } else {
            log.info("Ya existen técnicos en la base de datos, omitiendo carga"); // Log si ya hay datos
        }
    }

    private String generarTelefono() { // Método para generar teléfonos chilenos válidos
        return "9" + faker.number().digits(8); // Formato 9XXXXXXXX (Chile)
    }
}