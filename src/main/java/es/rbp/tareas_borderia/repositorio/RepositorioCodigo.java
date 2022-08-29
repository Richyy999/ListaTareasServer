package es.rbp.tareas_borderia.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import es.rbp.tareas_borderia.entidad.bbdd.Codigo;

public interface RepositorioCodigo extends JpaRepository<Codigo, Long> {

	Codigo findByCodigoAndTipoCodigo(int codigo, String tipoCodigo);
}
