package es.rbp.tareas_borderia.entidad;

import java.io.Serializable;
import java.time.LocalDateTime;

import es.rbp.tareas_borderia.entidad.bbdd.UsuarioBBDD;

public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	private boolean admin;
	private boolean developer;
	private boolean cambiarPasswd;
	private boolean terminosAceptados;
	private boolean habilitado;

	private double bonificacion;

	private String token;
	private String nombre;

	private LocalDateTime fechaCreacion;
	private LocalDateTime ultimaAccion;

	public Usuario() {
	}

	public Usuario(UsuarioBBDD usuarioBBDD) {
		this.id = usuarioBBDD.getId();

		this.admin = usuarioBBDD.isAdmin();
		this.developer = usuarioBBDD.isDeveloper();
		this.cambiarPasswd = usuarioBBDD.isCambiarPasswd();
		this.terminosAceptados = usuarioBBDD.isTerminosAceptados();
		this.habilitado = usuarioBBDD.isHabilitado();
		
		this.bonificacion = usuarioBBDD.getBonificacion();

		this.nombre = usuarioBBDD.getNombre();
		this.token = usuarioBBDD.getToken();

		this.fechaCreacion = usuarioBBDD.getFechaCreacion();
		this.ultimaAccion = usuarioBBDD.getUltimaAccion();
	}

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

	public boolean isHabilitado() {
		return habilitado;
	}

	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}

	public double getBonificacion() {
		return bonificacion;
	}

	public void setBonificacion(double bonificacion) {
		this.bonificacion = bonificacion;
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

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public LocalDateTime getUltimaAccion() {
		return ultimaAccion;
	}

	public void setUltimaAccion(LocalDateTime ultimaAccion) {
		this.ultimaAccion = ultimaAccion;
	}
}
