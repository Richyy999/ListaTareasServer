package es.rbp.tareas_borderia.service;

import java.util.List;

import es.rbp.tareas_borderia.entidad.Tarea;
import es.rbp.tareas_borderia.entidad.bbdd.TareaBBDD;

/**
 * Interfaz para controla las tareas. Se puede:
 * <ul>
 * <li>Añadir una tarea</li>
 * <li>Obtener la lista de las tareas de un habitación</li>
 * <li>Cobrar tareas</li>
 * <li>Eliminar una tarea</li>
 * </ul>
 * 
 * @author Ricardo Bordería Pi
 *
 */
public interface ServicioTarea {

	/**
	 * Añade una tarea de limpieza a la habitación indicada
	 * 
	 * @param idHabitacion  id de la habitación en la que se creará la tarea de
	 *                      limpieza
	 * @param tareaLimpieza {@link Tarea} con los datos de la tarea
	 * @return true Tarea de limpieza creada, null en caso de que no se haya creado
	 */
	TareaBBDD anadirTarea(long idHabitacion, Tarea tareaLimpieza);

	/**
	 * Obtiene todas las tareas de limpieza de una habitación
	 * 
	 * @param idHabitación id de la habitación que contiene las tareas de limpieza
	 * @return List con las tareas de limpieza contenidas en la habitación
	 */
	List<TareaBBDD> getTareas(long idHabitación);

	/**
	 * Cobra la tarea
	 * 
	 * @param idTarea id de la tarea a cobrar
	 */
	void cobrarTarea(long idTarea);

	/**
	 * Cobra una cantidad concreta modificando el precio de las tareas si es
	 * necesario
	 * 
	 * @param cantidad  cantidad de dinero a cobrar
	 * @param idUsuario id del usuario que cobra la tarea
	 * @return true si se ha cobrado correctamente las tareas
	 */
	boolean cobrarCantidad(double cantidad, long idUsuario);

	/**
	 * Devuelve el total sin pagar de un usuario
	 * 
	 * @param idUsuario id del usuario del que se desea saber la cantidad sin cobrar
	 * @return cantidad sin cobrar
	 */
	double getCantidadSinCobrar(long idUsuario);
}
