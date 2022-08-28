package es.rbp.tareas_borderia.service;

import es.rbp.tareas_borderia.entidad.bbdd.DeudaConfigBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.UsuarioBBDD;
import es.rbp.tareas_borderia.entidad.config.DeudaConfig;

public interface ServicioDeudaConfig {

	/**
	 * Busca la última deuda de configuración
	 * 
	 * @return última deuda de configuración, null si no existe
	 */
	DeudaConfigBBDD getUltimaDeudaConfig();

	/**
	 * Crea una deuda de configuración y si ya existe, la modifica
	 * 
	 * @param deudaConfig deuda de configuración con los datos
	 * @param usuario     usuario que crea la deuda de configuración
	 * @return la última deuda de configuración
	 */
	boolean crearDeudaConfig(DeudaConfig deudaConfig, UsuarioBBDD usuario);

	/**
	 * Modifica la deuda de configuración
	 * 
	 * @param deudaConfig
	 * @return
	 */
	boolean modificarDeudaConfig(DeudaConfig deudaConfig);
}
