package com.homefixer.autenticacion.service;

import com.homefixer.autenticacion.model.Token; // Importa entidad Token
import com.homefixer.autenticacion.repository.TokenRepository; // Importa repositorio
import lombok.RequiredArgsConstructor; // Constructor automático
import org.springframework.stereotype.Service; // Anotación servicio
import java.time.LocalDateTime; // Para fechas
import java.util.List; // Para listas
import java.util.Optional; // Para opcionales
import java.util.UUID; // Para generar tokens únicos

@Service // Marca como servicio Spring
@RequiredArgsConstructor // Constructor automático
public class TokenService {
    
    private final TokenRepository tokenRepository; // Repositorio inyectado
    
    // Generar token de login
    public Token generarTokenLogin(Long idUsuario, String ipOrigen, String userAgent) {
        System.out.println("🔐 Generando token de login para usuario: " + idUsuario); // Log inicio
        
        // Revocar tokens de login anteriores del usuario
        List<Token> tokensAnteriores = tokenRepository.findByIdUsuarioAndTipo(idUsuario, Token.TipoToken.LOGIN);
        for (Token tokenAnterior : tokensAnteriores) {
            tokenAnterior.setActivo(false); // Desactiva tokens previos
            tokenRepository.save(tokenAnterior); // Guarda cambio
        }
        
        // Crear nuevo token
        Token nuevoToken = Token.builder()
            .idUsuario(idUsuario) // Usuario propietario
            .token(generarStringToken()) // Token único generado
            .tipo(Token.TipoToken.LOGIN) // Tipo login
            .fechaCreacion(LocalDateTime.now()) // Fecha actual
            .fechaExpiracion(LocalDateTime.now().plusHours(24)) // Expira en 24 horas
            .activo(true) // Token activo
            .ipOrigen(ipOrigen) // IP del cliente
            .userAgent(userAgent) // Navegador del cliente
            .build(); // Construye objeto
        
        Token tokenGuardado = tokenRepository.save(nuevoToken); // Guarda en BD
        System.out.println("✅ Token de login creado con ID: " + tokenGuardado.getIdToken()); // Log éxito
        
        return tokenGuardado; // Retorna token creado
    }
    
    // Generar token de refresh
    public Token generarTokenRefresh(Long idUsuario) {
        System.out.println("🔄 Generando token refresh para usuario: " + idUsuario); // Log inicio
        
        Token tokenRefresh = Token.builder()
            .idUsuario(idUsuario) // Usuario propietario
            .token(generarStringToken()) // Token único
            .tipo(Token.TipoToken.REFRESH) // Tipo refresh
            .fechaCreacion(LocalDateTime.now()) // Fecha actual
            .fechaExpiracion(LocalDateTime.now().plusDays(7)) // Expira en 7 días
            .activo(true) // Token activo
            .build(); // Construye objeto
        
        Token guardado = tokenRepository.save(tokenRefresh); // Guarda en BD
        System.out.println("✅ Token refresh creado"); // Log éxito
        
        return guardado; // Retorna token
    }
    
    // Validar token
    public boolean validarToken(String token) {
        System.out.println("🔍 Validando token: " + token.substring(0, 10) + "..."); // Log validación (solo primeros 10 chars)
        
        Optional<Token> tokenOpt = tokenRepository.findByTokenAndActivoTrue(token); // Busca token activo
        
        if (tokenOpt.isEmpty()) {
            System.out.println("❌ Token no encontrado o inactivo"); // Log error
            return false; // Token no existe o está inactivo
        }
        
        Token tokenBD = tokenOpt.get(); // Obtiene token de BD
        
        // Verificar si está expirado
        if (tokenBD.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            System.out.println("❌ Token expirado"); // Log error
            tokenBD.setActivo(false); // Desactiva token expirado
            tokenRepository.save(tokenBD); // Guarda cambio
            return false; // Token expirado
        }
        
        System.out.println("✅ Token válido"); // Log éxito
        return true; // Token válido
    }
    
    // Obtener usuario por token
    public Optional<Long> obtenerUsuarioPorToken(String token) {
        System.out.println("🔍 Obteniendo usuario por token"); // Log búsqueda
        
        Optional<Token> tokenOpt = tokenRepository.findByTokenAndActivoTrue(token); // Busca token activo
        
        if (tokenOpt.isPresent()) {
            Long idUsuario = tokenOpt.get().getIdUsuario(); // Obtiene ID usuario
            System.out.println("✅ Usuario encontrado: " + idUsuario); // Log éxito
            return Optional.of(idUsuario); // Retorna ID usuario
        }
        
        System.out.println("❌ No se encontró usuario para el token"); // Log error
        return Optional.empty(); // No se encontró usuario
    }
    
    // Revocar token
    public void revocarToken(String token) {
        System.out.println("🚫 Revocando token"); // Log revocación
        
        Optional<Token> tokenOpt = tokenRepository.findByToken(token); // Busca token
        
        if (tokenOpt.isPresent()) {
            Token tokenBD = tokenOpt.get(); // Obtiene token
            tokenBD.setActivo(false); // Desactiva token
            tokenRepository.save(tokenBD); // Guarda cambio
            System.out.println("✅ Token revocado exitosamente"); // Log éxito
        } else {
            System.out.println("❌ Token no encontrado para revocar"); // Log error
        }
    }
    
    // Revocar todos los tokens de un usuario
    public void revocarTodosTokensUsuario(Long idUsuario) {
        System.out.println("🚫 Revocando todos los tokens del usuario: " + idUsuario); // Log revocación
        
        List<Token> tokens = tokenRepository.findByIdUsuarioAndActivoTrue(idUsuario); // Busca tokens activos
        
        for (Token token : tokens) {
            token.setActivo(false); // Desactiva cada token
            tokenRepository.save(token); // Guarda cambio
        }
        
        System.out.println("✅ Revocados " + tokens.size() + " tokens"); // Log éxito
    }
    
    // Limpiar tokens expirados
    public void limpiarTokensExpirados() {
        System.out.println("🧹 Limpiando tokens expirados"); // Log limpieza
        
        List<Token> tokensExpirados = tokenRepository.encontrarTokensExpirados(LocalDateTime.now()); // Busca expirados
        
        for (Token token : tokensExpirados) {
            token.setActivo(false); // Desactiva token expirado
            tokenRepository.save(token); // Guarda cambio
        }
        
        System.out.println("✅ Limpiados " + tokensExpirados.size() + " tokens expirados"); // Log éxito
    }
    
    // Método privado para generar string único de token
    private String generarStringToken() {
        return "HF_" + UUID.randomUUID().toString().replace("-", "") + "_" + System.currentTimeMillis(); // Token único con prefijo HF
    }
    
    // Obtener todos los tokens de un usuario
    public List<Token> obtenerTokensUsuario(Long idUsuario) {
        System.out.println("📋 Obteniendo tokens del usuario: " + idUsuario); // Log consulta
        return tokenRepository.findByIdUsuario(idUsuario); // Retorna todos los tokens del usuario
    }
}