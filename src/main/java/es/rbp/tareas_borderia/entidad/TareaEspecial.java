package es.rbp.tareas_borderia.entidad;

import java.io.Serializable;

import es.rbp.tareas_borderia.entidad.bbdd.Codigo;

public class TareaEspecial implements Serializable {

	private static final long serialVersionUID = 7L;

	private Codigo codigo;

	private Habitacion habitacion;

	public TareaEspecial() {

	}

	public Codigo getCodigo() {
		return codigo;
	}

	public void setCodigo(Codigo codigo) {
		this.codigo = codigo;
	}

	public Habitacion getHabitacion() {
		return habitacion;
	}

	public void setHabitacion(Habitacion habitacion) {
		this.habitacion = habitacion;
	}
}
