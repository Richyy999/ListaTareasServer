package es.rbp.tareas_borderia.controlador;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.rbp.tareas_borderia.entidad.Historial;
import es.rbp.tareas_borderia.entidad.IDWrapper;
import es.rbp.tareas_borderia.entidad.Usuario;
import es.rbp.tareas_borderia.entidad.bbdd.HabitacionConfigBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.HistorialBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.TareaConfigBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.UsuarioBBDD;
import es.rbp.tareas_borderia.entidad.config.HabitacionConfig;
import es.rbp.tareas_borderia.entidad.config.TareaConfig;
import es.rbp.tareas_borderia.service.ServicioHabitacionConfig;
import es.rbp.tareas_borderia.service.ServicioHistorial;
import es.rbp.tareas_borderia.service.ServicioTareaConfig;
import es.rbp.tareas_borderia.service.ServicioUsuario;

import static es.rbp.tareas_borderia.controlador.ConstantesControlador.*;
import static es.rbp.tareas_borderia.service.Acciones.ACCION_VER_TAREAS_CONFIG;
import static es.rbp.tareas_borderia.service.Acciones.ACCION_CREAR_TAREA_CONFIG;
import static es.rbp.tareas_borderia.service.Acciones.ACCION_MODIFICAR_TAREA_CONFIG;
import static es.rbp.tareas_borderia.service.Acciones.ACCION_ELIMINAR_TAREA_CONFIG;
import static es.rbp.tareas_borderia.service.Acciones.ACCION_VER_HABITACIONES_CONFIG;
import static es.rbp.tareas_borderia.service.Acciones.ACCION_CREAR_HABITACION_CONFIG;
import static es.rbp.tareas_borderia.service.Acciones.ACCION_MODIFICAR_HABITACION_CONFIG;
import static es.rbp.tareas_borderia.service.Acciones.ACCION_ELIMINAR_HABITACION_CONFIG;
import static es.rbp.tareas_borderia.service.Acciones.ACCION_CREAR_HISTORIAL;
import static es.rbp.tareas_borderia.service.Acciones.ACCION_MODIFICAR_HISTORIAL;
import static es.rbp.tareas_borderia.service.Acciones.ACCION_ELIMINAR_HISTORIAL;

@RestController
@RequestMapping("/admin")
public class ControladorAdmin {

	@Autowired
	private ServicioUsuario servicioUsuario;

	@Autowired
	private ServicioTareaConfig servicioTareaConfig;

	@Autowired
	private ServicioHabitacionConfig servicioHabitacionConfig;

	@Autowired
	private ServicioHistorial servicioHistorial;

	/**
	 * Obtiene todas las tareas de configuración
	 * 
	 * @param idUsuario id del usuario que desea obtener las tareas de configuración
	 * @param token     token único del usuario para identificarlo
	 * @return todos las tareas de configuración
	 */
	@GetMapping(path = "/tareas", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<TareaConfig>> getTareasConfig(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_VER_TAREAS_CONFIG, idUsuario))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		List<TareaConfig> tareasConfig = getTareasConfig();
		return new ResponseEntity<List<TareaConfig>>(tareasConfig, HttpStatus.OK);
	}

	/**
	 * Obtiene todas las habitaciones de configuración
	 * 
	 * @param idUsuario id del usuario que desea obtener las habitaciones de
	 *                  configuración
	 * @param token     token único del usuario para identificarlo
	 * @return lista con todas las habitaciones de configuración
	 */
	@GetMapping(path = "/habitaciones", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<HabitacionConfig>> getHabitacionesConfig(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_VER_HABITACIONES_CONFIG, idUsuario))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		List<HabitacionConfig> habitacionesConfig = getHabitacionesConfig();
		return new ResponseEntity<List<HabitacionConfig>>(habitacionesConfig, HttpStatus.OK);
	}

	/**
	 * Crea una tarea de configuración
	 * 
	 * @param idUsuario   id del usuario que desea crear la tarea de configuración
	 * @param token       token único del usuario para identificarlo
	 * @param tareaConfig Tarea de configuración con los datos para crear una nueva
	 * @return lista con todas las tareas de configuración
	 */
	@PostMapping(path = "/tarea/anadir", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<TareaConfig>> anadirTarea(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody TareaConfig tareaConfig) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_CREAR_TAREA_CONFIG, idUsuario))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (servicioTareaConfig.crearTareaConfig(tareaConfig, usuarioBBDD))
			return new ResponseEntity<List<TareaConfig>>(getTareasConfig(), HttpStatus.OK);

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Modifica una tarea de configuración
	 * 
	 * @param idUsuario   id del usuario que desea modificar la tarea de
	 *                    configuración
	 * @param token       token único del usuario para identificarlo
	 * @param tareaConfig tarea de configuracion con los datos actualizados
	 * @return lista con todas las tareas de configuración
	 */
	@PutMapping(path = "/tarea/modificar", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<TareaConfig>> modificarTareas(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody TareaConfig tareaConfig) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_MODIFICAR_TAREA_CONFIG, idUsuario))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (servicioTareaConfig.modificarTareaLimpiezaConfig(tareaConfig, usuarioBBDD))
			return new ResponseEntity<List<TareaConfig>>(getTareasConfig(), HttpStatus.OK);

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Elimina tareas de configuración
	 * 
	 * @param idUsuario id del usuario que desea eliminar tareas de configuración
	 * @param token     token único del usuario para identificarlo
	 * @param ids       lista con los ID de las gtareas de configuración a eliminar
	 * @return lista con todas las tareas de configuración
	 */
	@DeleteMapping(path = "/tarea/eliminar", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<TareaConfig>> eliminarTareaConfig(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody IDWrapper ids) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_ELIMINAR_TAREA_CONFIG, idUsuario))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		for (long id : ids.getIds()) {
			servicioTareaConfig.eliminarTareaConfig(id);
		}

		return new ResponseEntity<List<TareaConfig>>(getTareasConfig(), HttpStatus.OK);
	}

	/**
	 * Crea una habitación de configuración
	 * 
	 * @param idUsuario        id del usuario que desea crear la habitación de
	 *                         configuración
	 * @param token            token único del usuario para identificarlo
	 * @param habitacionConfig habitación de configuración
	 * @return lista con las habitaciones de configuración
	 */
	@PostMapping(path = "/habitacion/anadir", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<HabitacionConfig>> anadirHabitacion(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody HabitacionConfig habitacionConfig) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_CREAR_HABITACION_CONFIG, idUsuario))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		HabitacionConfigBBDD habitacionConfigBBDD = servicioHabitacionConfig
				.anadirHabitacion(habitacionConfig.getNombre(), usuarioBBDD);
		if (habitacionConfigBBDD == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		for (TareaConfig tareaConfig : habitacionConfig.getTareas()) {
			TareaConfigBBDD tareaConfigBBDD = new TareaConfigBBDD();
			tareaConfigBBDD.setIdHabitacionConfig(habitacionConfigBBDD.getId());
			tareaConfigBBDD.setNombre(tareaConfig.getNombre());
			tareaConfigBBDD.setPrecio(tareaConfig.getPrecio());
			servicioTareaConfig.anadirTareaLimpiezaConfig(tareaConfigBBDD, usuarioBBDD);
		}

		List<HabitacionConfig> habitacionesConfig = getHabitacionesConfig();
		return new ResponseEntity<List<HabitacionConfig>>(habitacionesConfig, HttpStatus.OK);
	}

	/**
	 * Modifica una habitación de configuración
	 * 
	 * @param idUsuario        id del usuario que desea modificar la habitación de
	 *                         configuración
	 * @param token            token único del usuario para identificarlo
	 * @param habitacionConfig habitación de configuración con los datos
	 *                         actualizados
	 * @return lista con todas las habitaciones de configuración
	 */
	@PutMapping(path = "/habitacion/modificar", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<HabitacionConfig>> modificarHabitacionConfig(
			@RequestParam(name = ID_USUARIO) Long idUsuario, @RequestHeader(name = CABECERA_TOKEN) String token,
			@RequestBody HabitacionConfig habitacionConfig) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_MODIFICAR_HABITACION_CONFIG, idUsuario))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioHabitacionConfig.modificarHabitacion(habitacionConfig, usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		for (TareaConfig tareaConfig : habitacionConfig.getTareas()) {
			servicioTareaConfig.modificarTareaLimpiezaConfig(tareaConfig, usuarioBBDD);
		}

		List<HabitacionConfig> habitacionesConfig = getHabitacionesConfig();
		return new ResponseEntity<List<HabitacionConfig>>(habitacionesConfig, HttpStatus.OK);
	}

	/**
	 * Elimina habitaciones de configuración y sus historiales correspondientes
	 * 
	 * @param idUsuario id del usuario que desea eliminar las habitaciones de
	 *                  configuración
	 * @param token     token único del usuario para identificarlo
	 * @param ids       lista con los ID de las habitaciones de configuración a
	 *                  eliminar
	 * @return lista con todas las habitaciones de configuración
	 */
	@DeleteMapping(path = "/habitacion/eliminar", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<HabitacionConfig>> eliminarHabitacionConfig(
			@RequestParam(name = ID_USUARIO) Long idUsuario, @RequestHeader(name = CABECERA_TOKEN) String token,
			@RequestBody IDWrapper ids) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_ELIMINAR_HABITACION_CONFIG, idUsuario))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		for (long id : ids.getIds()) {
			List<TareaConfigBBDD> tareasConfig = servicioTareaConfig.findByIdHabitacionConfig(id);
			for (TareaConfigBBDD tareaConfig : tareasConfig) {
				servicioTareaConfig.eliminarTareaConfig(tareaConfig.getId());
			}

			List<HistorialBBDD> historiales = servicioHistorial.findByIdHabitacionConfig(id);
			for (HistorialBBDD historialBBDD : historiales) {
				servicioHistorial.eliminarHistorial(historialBBDD.getId());
			}

			servicioHabitacionConfig.eliminarHabitacion(id);
		}

		List<HabitacionConfig> habitacionesConfig = getHabitacionesConfig();
		return new ResponseEntity<List<HabitacionConfig>>(habitacionesConfig, HttpStatus.OK);
	}

	/**
	 * Crea un historial de limpieza nuevo
	 * 
	 * @param idUsuario     id del usuario que desea crear el historial
	 * @param token         token único del usuario para identificarlo
	 * @param historialBBDD historial con los datos necesarios para crearlo
	 * @return lista de todos los historiales
	 */
	@PostMapping(path = "/historial/crear", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<Historial>> anadirHistorial(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody HistorialBBDD historialBBDD) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_CREAR_HISTORIAL, idUsuario))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (servicioHistorial.crearHistorial(historialBBDD.getIdHabitacionConfig(), historialBBDD.getNombreHabitacion(),
				idUsuario))
			return new ResponseEntity<List<Historial>>(getHistorial(), HttpStatus.OK);

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Modifica un historial
	 * 
	 * @param idUsuario id del usuario que desea modificar el historial
	 * @param token     token único del usuario para identificarlo
	 * @param historial historial con los datos actualizados
	 * @return lista de todos los historiales
	 */
	@PutMapping(path = "/historial/modificar", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<Historial>> modificarHistorial(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody Historial historial) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_MODIFICAR_HISTORIAL, idUsuario))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (servicioHistorial.modificarHistorial(historial.getId(), historial.getNombreHabitacion()))
			return new ResponseEntity<List<Historial>>(getHistorial(), HttpStatus.OK);

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Elimina historiales
	 * 
	 * @param idUsuario id del usuario que desea eliminar los historiales
	 * @param token     token único del usuario para identificarlo
	 * @param ids       lista de los IDs de los historiales a eliminar
	 * @return lista con todos los jistoriales
	 */
	@DeleteMapping(path = "/historial/eliminar", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<Historial>> eliminarHistoriales(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody IDWrapper ids) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_ELIMINAR_HISTORIAL, idUsuario))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		for (long id : ids.getIds()) {
			servicioHistorial.eliminarHistorial(id);
		}

		return new ResponseEntity<>(getHistorial(), HttpStatus.OK);
	}

	/**
	 * Obtiene las tareas de configuración de la habitación de muestra
	 * 
	 * @return tareas de configuración de la habitación extra
	 */
	private List<TareaConfig> getTareasConfig() {
		List<TareaConfigBBDD> tareasConfigBBDD = servicioTareaConfig
				.findByIdHabitacionConfig(HabitacionConfigBBDD.ID_HABITACION_MUESTRA);
		List<TareaConfig> tareasConfig = new ArrayList<>();
		for (TareaConfigBBDD tareaConfigBBDD : tareasConfigBBDD) {
			TareaConfig tareaConfig = new TareaConfig(tareaConfigBBDD);
			tareasConfig.add(tareaConfig);
		}

		return tareasConfig;
	}

	/**
	 * Obtiene todas las habitaciones de configuración
	 * 
	 * @return todas las habitaciones de configuración excepto la habitación de
	 *         muestra
	 */
	private List<HabitacionConfig> getHabitacionesConfig() {
		List<HabitacionConfigBBDD> habitacionesConfigBBDD = servicioHabitacionConfig.findAll();
		List<HabitacionConfig> habitacionesConfig = new ArrayList<>();
		for (HabitacionConfigBBDD habitacionConfigBBDD : habitacionesConfigBBDD) {
			List<TareaConfigBBDD> tareasConfigBBDD = servicioTareaConfig
					.findByIdHabitacionConfig(habitacionConfigBBDD.getId());
			List<TareaConfig> tareasConfig = new ArrayList<>();
			for (TareaConfigBBDD tareaConfigBBDD : tareasConfigBBDD) {
				TareaConfig tareaConfig = new TareaConfig(tareaConfigBBDD);
				tareasConfig.add(tareaConfig);
			}
			HabitacionConfig habitacionConfig = new HabitacionConfig(habitacionConfigBBDD, tareasConfig);
			habitacionesConfig.add(habitacionConfig);
		}

		return habitacionesConfig;
	}

	/**
	 * Onbtiene los historiales de las habitaciones limpiadas
	 * 
	 * @return historial de limpieza
	 */
	private List<Historial> getHistorial() {
		List<HistorialBBDD> historialesBBDD = servicioHistorial.findAll();
		List<Historial> historiales = new ArrayList<>();
		for (HistorialBBDD historialBBDD : historialesBBDD) {
			UsuarioBBDD usuarioBBDDHistorial = servicioUsuario.findById(historialBBDD.getIdUsuario());
			Usuario usuarioHistorial = new Usuario(usuarioBBDDHistorial);

			HabitacionConfigBBDD habitacionConfigBBDD = servicioHabitacionConfig
					.findById(historialBBDD.getIdHabitacionConfig());

			List<TareaConfigBBDD> tareasConfigBBDD = servicioTareaConfig
					.findByIdHabitacionConfig(habitacionConfigBBDD.getId());
			List<TareaConfig> tareasConfig = new ArrayList<>();
			for (TareaConfigBBDD tareaConfigBBDD : tareasConfigBBDD) {
				TareaConfig tareaConfig = new TareaConfig(tareaConfigBBDD);
				tareasConfig.add(tareaConfig);
			}
			HabitacionConfig habitacionConfig = new HabitacionConfig(habitacionConfigBBDD, tareasConfig);

			Historial historial = new Historial(historialBBDD, usuarioHistorial, habitacionConfig);
			historiales.add(historial);
		}

		return historiales;
	}
}
