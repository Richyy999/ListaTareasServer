package es.rbp.tareas_borderia.service;

import es.rbp.tareas_borderia.entidad.Habitacion;
import es.rbp.tareas_borderia.entidad.bbdd.DeudaBBDD;

public interface ServicioDeuda {

	/**
	 * Obtiene la deuda de un usuario
	 * 
	 * @param idUsuario id del usuario
	 * @return deuda del usuario indicado
	 */
	DeudaBBDD findByIdUsuario(long idUsuario);

	/**
	 * Reduce la deuda cobrando las tareas de la habitación
	 * 
	 * @param idUsuario  id del usuario que ha limpiado la habitación
	 * @param habitacion habitación que ha limpiado para reducir la deuda
	 */
	void reducirDeuda(long idUsuario, Habitacion habitacion);

	/**
	 * Añade una deuda al usuario
	 * 
	 * @param idUsuario usuario al que se le añade la deuda
	 * @param deuda     valor de la deuda
	 * @return deuda creada, null si hay algún dato erróneo
	 */
	DeudaBBDD anadirDeuda(long idUsuario, double deuda);

	/**
	 * Crea una deuda de 0€ para el usuario indicado
	 * 
	 * @param idUsuario id del usuario al que se le creará una deuda
	 */
	void crearDeuda(long idUsuario);
}
