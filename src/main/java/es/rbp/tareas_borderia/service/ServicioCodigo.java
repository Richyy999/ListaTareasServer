package es.rbp.tareas_borderia.service;

import java.util.List;

import es.rbp.tareas_borderia.entidad.bbdd.Codigo;

public interface ServicioCodigo {

	/**
	 * Busca todos los códigos almacenados
	 * 
	 * @return todos los códigos
	 */
	List<Codigo> findAll();

	/**
	 * Busca un código a partir de su código
	 * 
	 * @param codigo secuencia numérica que conforma el código
	 * @return Código que posea la misma secuencia y tipo, null si no hay
	 *         coincidencias
	 */
	Codigo findByCodigo(int codigo);

	/**
	 * Crea un código nuevo
	 * 
	 * @param codigo código con los datos
	 * @return true si se ha creado, false en caso contrario
	 */
	boolean crearCodigo(Codigo codigo);

	/**
	 * Modifica un código
	 * 
	 * @param codigo código con los datos actualizados
	 * @return true si se ha modificado, false en caso contrario
	 */
	boolean modificarCodigo(Codigo codigo);

	/**
	 * Elimina un código
	 * 
	 * @param codigo código a eliminar
	 * @return true si se ha eliminado, false en caso contrario
	 */
	boolean eliminarCodigo(long id);
}
