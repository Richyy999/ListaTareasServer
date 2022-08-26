package es.rbp.tareas_borderia.entidad.config;

import java.io.Serializable;
import java.time.LocalDateTime;

import es.rbp.tareas_borderia.entidad.bbdd.TareaConfigBBDD;

public class TareaConfig implements Serializable {

	private static final long serialVersionUID = 12L;

	private long id;

	private double precio;

	private String nombre;
	private String userAlta;
	private String userMod;

	private LocalDateTime fechaAlta;
	private LocalDateTime fechaMod;

	public TareaConfig() {
	}

	public TareaConfig(TareaConfigBBDD tareaLimpiezaConfigBBDD) {
		this.id = tareaLimpiezaConfigBBDD.getId();

		this.precio = tareaLimpiezaConfigBBDD.getPrecio();

		this.nombre = tareaLimpiezaConfigBBDD.getNombre();
		this.userAlta = tareaLimpiezaConfigBBDD.getUserAlta();
		this.userMod = tareaLimpiezaConfigBBDD.getUserMod();

		this.fechaAlta = tareaLimpiezaConfigBBDD.getFechaAlta();
		this.fechaMod = tareaLimpiezaConfigBBDD.getFechaMod();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getNombre() {
		return nombre;
	}

	public String getUserAlta() {
		return userAlta;
	}

	public void setUserAlta(String userAlta) {
		this.userAlta = userAlta;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUserMod() {
		return userMod;
	}

	public void setUserMod(String userMod) {
		this.userMod = userMod;
	}

	public LocalDateTime getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(LocalDateTime fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public LocalDateTime getFechaMod() {
		return fechaMod;
	}

	public void setFechaMod(LocalDateTime fechaMod) {
		this.fechaMod = fechaMod;
	}
}
