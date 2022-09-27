package es.rbp.tareas_borderia.entidad.bbdd;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "habitacion")
public class HabitacionBBDD {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "id_mes", nullable = false)
	private long idMes;

	@Column(name = "nombre", nullable = false)
	private String nombre;

	@CreationTimestamp
	@Column(name = "fecha_limpieza", nullable = false)
	private LocalDateTime fechaLimpieza;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdMes() {
		return idMes;
	}

	public void setIdMes(long idTarea) {
		this.idMes = idTarea;
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
}
