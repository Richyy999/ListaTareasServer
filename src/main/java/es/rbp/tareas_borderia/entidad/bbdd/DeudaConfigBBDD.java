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
@Table(name = "deuda_conf")
public class DeudaConfigBBDD {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "deuda_max", nullable = false)
	private double deudaMax;

	@Column(name = "acumular", nullable = false)
	private boolean acumular;

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

	public double getDeudaMax() {
		return deudaMax;
	}

	public void setDeudaMax(double deudaMax) {
		this.deudaMax = deudaMax;
	}

	public boolean isAcumular() {
		return acumular;
	}

	public void setAcumular(boolean acumular) {
		this.acumular = acumular;
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
