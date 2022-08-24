package es.rbp.tareas_borderia.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.rbp.tareas_borderia.entidad.bbdd.HabitacionBBDD;

public interface RepositorioHabitacion extends JpaRepository<HabitacionBBDD, Long> {

	List<HabitacionBBDD> findByIdMes(long idMes);
}
