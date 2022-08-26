package es.rbp.tareas_borderia.service;

import java.util.List;

import es.rbp.tareas_borderia.entidad.bbdd.HabitacionConfigBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.UsuarioBBDD;
import es.rbp.tareas_borderia.entidad.config.HabitacionConfig;

public interface ServicioHabitacionConfig {

	/**
	 * Crea una nueva habitación de configuración nueva
	 * 
	 * @param nombre nombre de la habitación nueva
	 * @return Habitacion de configuración creada
	 */
	HabitacionConfigBBDD anadirHabitacion(String nombre, UsuarioBBDD usuario);

	/**
	 * Devuelve todas las habitaciones de configuración
	 * 
	 * @return List con todas las habitaciones de configuración
	 */
	List<HabitacionConfigBBDD> findAll();

	/**
	 * Busca una habitación de confoguración por su id
	 * 
	 * @param id id de la habitación de configuración
	 * @return habitación de configuración con el id indicado, null si no existe
	 */
	HabitacionConfigBBDD findById(long id);

	/**
	 * Modifica una habitación de configuración
	 * 
	 * @param habitacionConfigBBDD habitación de configuración con los datos
	 *                             actualizados
	 * @param usuario              usuario que realiza la modificación
	 * @return true si se ha eliminado la habitación de configuración, false en caso
	 *         contrario
	 */
	boolean modificarHabitacion(HabitacionConfig habitacionConfigBBDD, UsuarioBBDD usuario);

	/**
	 * Elimina una habitación de configuración
	 * 
	 * @param idHabitacionConfig id de la habitación de configuración a eliminar
	 * @return true si se ha eliminado la habtación de configuración, false en caso
	 *         contrario
	 */
	boolean eliminarHabitacion(long idHabitacionConfig);
}
