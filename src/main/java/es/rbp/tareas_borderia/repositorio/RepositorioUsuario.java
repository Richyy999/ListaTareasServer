package es.rbp.tareas_borderia.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import es.rbp.tareas_borderia.entidad.bbdd.UsuarioBBDD;

public interface RepositorioUsuario extends JpaRepository<UsuarioBBDD, Long>{

	UsuarioBBDD findByNombreAndContrasena(String nombre, String contrasena);
	
	UsuarioBBDD findByIdAndToken(long id, String token);
	
	UsuarioBBDD findByToken(String token);
	
	UsuarioBBDD findByNombre(String nombre);
}
