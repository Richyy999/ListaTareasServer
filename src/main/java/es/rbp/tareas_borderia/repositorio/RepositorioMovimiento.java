package es.rbp.tareas_borderia.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.rbp.tareas_borderia.entidad.bbdd.MovimientoBBDD;

public interface RepositorioMovimiento extends JpaRepository<MovimientoBBDD, Long> {

	List<MovimientoBBDD> findByIdUsuario(long idUsuario);
}
