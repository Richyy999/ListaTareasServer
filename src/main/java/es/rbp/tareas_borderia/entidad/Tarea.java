package es.rbp.tareas_borderia.entidad;

import java.io.Serializable;

import es.rbp.tareas_borderia.entidad.bbdd.TareaBBDD;
import es.rbp.tareas_borderia.entidad.config.TareaConfig;

public class Tarea implements Serializable {

	private static final long serialVersionUID = 5L;

	private double precioSinPagar;
	private double precioPagado;

	private boolean cobrada;

	private String nombre;

	public Tarea() {
	}

	public Tarea(TareaBBDD tareaLimpiezaBBDD) {
		this.precioSinPagar = Math.round(tareaLimpiezaBBDD.getPrecioSinPagar() * 100.0) / 100.0;
		this.precioPagado = Math.round(tareaLimpiezaBBDD.getPrecioPagado() * 100.0) / 100.0;
		this.cobrada = tareaLimpiezaBBDD.isCobrada();
		this.nombre = tareaLimpiezaBBDD.getNombre();
	}

	public Tarea(TareaConfig tareaConfig) {
		this.precioSinPagar = tareaConfig.getPrecio();
		this.nombre = tareaConfig.getNombre();
	}

	public double getPrecioSinPagar() {
		return precioSinPagar;
	}

	public void setPrecioSinPagar(double precio) {
		this.precioSinPagar = precio;
	}

	public double getPrecioPagado() {
		return precioPagado;
	}

	public void setPrecioPagado(double precioPagado) {
		this.precioPagado = precioPagado;
	}

	public boolean isCobrada() {
		return cobrada;
	}

	public void setCobrada(boolean cobrada) {
		this.cobrada = cobrada;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
