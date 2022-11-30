package es.rbp.tareas_borderia.service.implement;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.rbp.tareas_borderia.entidad.bbdd.HabitacionBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.MesBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.TareaBBDD;
import es.rbp.tareas_borderia.repositorio.RepositorioHabitacion;
import es.rbp.tareas_borderia.repositorio.RepositorioMes;
import es.rbp.tareas_borderia.repositorio.RepositorioTarea;
import es.rbp.tareas_borderia.service.ServicioHabitacion;

@Service
public class ServicioHabitacionImpl implements ServicioHabitacion {

	@Autowired
	private RepositorioHabitacion repoHabitacion;

	@Autowired
	private RepositorioTarea repoTarea;

	@Autowired
	private RepositorioMes repoMes;

	@Override
	public HabitacionBBDD anadirHabitacion(String nombre, long idTarea) {
		HabitacionBBDD habitacion = new HabitacionBBDD();
		habitacion.setNombre(nombre);
		habitacion.setIdMes(idTarea);

		return repoHabitacion.save(habitacion);
	}

	@Override
	public List<HabitacionBBDD> getHabitaciones(long idMes) {
		return repoHabitacion.findByIdMes(idMes);
	}

	@Override
	public HabitacionBBDD getHabitacion(long idHabitacion) {
		Optional<HabitacionBBDD> optional = repoHabitacion.findById(idHabitacion);
		if (optional.isEmpty())
			return null;

		return optional.get();
	}

	@Override
	public boolean eliminarHabitacion(long idHabitacion) {
		Optional<HabitacionBBDD> optional = repoHabitacion.findById(idHabitacion);
		if (optional.isEmpty())
			return false;

		List<TareaBBDD> tareasLimpieza = repoTarea.findByIdHabitacion(idHabitacion);
		for (TareaBBDD tarea : tareasLimpieza) {
			if (tarea.getPrecioPagado() > 0)
				return false;
		}

		for (TareaBBDD tarea : tareasLimpieza) {
			repoTarea.delete(tarea);
		}

		HabitacionBBDD habitacion = optional.get();
		repoHabitacion.delete(habitacion);

		Optional<MesBBDD> optionalMes = repoMes.findById(habitacion.getIdMes());
		if (!optional.isEmpty()) {
			MesBBDD mes = optionalMes.get();
			List<HabitacionBBDD> habitaciones = repoHabitacion.findByIdMes(mes.getId());
			if (habitaciones.size() == 0)
				repoMes.delete(mes);
		}

		return true;
	}
}
