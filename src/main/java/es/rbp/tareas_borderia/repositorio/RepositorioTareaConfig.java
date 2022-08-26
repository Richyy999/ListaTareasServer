package es.rbp.tareas_borderia.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.rbp.tareas_borderia.entidad.bbdd.TareaConfigBBDD;

public interface RepositorioTareaConfig extends JpaRepository<TareaConfigBBDD, Long> {

	List<TareaConfigBBDD> findByIdHabitacionConfig(long idHabitacionConfig);

	List<TareaConfigBBDD> findByIdHabitacionConfigAndNombre(long id, String nombre);
}
