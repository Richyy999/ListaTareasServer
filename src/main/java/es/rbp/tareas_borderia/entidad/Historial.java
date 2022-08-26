package es.rbp.tareas_borderia.entidad;

import java.io.Serializable;
import java.time.LocalDateTime;

import es.rbp.tareas_borderia.entidad.bbdd.HistorialBBDD;
import es.rbp.tareas_borderia.entidad.config.HabitacionConfig;

public class Historial implements Serializable {

	private static final long serialVersionUID = 7L;

	private long id;

	private String nombreHabitacion;

	private LocalDateTime ultimaLimpieza;

	private Usuario usuario;

	private HabitacionConfig habitacionConfig;

	public Historial() {

	}

	public Historial(HistorialBBDD historialBBDD, Usuario usuario, HabitacionConfig habitacionConfig) {
		this.id = historialBBDD.getId();

		this.nombreHabitacion = historialBBDD.getNombreHabitacion();

		this.ultimaLimpieza = historialBBDD.getUltimaLimpieza();

		this.usuario = usuario;

		this.habitacionConfig = habitacionConfig;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombreHabitacion() {
		return nombreHabitacion;
	}

	public void setNombreHabitacion(String nombreHabitacion) {
		this.nombreHabitacion = nombreHabitacion;
	}

	public LocalDateTime getUltimaLimpieza() {
		return ultimaLimpieza;
	}

	public void setUltimaLimpieza(LocalDateTime ultimaLimpieza) {
		this.ultimaLimpieza = ultimaLimpieza;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public HabitacionConfig getHabitacionConfig() {
		return habitacionConfig;
	}

	public void setHabitacionConfig(HabitacionConfig habitacionConfig) {
		this.habitacionConfig = habitacionConfig;
	}
}
