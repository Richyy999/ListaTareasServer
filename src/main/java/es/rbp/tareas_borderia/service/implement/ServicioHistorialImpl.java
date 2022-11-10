package es.rbp.tareas_borderia.service.implement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.rbp.tareas_borderia.entidad.bbdd.HabitacionConfigBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.HistorialBBDD;
import es.rbp.tareas_borderia.repositorio.RepositorioHabitacionConfig;
import es.rbp.tareas_borderia.repositorio.RepositorioHistorial;
import es.rbp.tareas_borderia.service.ServicioHistorial;

@Service
public class ServicioHistorialImpl implements ServicioHistorial {

	@Autowired
	private RepositorioHistorial repoHistorial;

	@Autowired
	private RepositorioHabitacionConfig repoHabitacionConfig;

	@Override
	public List<HistorialBBDD> findAll() {
		return repoHistorial.findAll();
	}

	@Override
	public HistorialBBDD findById(long id) {
		Optional<HistorialBBDD> optional = repoHistorial.findById(id);
		if (optional.isEmpty())
			return null;

		return optional.get();
	}

	@Override
	public List<HistorialBBDD> findByIdHabitacionConfig(long idHabitacionConfig) {
		return repoHistorial.findByIdHabitacionConfig(idHabitacionConfig);
	}

	@Override
	public void actualizarHistorial(long idHabitacion, long idUsuario) {
		Optional<HistorialBBDD> optional = repoHistorial.findById(idHabitacion);
		if (optional.isEmpty())
			return;

		HistorialBBDD historialBBDD = optional.get();
		historialBBDD.setIdUsuario(idUsuario);

		LocalDateTime hoy = LocalDateTime.now();
		historialBBDD.setUltimaLimpieza(hoy);

		repoHistorial.save(historialBBDD);
	}

	@Override
	public boolean crearHistorial(long idHabitacionConfig, String nombreHabitacion) {
		HistorialBBDD historialBBDDNuevo = new HistorialBBDD();

		Optional<HabitacionConfigBBDD> optional = repoHabitacionConfig.findById(idHabitacionConfig);
		if (optional.isEmpty())
			return false;

		HistorialBBDD historialBBDD = repoHistorial.findByNombreHabitacion(nombreHabitacion);
		if (historialBBDD != null)
			return false;

		historialBBDDNuevo.setIdHabitacionConfig(idHabitacionConfig);
		historialBBDDNuevo.setNombreHabitacion(nombreHabitacion);
		repoHistorial.save(historialBBDDNuevo);
		return true;
	}

	@Override
	public boolean modificarHistorial(long idHistorial, String nombreHabitacion) {
		Optional<HistorialBBDD> optional = repoHistorial.findById(idHistorial);
		if (optional.isEmpty())
			return false;

		if (repoHistorial.findByNombreHabitacion(nombreHabitacion) != null)
			return false;

		HistorialBBDD historialBBDD = optional.get();
		historialBBDD.setNombreHabitacion(nombreHabitacion);
		repoHistorial.save(historialBBDD);

		return true;
	}

	@Override
	public boolean eliminarHistorial(long idHistorial) {
		Optional<HistorialBBDD> optional = repoHistorial.findById(idHistorial);
		if (optional.isEmpty())
			return false;

		repoHistorial.delete(optional.get());
		return true;
	}
}
