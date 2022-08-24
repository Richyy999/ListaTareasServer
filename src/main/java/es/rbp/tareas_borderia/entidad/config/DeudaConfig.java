package es.rbp.tareas_borderia.entidad.config;

import java.io.Serializable;

import es.rbp.tareas_borderia.entidad.bbdd.DeudaConfigBBDD;

public class DeudaConfig implements Serializable {

	private static final long serialVersionUID = 13L;

	private long id;

	private double deudaMax;

	private boolean acumular;

	public DeudaConfig() {
	}

	public DeudaConfig(DeudaConfigBBDD deudaConfigBBDD) {
		this.id = deudaConfigBBDD.getId();

		this.deudaMax = deudaConfigBBDD.getDeudaMax();

		this.acumular = deudaConfigBBDD.isAcumular();
	}

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
}
