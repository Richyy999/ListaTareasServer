package es.rbp.tareas_borderia.service.implement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.rbp.tareas_borderia.entidad.bbdd.HabitacionConfigBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.TareaConfigBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.UsuarioBBDD;
import es.rbp.tareas_borderia.entidad.config.HabitacionConfig;
import es.rbp.tareas_borderia.repositorio.RepositorioHabitacionConfig;
import es.rbp.tareas_borderia.repositorio.RepositorioTareaConfig;

import es.rbp.tareas_borderia.service.ServicioHabitacionConfig;

@Service
public class ServicioHabitacionConfigImpl implements ServicioHabitacionConfig {

	@Autowired
	private RepositorioHabitacionConfig repoHabitacionConfig;

	@Autowired
	private RepositorioTareaConfig repoTareasLimpiezaConfig;

	@Override
	public HabitacionConfigBBDD anadirHabitacion(String nombre, UsuarioBBDD usuario) {
		HabitacionConfigBBDD habitacionConfigBBDD = repoHabitacionConfig.findByNombre(nombre);
		if (habitacionConfigBBDD != null)
			return null;

		HabitacionConfigBBDD habitacionNueva = new HabitacionConfigBBDD();
		habitacionNueva.setNombre(nombre);
		habitacionNueva.setUserAlta(usuario.getNombre());
		habitacionNueva.setUserMod(usuario.getNombre());

		return repoHabitacionConfig.save(habitacionNueva);
	}

	@Override
	public List<HabitacionConfigBBDD> findAll() {
		List<HabitacionConfigBBDD> habitacionesConfigBBDD = repoHabitacionConfig.findAll();
		for (HabitacionConfigBBDD habitacionConfigBBDD : habitacionesConfigBBDD) {
			if (habitacionConfigBBDD.getId() == HabitacionConfigBBDD.ID_HABITACION_MUESTRA)
				habitacionesConfigBBDD.remove(habitacionConfigBBDD);
		}
		return habitacionesConfigBBDD;
	}

	@Override
	public HabitacionConfigBBDD findById(long id) {
		Optional<HabitacionConfigBBDD> optional = repoHabitacionConfig.findById(id);
		if (optional.isEmpty())
			return null;

		return optional.get();
	}

	@Override
	public boolean modificarHabitacion(HabitacionConfig habitacionConfig, UsuarioBBDD usuario) {
		Optional<HabitacionConfigBBDD> optional = repoHabitacionConfig.findById(habitacionConfig.getId());
		if (optional.isEmpty())
			return false;

		HabitacionConfigBBDD habitacionConfigBBDDActualizada = optional.get();
		if (habitacionConfig.getId() == 0)
			return false;

		String nombre = habitacionConfig.getNombre();
		if (nombre != null && nombre.strip().length() > 0) {
			habitacionConfigBBDDActualizada.setNombre(nombre);
			habitacionConfigBBDDActualizada.setUserMod(usuario.getNombre());
			habitacionConfigBBDDActualizada.setFechaMod(LocalDateTime.now());
			repoHabitacionConfig.save(habitacionConfigBBDDActualizada);
			return true;
		}

		return false;
	}

	@Override
	public boolean eliminarHabitacion(long idHabitacionConfig) {
		Optional<HabitacionConfigBBDD> optional = repoHabitacionConfig.findById(idHabitacionConfig);
		if (optional.isEmpty())
			return false;

		List<TareaConfigBBDD> tareas = repoTareasLimpiezaConfig.findByIdHabitacionConfig(idHabitacionConfig);
		if (tareas.size() > 0)
			return false;

		HabitacionConfigBBDD habitacionConfigBBDD = optional.get();
		repoHabitacionConfig.delete(habitacionConfigBBDD);
		return true;
	}
}
