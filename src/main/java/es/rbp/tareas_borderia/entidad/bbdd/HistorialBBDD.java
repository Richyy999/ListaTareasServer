package es.rbp.tareas_borderia.entidad.bbdd;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "historial")
public class HistorialBBDD {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "id_habitacion_config", nullable = false)
	private long idHabitacionConfig;
	@Column(name = "id_usuario")
	private long idUsuario;

	@Column(name = "nombre", nullable = false)
	private String nombreHabitacion;

	@Column(name = "ultima_limpieza")
	private LocalDateTime ultimaLimpieza;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdHabitacionConfig() {
		return idHabitacionConfig;
	}

	public void setIdHabitacionConfig(long idHabitacionConfig) {
		this.idHabitacionConfig = idHabitacionConfig;
	}

	public long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(long idUsuario) {
		this.idUsuario = idUsuario;
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
}
