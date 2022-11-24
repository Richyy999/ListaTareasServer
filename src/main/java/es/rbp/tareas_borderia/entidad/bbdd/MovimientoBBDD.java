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
@Table(name = "movimiento")
public class MovimientoBBDD {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "id_usuario", nullable = false)
	private long idUsuario;

	@Column(name = "tipo_movi", nullable = false)
	private int tipoMovi;

	@Column(name = "cantidad", nullable = false)
	private double cantidad;
	@Column(name = "saldo", nullable = false)
	private double saldo;

	@Column(name = "fecha_movi", nullable = false)
	@CreationTimestamp
	private LocalDateTime fechaMovi;

	public MovimientoBBDD() {

	}

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

	public int getTipoMovi() {
		return tipoMovi;
	}

	public void setTipoMovi(int tipoMovi) {
		this.tipoMovi = tipoMovi;
	}

	public double getCantidad() {
		return cantidad;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public LocalDateTime getFechaMovi() {
		return fechaMovi;
	}

	public void setFechaMovi(LocalDateTime fechaMovi) {
		this.fechaMovi = fechaMovi;
	}
}
