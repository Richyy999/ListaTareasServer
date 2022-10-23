package es.rbp.tareas_borderia.service.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.rbp.tareas_borderia.entidad.Termino;
import es.rbp.tareas_borderia.entidad.bbdd.TerminoBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.UsuarioBBDD;
import es.rbp.tareas_borderia.repositorio.RepositorioTermino;
import es.rbp.tareas_borderia.repositorio.RepositorioUsuario;
import es.rbp.tareas_borderia.service.ServicioTermino;

@Service
public class ServicioTerminoImpl implements ServicioTermino {

	@Autowired
	private RepositorioTermino repoTermino;

	@Autowired
	private RepositorioUsuario repoUsuario;

	@Override
	public List<TerminoBBDD> findAlll() {
		return repoTermino.findAll();
	}

	@Override
	public boolean anadirTermino(Termino termino) {
		TerminoBBDD terminoBBDD = repoTermino.findByTitulo(termino.getTitulo());
		if (terminoBBDD != null)
			return false;

		TerminoBBDD terminoNuevo = new TerminoBBDD();
		terminoNuevo.setTitulo(termino.getTitulo());
		terminoNuevo.setDescripcion(termino.getDescripcion());
		terminoNuevo.setOrden(termino.getOrden());

		if (repoTermino.save(terminoNuevo) != null) {
			actualizarUsuarios();
			return true;
		}

		return false;
	}

	@Override
	public boolean modificarTermino(String titulo, Termino termino) {
		TerminoBBDD terminoBBDD = repoTermino.findByTitulo(titulo);
		if (terminoBBDD == null)
			return false;

		boolean modificado = false;

		if (!termino.getDescripcion().equals(terminoBBDD.getDescripcion())) {
			modificado = true;
			terminoBBDD.setDescripcion(termino.getDescripcion());
		}

		if (termino.getOrden() != terminoBBDD.getOrden()) {
			modificado = true;
			terminoBBDD.setOrden(termino.getOrden());
		}

		if (modificado) {
			repoTermino.save(terminoBBDD);
			actualizarUsuarios();
		}

		return modificado;
	}

	@Override
	public boolean eliminarTermino(String titulo) {
		TerminoBBDD terminoBBDD = repoTermino.findByTitulo(titulo);
		if (terminoBBDD == null)
			return false;

		repoTermino.delete(terminoBBDD);
		actualizarUsuarios();
		return true;
	}

	/**
	 * Actualiza el estado de lo susuarios para que vuelvan a aceptar los términos
	 */
	private void actualizarUsuarios() {
		List<UsuarioBBDD> usuarios = repoUsuario.findAll();
		for (UsuarioBBDD usuarioBBDD : usuarios) {
			usuarioBBDD.setTerminosAceptados(false);
			repoUsuario.save(usuarioBBDD);
		}
	}
}
