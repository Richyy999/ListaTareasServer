package es.rbp.tareas_borderia.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.rbp.tareas_borderia.entidad.bbdd.MesBBDD;

public interface RepositorioMes extends JpaRepository<MesBBDD, Long> {

	List<MesBBDD> findByIdUsuario(long idUsuario);
}
