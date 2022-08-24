package es.rbp.tareas_borderia.entidad;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import es.rbp.tareas_borderia.entidad.bbdd.MesBBDD;

public class Mes implements Serializable {

	private static final long serialVersionUID = 2L;

	private LocalDateTime fecha;

	private List<Habitacion> habitaciones;

	public Mes() {
	}

	public Mes(MesBBDD mesBBDD, List<Habitacion> habitaciones) {
		this.fecha = mesBBDD.getFecha();

		this.habitaciones = habitaciones;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public List<Habitacion> getHabitaciones() {
		return habitaciones;
	}

	public void setHabitaciones(List<Habitacion> habitaciones) {
		this.habitaciones = habitaciones;
	}
}
