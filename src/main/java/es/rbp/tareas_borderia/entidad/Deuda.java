package es.rbp.tareas_borderia.entidad;

import java.io.Serializable;

import es.rbp.tareas_borderia.entidad.bbdd.DeudaBBDD;

public class Deuda implements Serializable {

	private static final long serialVersionUID = 6L;

	private double deuda;
	private double deudaMax;

	private boolean acumular;

	public Deuda() {
	}

	public Deuda(DeudaBBDD deudaBBDD) {
		this.deuda = deudaBBDD.getDeuda();
		this.deudaMax = deudaBBDD.getDeudaMax();

		this.acumular = deudaBBDD.isAcumular();
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
