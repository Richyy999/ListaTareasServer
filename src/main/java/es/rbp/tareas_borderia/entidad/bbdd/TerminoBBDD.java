package es.rbp.tareas_borderia.entidad.bbdd;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "terminos")
public class TerminoBBDD {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private int orden;

	@Column(name = "titulo", nullable = false, unique = true)
	private String titulo;
	@Column(name = "descripcion", nullable = false)
	private String descripcion;

	public TerminoBBDD() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
