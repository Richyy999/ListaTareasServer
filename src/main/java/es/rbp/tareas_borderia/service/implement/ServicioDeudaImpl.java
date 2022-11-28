package es.rbp.tareas_borderia.service.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.rbp.tareas_borderia.entidad.Deuda;
import es.rbp.tareas_borderia.entidad.Habitacion;
import es.rbp.tareas_borderia.entidad.Tarea;
import es.rbp.tareas_borderia.entidad.bbdd.DeudaBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.DeudaConfigBBDD;
import es.rbp.tareas_borderia.repositorio.RepositorioDeuda;
import es.rbp.tareas_borderia.repositorio.RepositorioDeudaConfig;
import es.rbp.tareas_borderia.service.ServicioDeuda;

@Service
public class ServicioDeudaImpl implements ServicioDeuda {

	@Autowired
	private RepositorioDeuda repoDeuda;

	@Autowired
	private RepositorioDeudaConfig repoDeudaConfig;

	@Override
	public DeudaBBDD findByIdUsuario(long idUsuario) {
		return repoDeuda.findByIdUsuario(idUsuario);
	}

	@Override
	public void reducirDeuda(long idUsuario, Habitacion habitacion) {
		DeudaBBDD deudaBBDD = repoDeuda.findByIdUsuario(idUsuario);
		double deuda = deudaBBDD.getDeuda();
		if (deuda == 0)
			return;

		List<Tarea> tareas = habitacion.getTareas();
		for (Tarea tarea : tareas) {
			if (deuda == 0)
				return;

			if (deuda >= tarea.getPrecioSinPagar()) {
				deuda -= tarea.getPrecioSinPagar();
				tarea.setCobrada(true);
				tarea.setPrecioPagado(tarea.getPrecioPagado() + tarea.getPrecioSinPagar());
				tarea.setPrecioSinPagar(0);
			} else if (deuda < tarea.getPrecioSinPagar()) {
				double precioNuevo = tarea.getPrecioSinPagar() - deuda;
				precioNuevo = Math.round(precioNuevo * 100.0) / 100.0;
				tarea.setPrecioSinPagar(precioNuevo);
				tarea.setPrecioPagado(tarea.getPrecioPagado() + deuda);
				deuda = 0;
			}

			deudaBBDD.setDeuda(deuda);
			repoDeuda.save(deudaBBDD);
		}
	}

	@Override
	public DeudaBBDD anadirDeuda(long idUsuario, double deuda) {
		if (deuda < 0)
			return null;

		DeudaBBDD deudaBBDD = repoDeuda.findByIdUsuario(idUsuario);
		if ((deudaBBDD.getDeuda() > 0 && deudaBBDD.isAcumular()) || deudaBBDD.getDeuda() == 0) {
			double cantidadDeuda = deudaBBDD.getDeuda() + deuda;
			if (cantidadDeuda > deudaBBDD.getDeudaMax())
				return null;

			deudaBBDD.setDeuda(cantidadDeuda);
			return repoDeuda.save(deudaBBDD);
		}

		return null;
	}

	@Override
	public void crearDeuda(long idUsuario) {
		DeudaBBDD deuda = repoDeuda.findByIdUsuario(idUsuario);
		if (deuda != null)
			return;

		List<DeudaConfigBBDD> deudasConfigBBDD = repoDeudaConfig.findAll();
		DeudaBBDD deudaBBDD = new DeudaBBDD();
		if (deudasConfigBBDD.size() == 0) {
			deudaBBDD.setDeuda(0d);
			deudaBBDD.setDeudaMax(50d);
			deudaBBDD.setAcumular(false);
		} else {
			DeudaConfigBBDD deudaConfigBBDD = deudasConfigBBDD.get(deudasConfigBBDD.size() - 1);
			deudaBBDD.setDeuda(0d);
			deudaBBDD.setDeudaMax(deudaConfigBBDD.getDeudaMax());
			deudaBBDD.setAcumular(deudaConfigBBDD.isAcumular());
		}
		deudaBBDD.setIdUsuario(idUsuario);
		repoDeuda.save(deudaBBDD);
	}

	@Override
	public boolean modificarDeuda(long idUsuario, Deuda deuda) {
		boolean actualizada = false;
		DeudaBBDD deudaBBDD = repoDeuda.findByIdUsuario(idUsuario);
		if (deudaBBDD == null)
			return false;

		if (deuda.getDeudaMax() > 0) {
			deudaBBDD.setDeudaMax(deuda.getDeudaMax());
			actualizada = true;
		}

		if (deuda.isAcumular() != deudaBBDD.isAcumular()) {
			deudaBBDD.setAcumular(deuda.isAcumular());
			actualizada = true;
		}

		if (actualizada)
			return repoDeuda.save(deudaBBDD) != null;

		return false;
	}
}
