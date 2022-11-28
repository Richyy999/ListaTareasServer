package es.rbp.tareas_borderia.service.implement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.rbp.tareas_borderia.entidad.Tarea;
import es.rbp.tareas_borderia.entidad.bbdd.HabitacionBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.MesBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.TareaBBDD;
import es.rbp.tareas_borderia.repositorio.RepositorioHabitacion;
import es.rbp.tareas_borderia.repositorio.RepositorioMes;
import es.rbp.tareas_borderia.repositorio.RepositorioTarea;
import es.rbp.tareas_borderia.service.ServicioTarea;

@Service
public class ServicioTareaImpl implements ServicioTarea {

	@Autowired
	private RepositorioTarea repoTareas;

	@Autowired
	private RepositorioMes repoMes;

	@Autowired
	private RepositorioHabitacion repoHabitacion;

	@Override
	public TareaBBDD anadirTarea(long idHabitacion, Tarea tareaLimpieza) {
		TareaBBDD tareaLimpiezaBBDD = new TareaBBDD();
		tareaLimpiezaBBDD.setNombre(tareaLimpieza.getNombre());
		tareaLimpiezaBBDD.setPrecioSinPagar(tareaLimpieza.getPrecioSinPagar());
		tareaLimpiezaBBDD.setPrecioPagado(tareaLimpieza.getPrecioPagado());
		tareaLimpiezaBBDD.setCobrada(tareaLimpieza.isCobrada());
		tareaLimpiezaBBDD.setIdHabitacion(idHabitacion);
		return repoTareas.save(tareaLimpiezaBBDD);
	}

	@Override
	public List<TareaBBDD> getTareas(long idHabitación) {
		return repoTareas.findByIdHabitacion(idHabitación);
	}

	@Override
	public double cobrarTarea(long idTarea) {
		Optional<TareaBBDD> optional = repoTareas.findById(idTarea);
		if (optional.isEmpty())
			return 0.0;

		TareaBBDD tarea = optional.get();
		if (tarea.isCobrada())
			return 0.0;

		tarea.setCobrada(true);
		double precio = tarea.getPrecioSinPagar();
		tarea.setPrecioPagado(tarea.getPrecioPagado() + tarea.getPrecioSinPagar());
		tarea.setPrecioSinPagar(0);
		repoTareas.save(tarea);

		return precio;
	}

	@Override
	public boolean cobrarCantidad(double cantidad, long idUsuario) {
		List<TareaBBDD> tareasSinCobrar = getTareasSinCobrar(idUsuario);
		double cantidadSinCobrar = cantidad;
		double cantidadSinPagar = getPrecioSinPagar(tareasSinCobrar);

		if (cantidadSinCobrar > cantidadSinPagar)
			return false;

		for (TareaBBDD tarea : tareasSinCobrar) {
			double precioSinPagar = tarea.getPrecioSinPagar();
			if (cantidadSinCobrar == 0)
				return true;

			if (cantidadSinCobrar >= precioSinPagar) {
				tarea.setCobrada(true);
				tarea.setPrecioPagado(tarea.getPrecioPagado() + tarea.getPrecioSinPagar());
				tarea.setPrecioSinPagar(0);
				cantidadSinCobrar -= precioSinPagar;
			} else if (cantidadSinCobrar < precioSinPagar) {
				precioSinPagar -= cantidadSinCobrar;
				tarea.setPrecioPagado(tarea.getPrecioPagado() + cantidadSinCobrar);
				tarea.setPrecioSinPagar(precioSinPagar);
				cantidadSinCobrar = 0;
			}

			repoTareas.save(tarea);
		}

		return true;
	}

	@Override
	public double getCantidadSinCobrar(long idUsuario) {
		List<TareaBBDD> tareas = getTareasSinCobrar(idUsuario);
		return getPrecioSinPagar(tareas);
	}

	/**
	 * Obtiene la lista de todas las tareas sin cobrar del usuario
	 * 
	 * @param idUsuario id del usuario que ha realizado las tareas
	 * @return lista de {@link TareaBBDD} con las tareas sin cobrar
	 */
	private List<TareaBBDD> getTareasSinCobrar(long idUsuario) {
		List<TareaBBDD> listaTareas = new ArrayList<>();
		List<MesBBDD> meses = repoMes.findByIdUsuario(idUsuario);

		for (MesBBDD mes : meses) {
			List<HabitacionBBDD> habitaciones = repoHabitacion.findByIdMes(mes.getId());
			for (HabitacionBBDD habitacion : habitaciones) {
				List<TareaBBDD> tareas = repoTareas.findByIdHabitacion(habitacion.getId());
				for (TareaBBDD tarea : tareas) {
					if (!tarea.isCobrada())
						listaTareas.add(tarea);
				}
			}
		}
		return listaTareas;
	}

	/**
	 * Calcula el precio sin pagar de las tareas indicadas
	 * 
	 * @param tareasSinPagar lista de tareas sin pagar
	 * @return cantidad sin pagar
	 */
	private double getPrecioSinPagar(List<TareaBBDD> tareasSinPagar) {
		double total = 0;
		for (TareaBBDD tarea : tareasSinPagar) {
			total += tarea.getPrecioSinPagar();
		}

		return Math.round(total * 100.0) / 100.0;
	}
}
