package es.rbp.tareas_borderia.entidad;

import java.io.Serializable;
import java.util.List;

public class IDWrapper implements Serializable {

	private static final long serialVersionUID = 7L;

	private List<Long> ids;

	public IDWrapper() {
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
}
