package es.rbp.tareas_borderia.entidad.bbdd;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "mes")
public class MesBBDD implements Comparable<MesBBDD> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "id_usuario", nullable = false)
	private long idUsuario;

	@CreationTimestamp
	@Column(name = "fecha", nullable = false)
	private LocalDateTime fecha;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public Date getDate() {
		return Date.from(fecha.atZone(ZoneId.systemDefault()).toInstant());
	}

	@Override
	public int compareTo(MesBBDD o) {
		return getDate().compareTo(getDate());
	}
}
