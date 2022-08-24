package es.rbp.tareas_borderia.service.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
			if (deuda >= tarea.getPrecioSinPagar()) {
				deuda -= tarea.getPrecioSinPagar();
				tarea.setCobrada(true);
			} else if (deuda < tarea.getPrecioSinPagar()) {
				double precioNuevo = tarea.getPrecioSinPagar() - deuda;
				tarea.setPrecioSinPagar(precioNuevo);
			}
			if (deuda == 0)
				return;
		}
	}

	@Override
	public DeudaBBDD anadirDeuda(long idUsuario, double deuda) {
		DeudaBBDD deudaBBDD = repoDeuda.findByIdUsuario(idUsuario);
		deudaBBDD.setDeuda(deuda);
		return repoDeuda.save(deudaBBDD);
	}

	@Override
	public void crearDeuda(long idUsuario) {
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
}
