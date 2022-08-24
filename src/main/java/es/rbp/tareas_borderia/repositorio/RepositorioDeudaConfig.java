package es.rbp.tareas_borderia.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import es.rbp.tareas_borderia.entidad.bbdd.DeudaConfigBBDD;

public interface RepositorioDeudaConfig extends JpaRepository<DeudaConfigBBDD, Long> {

}
