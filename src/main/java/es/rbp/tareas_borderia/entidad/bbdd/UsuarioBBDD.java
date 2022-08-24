package es.rbp.tareas_borderia.entidad.bbdd;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "usuario")
public class UsuarioBBDD {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "admin", nullable = false)
	private boolean admin;
	@Column(name = "developer", nullable = false)
	private boolean developer;
	@Column(name = "cambiar_pwd", nullable = false)
	private boolean cambiarPasswd;
	@Column(name = "terminos_aceptados", nullable = false)
	private boolean terminosAceptados;

	@Column(name = "token", unique = true)
	private String token;
	@Column(name = "nombre", nullable = false, unique = true)
	private String nombre;
	@Column(name = "paswwd", nullable = false)
	private String contrasena;

	@CreationTimestamp
	@Column(name = "fecha_alta", nullable = false)
	private LocalDateTime fechaCreacion;
	@UpdateTimestamp
	@Column(name = "ultima_peticion", nullable = false)
	private LocalDateTime ultimoLogin;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isDeveloper() {
		return developer;
	}

	public void setDeveloper(boolean developer) {
		this.developer = developer;
	}

	public boolean isCambiarPasswd() {
		return cambiarPasswd;
	}

	public void setCambiarPasswd(boolean cambiarPasswd) {
		this.cambiarPasswd = cambiarPasswd;
	}

	public boolean isTerminosAceptados() {
		return terminosAceptados;
	}

	public void setTerminosAceptados(boolean terminosAceptados) {
		this.terminosAceptados = terminosAceptados;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public LocalDateTime getUltimoLogin() {
		return ultimoLogin;
	}

	public void setUltimoLogin(LocalDateTime ultimoLogin) {
		this.ultimoLogin = ultimoLogin;
	}
}
