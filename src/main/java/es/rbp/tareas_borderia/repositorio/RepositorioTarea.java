package es.rbp.tareas_borderia.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.rbp.tareas_borderia.entidad.bbdd.TareaBBDD;

public interface RepositorioTarea extends JpaRepository<TareaBBDD, Long> {

	List<TareaBBDD> findByIdHabitacion(long idHabitacion);
}
