package es.rbp.tareas_borderia.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import es.rbp.tareas_borderia.entidad.bbdd.HabitacionConfigBBDD;

public interface RepositorioHabitacionConfig extends JpaRepository<HabitacionConfigBBDD, Long> {

	HabitacionConfigBBDD findByNombre(String nombre);
}
