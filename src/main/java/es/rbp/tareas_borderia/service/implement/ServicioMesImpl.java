package es.rbp.tareas_borderia.service.implement;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.rbp.tareas_borderia.entidad.bbdd.HabitacionBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.MesBBDD;
import es.rbp.tareas_borderia.repositorio.RepositorioHabitacion;
import es.rbp.tareas_borderia.repositorio.RepositorioMes;
import es.rbp.tareas_borderia.service.ServicioMes;

@Service
public class ServicioMesImpl implements ServicioMes {

	@Autowired
	private RepositorioMes repoMes;

	@Autowired
	private RepositorioHabitacion repoHabitacion;

	@Override
	public MesBBDD getMes(long idUsuario) {
		List<MesBBDD> meses = repoMes.findByIdUsuario(idUsuario);

		if (meses.size() == 0) {
			MesBBDD mesNuevo = new MesBBDD();
			mesNuevo.setIdUsuario(idUsuario);
			return repoMes.save(mesNuevo);
		}

		Collections.sort(meses);

		MesBBDD mes = meses.get(meses.size() - 1);

		LocalDateTime fecha = mes.getFecha();
		Date mesDate = Date.from(fecha.atZone(ZoneId.systemDefault()).toInstant());
		Calendar mesCalendar = Calendar.getInstance();
		mesCalendar.setTime(mesDate);

		Calendar hoy = Calendar.getInstance();

		int mesHoy = hoy.get(Calendar.MONTH);
		int anioHoy = hoy.get(Calendar.YEAR);

		int mesMes = mesCalendar.get(Calendar.MONTH);
		int anioMes = mesCalendar.get(Calendar.YEAR);

		if (mesHoy == mesMes && anioHoy == anioMes)
			return mes;

		MesBBDD mesNuevo = new MesBBDD();
		mesNuevo.setIdUsuario(idUsuario);
		return repoMes.save(mesNuevo);
	}

	@Override
	public boolean eliminarMes(long idMes) {
		Optional<MesBBDD> optional = repoMes.findById(idMes);
		if (optional.isEmpty())
			return false;

		List<HabitacionBBDD> habitaciones = repoHabitacion.findByIdMes(idMes);
		if (habitaciones.size() > 0)
			return false;

		MesBBDD mes = optional.get();
		repoMes.delete(mes);
		return true;
	}

	@Override
	public List<MesBBDD> getMeses(long idUsuario) {
		return repoMes.findByIdUsuario(idUsuario);
	}
}
