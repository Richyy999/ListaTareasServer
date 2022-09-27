package es.rbp.tareas_borderia.entidad;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import es.rbp.tareas_borderia.entidad.bbdd.HabitacionBBDD;
import es.rbp.tareas_borderia.entidad.config.HabitacionConfig;
import es.rbp.tareas_borderia.entidad.config.TareaConfig;

public class Habitacion implements Serializable {

	private static final long serialVersionUID = 4L;

	private long id;

	private String nombre;

	private LocalDateTime fechaLimpieza;

	private List<Tarea> tareas;

	public Habitacion() {
	}

	public Habitacion(HabitacionBBDD habitacionBBDD, List<Tarea> tareas) {
		this.id = habitacionBBDD.getId();
		this.nombre = habitacionBBDD.getNombre();
		this.fechaLimpieza = habitacionBBDD.getFechaLimpieza();

		this.tareas = tareas;
	}

	public Habitacion(HabitacionConfig habitacionConfig) {
		this.id = habitacionConfig.getId();
		this.nombre = habitacionConfig.getNombre();

		this.tareas = new ArrayList<>();
		for (TareaConfig tareaConfig : habitacionConfig.getTareas()) {
			this.tareas.add(new Tarea(tareaConfig));
		}
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

	public LocalDateTime getFechaLimpieza() {
		return fechaLimpieza;
	}

	public void setFechaLimpieza(LocalDateTime fechaLimpieza) {
		this.fechaLimpieza = fechaLimpieza;
	}

	public List<Tarea> getTareas() {
		return tareas;
	}

	public void setTareas(List<Tarea> tareas) {
		this.tareas = tareas;
	}
}
