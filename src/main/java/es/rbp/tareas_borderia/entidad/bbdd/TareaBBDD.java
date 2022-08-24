package es.rbp.tareas_borderia.entidad.bbdd;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tarea_limpieza")
public class TareaBBDD {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "id_habitacion", nullable = false)
	private long idHabitacion;

	@Column(name = "precioSinPagar", nullable = false)
	private double precioSinPagar;
	@Column(name = "precioPagado", nullable = false)
	private double precioPagado;

	@Column(name = "cobrada", nullable = false)
	private boolean cobrada;

	@Column(name = "nombre", nullable = false)
	private String nombre;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdHabitacion() {
		return idHabitacion;
	}

	public void setIdHabitacion(long idHabitacion) {
		this.idHabitacion = idHabitacion;
	}

	public double getPrecioSinPagar() {
		return precioSinPagar;
	}

	public void setPrecioSinPagar(double precioSinPagar) {
		this.precioSinPagar = precioSinPagar;
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
