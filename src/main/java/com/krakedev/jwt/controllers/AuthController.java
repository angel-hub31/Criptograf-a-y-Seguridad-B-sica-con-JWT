package com.krakedev.jwt.controllers;

import com.krakedev.jwt.entidades.Usuario;
import com.krakedev.jwt.services.UsuarioService;
import com.krakedev.jwt.utils.JwtUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private UsuarioService usuarioService;

	// Endpoint para registrar en texto plano
	@PostMapping("/registrar/vulnerable")
	public ResponseEntity<Usuario> registrarVulnerable(@RequestBody Usuario usuario) {
		return ResponseEntity.ok(usuarioService.registrarTextoPlano(usuario));
	}

	// Endpoint oficial de registro usando BCrypt 
	@PostMapping("/registrar")
	public ResponseEntity<Usuario> registrarSeguro(@RequestBody Usuario usuario) {
		return ResponseEntity.ok(usuarioService.registrarConBCrypt(usuario));
	}

	// Endpoint de Login que retorna un JSON estructurado con el Token JWT 
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
		String username = credenciales.get("username");
		String password = credenciales.get("password");

		Optional<Usuario> usuarioOpt = usuarioService.login(username, password);

		if (usuarioOpt.isPresent()) {
			Usuario usuario = usuarioOpt.get();
			String token = JwtUtil.generarToken(usuario.getUsername(), usuario.getRol());

			Map<String, String> respuesta = new HashMap<>();
			respuesta.put("username", usuario.getUsername());
			respuesta.put("rol", usuario.getRol());
			respuesta.put("token", token);

			return ResponseEntity.ok(respuesta);
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
	}

	@GetMapping("/perfil")
	public ResponseEntity<?> verPerfil(@RequestHeader(value = "Authorization", required = false) String authHeader) {

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("Falta la cabecera Authorization o el formato es incorrecto");
		}

		String token = authHeader.substring(7);

		try {
			DecodedJWT decodedJWT = JwtUtil.verificarToken(token);
			String username = decodedJWT.getSubject();
			String rol = decodedJWT.getClaim("rol").asString();

			String saludo = "Bienvenido " + username + ". Tu rol en el refugio es: " + rol;
			return ResponseEntity.ok(saludo);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("Acceso denegado: Firma digital rota o token inválido.");
		}
	}
}