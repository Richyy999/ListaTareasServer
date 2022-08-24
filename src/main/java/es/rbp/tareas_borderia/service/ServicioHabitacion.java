package es.rbp.tareas_borderia.service;

import java.util.List;

import es.rbp.tareas_borderia.entidad.bbdd.HabitacionBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.TareaBBDD;

public interface ServicioHabitacion {

	/**
	 * Añade una habitación a la tarea
	 * 
	 * @param idMes id del mes en el que se limpió la habitación
	 * @return habitación creada, null en caso de que no se haya creado
	 */
	HabitacionBBDD anadirHabitacion(String nombre, long idMes);

	/**
	 * Obtiene la habitación de una {@link TareaBBDD}
	 * 
	 * @param idMes id del mes en el que se limpió la habitación
	 * @return Habitación pertneciente a la tarea, null en caso de que no exista
	 */
	List<HabitacionBBDD> getHabitaciones(long idMes);

	/**
	 * Obtiene una habitación por su id
	 * 
	 * @param idHabitacion id de la habitación
	 * @return Habitación con el id indicado, null en caso de que no exista
	 */
	HabitacionBBDD getHabitacion(long idHabitacion);

	/**
	 * elimina una habitación y el mes si no hay habitaciones
	 * 
	 * @param idHabitacion id de la habitación a eliminar
	 * @return true si se ha eliminado la habitación, false en caso contrario
	 */
	boolean eliminarHabitacion(long idHabitacion);
}
