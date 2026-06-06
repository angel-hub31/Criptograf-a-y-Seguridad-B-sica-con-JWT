package com.krakedev.jwt.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "username", nullable = false, unique = true, length = 50)
	private String username;

	@Column(name = "password", nullable = false, length = 100)
	private String password;

	@Column(name = "rol", nullable = false, length = 20)
	private String rol;

	// Constructor por defecto requerido por JPA
	public Usuario() {
	}

	// Constructor con parámetros para facilitar el registro
	public Usuario(String username, String password, String rol) {
		this.username = username;
		this.password = password;
		this.rol = rol;
	}

	// Getters y Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	// Método toString 
	@Override
	public String toString() {
		return "Usuario [id=" + id + ", username=" + username + ", rol=" + rol + "]";
	}
}