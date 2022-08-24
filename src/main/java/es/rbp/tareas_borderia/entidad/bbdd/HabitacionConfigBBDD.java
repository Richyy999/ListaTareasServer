package es.rbp.tareas_borderia.entidad.bbdd;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "habitacion_conf")
public class HabitacionConfigBBDD {
	
	public static final long ID_HABITACION_MUESTRA = 0;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "nombre", nullable = false, unique = true)
	private String nombre;
	@Column(name = "user_alta", nullable = false)
	private String userAlta;
	@Column(name = "user_mod", nullable = false)
	private String userMod;

	@CreationTimestamp
	@Column(name = "fecha_alta", nullable = false)
	private LocalDateTime fechaAlta;
	@UpdateTimestamp
	@Column(name = "fecha_mod", nullable = false)
	private LocalDateTime fechaMod;

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
}
