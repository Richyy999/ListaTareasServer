package es.rbp.tareas_borderia.service;

import java.util.List;

import es.rbp.tareas_borderia.entidad.bbdd.MesBBDD;

public interface ServicioMes {

	/**
	 * Obtiene el mes actual del usuario o crea uno nuevo si es necesario
	 * 
	 * @param idUsuario id del usuario del que se desea obtener sus meses
	 * @return mes actual
	 */
	MesBBDD getMes(long idUsuario);

	/**
	 * Obtiene todos los meses del usuario
	 * 
	 * @param idUsuario Id del usuario al que pertenecen los meses
	 * @return Lista con todos los meses de un usuario
	 */
	List<MesBBDD> getMeses(long idUsuario);

	/**
	 * Elimina un mes si está vacío
	 * 
	 * @param idMes id del mes a eliminar
	 * 
	 * @return true si se ha eliminado el mes, false en caso contrario
	 */
	boolean eliminarMes(long idMes);
}
