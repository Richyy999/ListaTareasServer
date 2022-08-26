package es.rbp.tareas_borderia.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.rbp.tareas_borderia.entidad.bbdd.HistorialBBDD;

public interface RepositorioHistorial extends JpaRepository<HistorialBBDD, Long> {

	HistorialBBDD findByNombreHabitacion(String nombreHabitacion);

	List<HistorialBBDD> findByIdHabitacionConfig(long idHabitacionConfig);
}