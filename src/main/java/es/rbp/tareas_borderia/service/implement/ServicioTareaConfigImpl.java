package es.rbp.tareas_borderia.service.implement;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.rbp.tareas_borderia.entidad.bbdd.HabitacionConfigBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.TareaConfigBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.UsuarioBBDD;
import es.rbp.tareas_borderia.entidad.config.HabitacionConfig;
import es.rbp.tareas_borderia.entidad.config.TareaConfig;
import es.rbp.tareas_borderia.repositorio.RepositorioTareaConfig;
import es.rbp.tareas_borderia.service.ServicioTareaConfig;

@Service
public class ServicioTareaConfigImpl implements ServicioTareaConfig {

	@Autowired
	private RepositorioTareaConfig repo;

	@Override
	public TareaConfigBBDD findById(long id) {
		Optional<TareaConfigBBDD> optional = repo.findById(id);
		if (optional.isEmpty())
			return null;

		return optional.get();
	}

	@Override
	public List<TareaConfigBBDD> findByIdHabitacionConfig(long idHabitacionConfig) {
		return repo.findByIdHabitacionConfig(idHabitacionConfig);
	}

	@Override
	public boolean anadirTareaLimpiezaConfig(TareaConfig tareaConfig, HabitacionConfig habitacionConfig, UsuarioBBDD usuario) {
		TareaConfigBBDD tareaConfigBBDD = new TareaConfigBBDD();
		tareaConfigBBDD.setIdHabitacionConfig(habitacionConfig.getId());
		tareaConfigBBDD.setPrecio(tareaConfig.getPrecio());
		tareaConfigBBDD.setNombre(tareaConfig.getNombre());
		tareaConfigBBDD.setUserAlta(usuario.getNombre());
		tareaConfigBBDD.setUserMod(usuario.getNombre());

		TareaConfigBBDD tareaLimpiezaConfigNueva = repo.save(tareaConfigBBDD);
		return tareaLimpiezaConfigNueva != null;
	}

	@Override
	public boolean crearTareaConfig(TareaConfig tareaConfig, UsuarioBBDD usuario) {
		List<TareaConfigBBDD> tareasConfigBBDD = repo
				.findByIdHabitacionConfigAndNombre(HabitacionConfigBBDD.ID_HABITACION_MUESTRA, tareaConfig.getNombre());
		if (tareasConfigBBDD.size() >= 1)
			return false;

		TareaConfigBBDD tareaConfigBBDD = new TareaConfigBBDD();
		tareaConfigBBDD.setIdHabitacionConfig(HabitacionConfigBBDD.ID_HABITACION_MUESTRA);
		tareaConfigBBDD.setPrecio(tareaConfig.getPrecio());
		tareaConfigBBDD.setNombre(tareaConfig.getNombre());
		tareaConfigBBDD.setUserAlta(usuario.getNombre());
		tareaConfigBBDD.setUserMod(usuario.getNombre());

		TareaConfigBBDD tareaLimpiezaConfigNueva = repo.save(tareaConfigBBDD);
		return tareaLimpiezaConfigNueva != null;
	}

	@Override
	public boolean modificarTareaLimpiezaConfig(TareaConfig tareaActualizada, UsuarioBBDD usuario) {
		Optional<TareaConfigBBDD> optional = repo.findById(tareaActualizada.getId());
		if (optional.isEmpty())
			return false;

		TareaConfigBBDD tareaConfigBBDD = optional.get();
		double precio = tareaActualizada.getPrecio();
		String nombre = tareaActualizada.getNombre();

		boolean actualizada = false;

		if (precio > 0) {
			tareaConfigBBDD.setPrecio(precio);
			actualizada = true;
		}
		if (nombre != null && nombre.strip().length() > 0) {
			tareaConfigBBDD.setNombre(nombre);
			actualizada = true;
		}

		if (actualizada) {
			tareaConfigBBDD.setUserMod(usuario.getNombre());
			repo.save(tareaConfigBBDD);
			return true;
		}

		return false;
	}

	@Override
	public boolean eliminarTareaConfig(long idTareaLimpiezaConfig) {
		Optional<TareaConfigBBDD> optional = repo.findById(idTareaLimpiezaConfig);
		if (optional.isEmpty())
			return false;

		TareaConfigBBDD tareaLimpiezaConfigBBDD = optional.get();
		repo.delete(tareaLimpiezaConfigBBDD);
		return true;
	}
}
