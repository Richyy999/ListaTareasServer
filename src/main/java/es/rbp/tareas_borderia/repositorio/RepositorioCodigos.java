package es.rbp.tareas_borderia.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import es.rbp.tareas_borderia.entidad.bbdd.Codigos;

public interface RepositorioCodigos extends JpaRepository<Codigos, Long> {

	Codigos findByCodigoAndTipoCodigo(int codigo, String tipoCodigo);
}
