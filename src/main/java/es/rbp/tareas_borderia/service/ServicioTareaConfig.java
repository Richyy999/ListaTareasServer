package es.rbp.tareas_borderia.service;

import java.util.List;

import es.rbp.tareas_borderia.entidad.bbdd.TareaConfigBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.UsuarioBBDD;
import es.rbp.tareas_borderia.entidad.config.TareaConfig;

public interface ServicioTareaConfig {

	/**
	 * Devuelve todas las tareas de configuración para una habitación de
	 * configuración
	 * 
	 * @param idHabitacionConfig id de la habitación de configuración que contiene
	 *                           las tareas de configuración
	 * @return List con las tareas de configuración de la habitación indicada
	 */
	List<TareaConfigBBDD> findByIdHabitacionConfig(long idHabitacionConfig);

	/**
	 * añade una tarea de limpieza de configuración a una habitación
	 * 
	 * @param tareaLimpiezaConfig {@link TareaConfigBBDD} con los datos
	 * @param usuario             usuario que realiza el alta
	 * @return true si se ha añadido, false en caso contrario
	 */
	boolean anadirTareaLimpiezaConfig(TareaConfigBBDD tareaLimpiezaConfig, UsuarioBBDD usuario);

	/**
	 * Crea una tarea de configuración y la añade a la habitación de muestra
	 * 
	 * @param tareaConfig tarea de configuración con los datos de la tarea de
	 *                    configuración
	 * @param usuario     usuario que crea la tarea de configuración
	 * @return true si se ha creado correctamente, false en caso contrario
	 */
	boolean crearTareaConfig(TareaConfig tareaConfig, UsuarioBBDD usuario);

	/**
	 * Modifica una tarea de limpieza de configuración
	 * 
	 * @param tareaLimpiezaConfig {@link TareaConfigBBDD} con los datos actualizados
	 * @param usuario             usuario que realiza la modificación
	 * @return true si se ha actualizado correctamente, false en caso contrario
	 */
	boolean modificarTareaLimpiezaConfig(TareaConfig tareaLimpiezaConfig, UsuarioBBDD usuario);

	/**
	 * elimina una tarea de limpieza de configuración
	 * 
	 * @param idTareaLimpiezaConfig id de la tarea de limpieza de configuración a
	 *                              eliminar
	 * @return true si se ha eliminado, false en caso contrario
	 */
	boolean eliminarTareaConfig(long idTareaLimpiezaConfig);
}
