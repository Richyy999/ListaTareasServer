package es.rbp.tareas_borderia.service.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.rbp.tareas_borderia.entidad.bbdd.DeudaBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.HabitacionBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.MesBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.MovimientoBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.TareaBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.UsuarioBBDD;
import es.rbp.tareas_borderia.repositorio.RepositorioDeuda;
import es.rbp.tareas_borderia.repositorio.RepositorioHabitacion;
import es.rbp.tareas_borderia.repositorio.RepositorioMes;
import es.rbp.tareas_borderia.repositorio.RepositorioMovimiento;
import es.rbp.tareas_borderia.repositorio.RepositorioTarea;
import es.rbp.tareas_borderia.service.ServicioMovimiento;

@Service
public class ServicioMovimientoImpl implements ServicioMovimiento {

	@Autowired
	private RepositorioMovimiento repoMovi;

	@Autowired
	private RepositorioMes repoMes;

	@Autowired
	private RepositorioHabitacion repoHabitacion;

	@Autowired
	private RepositorioTarea repoTarea;

	@Autowired
	private RepositorioDeuda repoDeuda;

	@Override
	public void crearMovimiento(UsuarioBBDD usuario, int tipoMovi, double cantidad) {
		MovimientoBBDD movimiento = new MovimientoBBDD();
		movimiento.setIdUsuario(usuario.getId());
		movimiento.setTipoMovi(tipoMovi);
		movimiento.setCantidad(cantidad);
		movimiento.setSaldo(getSaldo(usuario));

		repoMovi.save(movimiento);
	}

	@Override
	public List<MovimientoBBDD> findByIdUsuario(long idUsuario) {
		return repoMovi.findByIdUsuario(idUsuario);
	}

	private double getSaldo(UsuarioBBDD usuario) {
		double saldo = 0.0;

		List<MesBBDD> meses = repoMes.findByIdUsuario(usuario.getId());
		for (MesBBDD mes : meses) {
			List<HabitacionBBDD> habitaciones = repoHabitacion.findByIdMes(mes.getId());
			for (HabitacionBBDD habitacion : habitaciones) {
				List<TareaBBDD> tareas = repoTarea.findByIdHabitacion(habitacion.getId());
				for (TareaBBDD tarea : tareas) {
					if (!tarea.isCobrada())
						saldo += tarea.getPrecioSinPagar();
				}
			}
		}

		DeudaBBDD deuda = repoDeuda.findByIdUsuario(usuario.getId());
		saldo -= deuda.getDeuda();

		return Math.round(saldo * 100.0) / 100.0;
	}
}
