package es.rbp.tareas_borderia.service;

import java.util.List;

import es.rbp.tareas_borderia.entidad.Termino;
import es.rbp.tareas_borderia.entidad.bbdd.TerminoBBDD;

public interface ServicioTermino {

	/**
	 * Devuelve todos los términos
	 * 
	 * @return {@link List} con todos los términos
	 */
	List<TerminoBBDD> findAlll();

	/**
	 * Añade un término y actualiza los datos de los usuarios para que lo acepten al
	 * volver a haver login
	 * 
	 * @param termino término a añadir
	 * @return true si se ha añadido correctamente, false en caso de algún error
	 */
	boolean anadirTermino(Termino termino);

	/**
	 * Modifica un término
	 * 
	 * @param titulo  título del término a modificar
	 * @param termino término con los datos nuevos
	 * @return true si se ha modificado, false en caso contrario
	 */
	boolean modificarTermino(String titulo, Termino termino);

	/**
	 * Elimina un término
	 * 
	 * @param titulo título del término a eliminar
	 * @return true si se ha eliminado, false en caso contrario
	 */
	boolean eliminarTermino(String titulo);
}
