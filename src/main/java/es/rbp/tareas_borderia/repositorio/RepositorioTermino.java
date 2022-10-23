package es.rbp.tareas_borderia.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import es.rbp.tareas_borderia.entidad.bbdd.TerminoBBDD;

public interface RepositorioTermino extends JpaRepository<TerminoBBDD, Long> {

	public TerminoBBDD findByTitulo(String titulo);
}
