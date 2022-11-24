package es.rbp.tareas_borderia.service;

import java.util.List;

import es.rbp.tareas_borderia.entidad.Movimiento;
import es.rbp.tareas_borderia.entidad.bbdd.MovimientoBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.UsuarioBBDD;

public interface ServicioMovimiento {

	/**
	 * Crea un movimiento nuevo
	 * 
	 * @param usuario  usuario al que pertenece el movimiento
	 * @param tipoMovi tipo de movimiento. Debe ser:
	 *                 <ul>
	 *                 <li>{@link Movimiento#TIPO_COBRO}</li>
	 *                 <li>{@link Movimiento#TIPO_DEUDA}</li>
	 *                 </ul>
	 * @param cantidad cantidad movida
	 */
	void crearMovimiento(UsuarioBBDD usuario, int tipoMovi, double cantidad);

	/**
	 * Obtiene todos los movimientos de un usuario
	 * 
	 * @param idUsuario id del usuario al que pertenecen los movimientos
	 * @return lista con todos los movimientos de un usuario
	 */
	List<MovimientoBBDD> findByIdUsuario(long idUsuario);
}
