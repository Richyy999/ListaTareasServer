package es.rbp.tareas_borderia.service;

import java.util.List;

import es.rbp.tareas_borderia.entidad.bbdd.HistorialBBDD;

public interface ServicioHistorial {

	/**
	 * Obtiene todos los historiales
	 * 
	 * @return todos los historiales
	 */
	List<HistorialBBDD> findAll();

	/**
	 * Busca un historial por su ID
	 * 
	 * @param id id del historial
	 * @return Historial con el ID indicado, null en caso de que no exista
	 */
	HistorialBBDD findById(long id);

	/**
	 * Busca los historiales por id de habitación de configuración
	 * 
	 * @param idHabitacionConfig id de la habitación de configuración por el que
	 *                           buscar
	 * @return lista de historiales asociados al id de habitación de configuración
	 *         indicado
	 */
	List<HistorialBBDD> findByIdHabitacionConfig(long idHabitacionConfig);

	/**
	 * Actualiza el historial al limpiar una habitación
	 * 
	 * @param idHabitacion id de la habitación que se ha limpiado
	 * @param idUsuario    id del usuario que ha limpiado la habitación
	 */
	void actualizarHistorial(long idHabitacion, long idUsuario);

	/**
	 * Crea un historial de una habitación
	 * 
	 * @param idHabitacionConfig id de la habitación de configuración a la que hará
	 *                           referencia
	 * @param nombreHabitacion   nombre de la habitación que se limpia
	 * @return true si se ha creado correctamente, false en caso contrario
	 */
	boolean crearHistorial(long idHabitacionConfig, String nombreHabitacion, long idUsuario);

	/**
	 * Modifica el nombre de la habitación del historial
	 * 
	 * @param idHistorial      id del historial que se desea modificar
	 * @param nombreHabitacion nombre nuevo de la habitación
	 * @return true si se ha modificado correctamente, false en caso contrario
	 */
	boolean modificarHistorial(long idHistorial, String nombreHabitacion);

	/**
	 * Elimina un historial
	 * 
	 * @param idHistorial id del historial que se desea elminiar
	 * @return true si se ha eliminado correctamente, fals een caso contrario
	 */
	boolean eliminarHistorial(long idHistorial);
}
