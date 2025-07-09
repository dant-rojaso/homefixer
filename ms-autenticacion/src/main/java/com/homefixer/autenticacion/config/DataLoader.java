package com.homefixer.autenticacion.config;

import com.homefixer.autenticacion.model.Autenticacion;
import com.homefixer.autenticacion.model.TipoUsuario;
import com.homefixer.autenticacion.model.EstadoSesion;
import com.homefixer.autenticacion.repository.AutenticacionRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
public class DataLoader implements CommandLineRunner {
    
    @Autowired
    private AutenticacionRepository autenticacionRepository;
    
    private final Faker faker = new Faker();
    private final Random random = new Random();
    
    @Override
    public void run(String... args) throws Exception {
        if (autenticacionRepository.count() == 0) {
            cargarDatosAutenticacion();
        }
    }
    
    private void cargarDatosAutenticacion() {
        // Crear autenticaciones de prueba
        for (int i = 1; i <= 20; i++) {
            Autenticacion autenticacion = new Autenticacion();
            autenticacion.setEmail(faker.internet().emailAddress());
            autenticacion.setContrasena("password123");
            autenticacion.setTipoUsuario(TipoUsuario.values()[random.nextInt(TipoUsuario.values().length)]);
            autenticacion.setIdUsuario((long) i);
            autenticacion.setFechaCreacion(LocalDateTime.now().minusDays(random.nextInt(30)));
            
            // Algunos con sesión activa
            if (i % 3 == 0) {
                autenticacion.setEstadoSesion(EstadoSesion.ACTIVA);
                autenticacion.setTokenSesion(faker.internet().uuid());
                autenticacion.setUltimoLogin(LocalDateTime.now().minusHours(random.nextInt(24)));
                autenticacion.setFechaExpiracion(LocalDateTime.now().plusHours(24));
            } else {
                autenticacion.setEstadoSesion(EstadoSesion.INACTIVA);
            }
            
            autenticacion.setObservaciones(faker.lorem().sentence());
            autenticacionRepository.save(autenticacion);
        }
        
        System.out.println("Datos de autenticación cargados: 20 registros");
    }
}