package es.rbp.tareas_borderia.entidad.bbdd;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "deuda")
public class DeudaBBDD {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "id_usuario", nullable = false)
	private long idUsuario;

	@Column(name = "deuda", nullable = false)
	private double deuda;
	@Column(name = "deuda_max", nullable = false)
	private double deudaMax;

	@Column(name = "acumular", nullable = false)
	private boolean acumular;

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

	public double getDeuda() {
		return deuda;
	}

	public void setDeuda(double deuda) {
		this.deuda = deuda;
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
}
