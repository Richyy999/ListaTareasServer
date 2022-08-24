package es.rbp.tareas_borderia.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import es.rbp.tareas_borderia.entidad.bbdd.DeudaBBDD;

public interface RepositorioDeuda extends JpaRepository<DeudaBBDD, Long> {

	DeudaBBDD findByIdUsuario(long idUsuario);
}
