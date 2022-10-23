package es.rbp.tareas_borderia.entidad;

import java.io.Serializable;

import es.rbp.tareas_borderia.entidad.bbdd.TerminoBBDD;

public class Termino implements Serializable {

	private static final long serialVersionUID = 9L;

	private int orden;

	private String titulo;
	private String descripcion;

	public Termino() {

	}

	public Termino(TerminoBBDD terminoBBDD) {
		this.orden = terminoBBDD.getOrden();

		this.titulo = terminoBBDD.getTitulo();
		this.descripcion = terminoBBDD.getDescripcion();
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
