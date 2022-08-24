package es.rbp.tareas_borderia.entidad.config;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import es.rbp.tareas_borderia.entidad.bbdd.HabitacionConfigBBDD;

public class HabitacionConfig implements Serializable {

	private static final long serialVersionUID = 11L;

	private long id;

	private String nombre;
	private String userAlta;
	private String userMod;

	private LocalDateTime fechaAlta;
	private LocalDateTime fechaMod;

	private List<TareaConfig> tareas;

	public HabitacionConfig() {
	}

	public HabitacionConfig(HabitacionConfigBBDD habitacionConfigBBDD, List<TareaConfig> tareas) {
		this.id = habitacionConfigBBDD.getId();

		this.nombre = habitacionConfigBBDD.getNombre();

		this.tareas = tareas;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUserAlta() {
		return userAlta;
	}

	public void setUserAlta(String userAlta) {
		this.userAlta = userAlta;
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

	public List<TareaConfig> getTareas() {
		return tareas;
	}

	public void setTareas(List<TareaConfig> tareas) {
		this.tareas = tareas;
	}
}
