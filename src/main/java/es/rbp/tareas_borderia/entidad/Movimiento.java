package es.rbp.tareas_borderia.entidad;

import java.io.Serializable;
import java.time.LocalDateTime;

import es.rbp.tareas_borderia.entidad.bbdd.MovimientoBBDD;

public class Movimiento implements Serializable {

	private static final long serialVersionUID = 10L;

	public static final int TIPO_COBRO = 1;
	public static final int TIPO_DEUDA = 2;

	private int tipoMovi;

	private double cantidad;
	private double saldo;

	private LocalDateTime fechaMovi;

	public Movimiento() {

	}

	public Movimiento(MovimientoBBDD movimientoBBDD) {
		this.tipoMovi = movimientoBBDD.getTipoMovi();

		this.cantidad = movimientoBBDD.getCantidad();
		this.saldo = movimientoBBDD.getSaldo();

		this.fechaMovi = movimientoBBDD.getFechaMovi();
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

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public LocalDateTime getFechaMovi() {
		return fechaMovi;
	}

	public void setFechaMovi(LocalDateTime fechaMovi) {
		this.fechaMovi = fechaMovi;
	}
}
