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
import es.rbp.tareas_borderia.entidad.Termino;
import es.rbp.tareas_borderia.entidad.Usuario;
import es.rbp.tareas_borderia.entidad.bbdd.Codigo;
import es.rbp.tareas_borderia.entidad.bbdd.HabitacionConfigBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.HistorialBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.TareaConfigBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.TerminoBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.UsuarioBBDD;
import es.rbp.tareas_borderia.entidad.config.HabitacionConfig;
import es.rbp.tareas_borderia.entidad.config.TareaConfig;
import es.rbp.tareas_borderia.service.ServicioCodigo;
import es.rbp.tareas_borderia.service.ServicioHabitacionConfig;
import es.rbp.tareas_borderia.service.ServicioHistorial;
import es.rbp.tareas_borderia.service.ServicioTareaConfig;
import es.rbp.tareas_borderia.service.ServicioTermino;
import es.rbp.tareas_borderia.service.ServicioUsuario;

import static es.rbp.tareas_borderia.controlador.ConstantesControlador.*;
import static es.rbp.tareas_borderia.service.Acciones.Admin.*;

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

	@Autowired
	private ServicioCodigo servicioCodigo;

	@Autowired
	private ServicioTermino servicioTermino;

	// -------------------- TAREAS CONFIG --------------------

	/**
	 * Obtiene todas las tareas de configuraci??n
	 * 
	 * @param idUsuario id del usuario que desea obtener las tareas de configuraci??n
	 * @param token     token ??nico del usuario para identificarlo
	 * @return todos las tareas de configuraci??n
	 */
	@GetMapping(path = "/tareas", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<TareaConfig>> getTareasConfig(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioUsuario.tieneSesionActiva(usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_VER_TAREAS_CONFIG))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		List<TareaConfig> tareasConfig = getTareasConfig();
		return new ResponseEntity<List<TareaConfig>>(tareasConfig, HttpStatus.OK);
	}

	/**
	 * Crea una tarea de configuraci??n
	 * 
	 * @param idUsuario   id del usuario que desea crear la tarea de configuraci??n
	 * @param token       token ??nico del usuario para identificarlo
	 * @param tareaConfig Tarea de configuraci??n con los datos para crear una nueva
	 * @return lista con todas las tareas de configuraci??n
	 */
	@PostMapping(path = "/tarea/anadir", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<TareaConfig>> anadirTarea(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody TareaConfig tareaConfig) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioUsuario.tieneSesionActiva(usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_CREAR_TAREA_CONFIG))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (servicioTareaConfig.crearTareaConfig(tareaConfig, usuarioBBDD))
			return new ResponseEntity<List<TareaConfig>>(getTareasConfig(), HttpStatus.OK);

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Modifica una tarea de configuraci??n
	 * 
	 * @param idUsuario   id del usuario que desea modificar la tarea de
	 *                    configuraci??n
	 * @param token       token ??nico del usuario para identificarlo
	 * @param tareaConfig tarea de configuracion con los datos actualizados
	 * @return lista con todas las tareas de configuraci??n
	 */
	@PutMapping(path = "/tarea/modificar", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<TareaConfig>> modificarTareas(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody TareaConfig tareaConfig) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioUsuario.tieneSesionActiva(usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_MODIFICAR_TAREA_CONFIG))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (servicioTareaConfig.modificarTareaLimpiezaConfig(tareaConfig, usuarioBBDD))
			return new ResponseEntity<List<TareaConfig>>(getTareasConfig(), HttpStatus.OK);

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Elimina tareas de configuraci??n
	 * 
	 * @param idUsuario id del usuario que desea eliminar tareas de configuraci??n
	 * @param token     token ??nico del usuario para identificarlo
	 * @param ids       lista con los ID de las gtareas de configuraci??n a eliminar
	 * @return lista con todas las tareas de configuraci??n
	 */
	@DeleteMapping(path = "/tarea/eliminar", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<TareaConfig>> eliminarTareaConfig(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestParam(name = "tarea") Long idTarea, @RequestHeader(name = CABECERA_TOKEN) String token) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioUsuario.tieneSesionActiva(usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_ELIMINAR_TAREA_CONFIG))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		servicioTareaConfig.eliminarTareaConfig(idTarea);

		return new ResponseEntity<List<TareaConfig>>(getTareasConfig(), HttpStatus.OK);
	}

	// -------------------- HABITACIONES CONFIG --------------------

	/**
	 * Obtiene todas las habitaciones de configuraci??n
	 * 
	 * @param idUsuario id del usuario que desea obtener las habitaciones de
	 *                  configuraci??n
	 * @param token     token ??nico del usuario para identificarlo
	 * @return lista con todas las habitaciones de configuraci??n
	 */
	@GetMapping(path = "/habitaciones", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<HabitacionConfig>> getHabitacionesConfig(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioUsuario.tieneSesionActiva(usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_VER_HABITACIONES_CONFIG))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		List<HabitacionConfig> habitacionesConfig = getHabitacionesConfig();
		return new ResponseEntity<List<HabitacionConfig>>(habitacionesConfig, HttpStatus.OK);
	}

	/**
	 * Crea una habitaci??n de configuraci??n
	 * 
	 * @param idUsuario        id del usuario que desea crear la habitaci??n de
	 *                         configuraci??n
	 * @param token            token ??nico del usuario para identificarlo
	 * @param habitacionConfig habitaci??n de configuraci??n
	 * @return lista con las habitaciones de configuraci??n
	 */
	@PostMapping(path = "/habitacion/anadir", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<HabitacionConfig>> anadirHabitacion(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody HabitacionConfig habitacionConfig) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioUsuario.tieneSesionActiva(usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_CREAR_HABITACION_CONFIG))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		HabitacionConfigBBDD habitacionConfigBBDD = servicioHabitacionConfig
				.anadirHabitacion(habitacionConfig.getNombre(), usuarioBBDD);
		if (habitacionConfigBBDD == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		habitacionConfig.setId(habitacionConfigBBDD.getId());

		for (TareaConfig tareaConfig : habitacionConfig.getTareas()) {
			TareaConfigBBDD tareaId = servicioTareaConfig.findById(tareaConfig.getId());

			TareaConfig tareaConfigNueva = new TareaConfig();
			tareaConfigNueva.setNombre(tareaId.getNombre());
			tareaConfigNueva.setPrecio(tareaId.getPrecio());

			servicioTareaConfig.anadirTareaLimpiezaConfig(tareaConfigNueva, habitacionConfig, usuarioBBDD);
		}

		List<HabitacionConfig> habitacionesConfig = getHabitacionesConfig();
		return new ResponseEntity<List<HabitacionConfig>>(habitacionesConfig, HttpStatus.OK);
	}

	/**
	 * Modifica una habitaci??n de configuraci??n
	 * 
	 * @param idUsuario        id del usuario que desea modificar la habitaci??n de
	 *                         configuraci??n
	 * @param token            token ??nico del usuario para identificarlo
	 * @param habitacionConfig habitaci??n de configuraci??n con los datos
	 *                         actualizados
	 * @return lista con todas las habitaciones de configuraci??n
	 */
	@PutMapping(path = "/habitacion/modificar", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<HabitacionConfig>> modificarHabitacionConfig(
			@RequestParam(name = ID_USUARIO) Long idUsuario, @RequestHeader(name = CABECERA_TOKEN) String token,
			@RequestBody HabitacionConfig habitacionConfig) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioUsuario.tieneSesionActiva(usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_MODIFICAR_HABITACION_CONFIG))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		// Elimino las originales
		List<TareaConfigBBDD> tareasConfigOriginales = servicioTareaConfig
				.findByIdHabitacionConfig(habitacionConfig.getId());

		for (TareaConfigBBDD tareaConfigBBDD : tareasConfigOriginales) {
			if (!servicioTareaConfig.eliminarTareaConfig(tareaConfigBBDD.getId()))
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		// Creo las nuevas
		for (TareaConfig tareaConfig : habitacionConfig.getTareas()) {
			TareaConfigBBDD tareaId = servicioTareaConfig.findById(tareaConfig.getId());

			TareaConfig tareaConfigNueva = new TareaConfig();
			tareaConfigNueva.setNombre(tareaId.getNombre());
			tareaConfigNueva.setPrecio(tareaId.getPrecio());

			if (!servicioTareaConfig.anadirTareaLimpiezaConfig(tareaConfigNueva, habitacionConfig, usuarioBBDD))
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		// Modifico la habitaci??n
		if (!servicioHabitacionConfig.modificarHabitacion(habitacionConfig, usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		List<HabitacionConfig> habitacionesConfig = getHabitacionesConfig();
		return new ResponseEntity<List<HabitacionConfig>>(habitacionesConfig, HttpStatus.OK);
	}

	/**
	 * Elimina habitaciones de configuraci??n y sus historiales correspondientes
	 * 
	 * @param idUsuario id del usuario que desea eliminar las habitaciones de
	 *                  configuraci??n
	 * @param token     token ??nico del usuario para identificarlo
	 * @param ids       lista con los ID de las habitaciones de configuraci??n a
	 *                  eliminar
	 * @return lista con todas las habitaciones de configuraci??n
	 */
	@DeleteMapping(path = "/habitacion/eliminar", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<HabitacionConfig>> eliminarHabitacionConfig(
			@RequestParam(name = ID_USUARIO) Long idUsuario, @RequestParam(name = "habitacion") Long idHabitacion,
			@RequestHeader(name = CABECERA_TOKEN) String token) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioUsuario.tieneSesionActiva(usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_ELIMINAR_HABITACION_CONFIG))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		List<TareaConfigBBDD> tareasConfig = servicioTareaConfig.findByIdHabitacionConfig(idHabitacion);
		for (TareaConfigBBDD tareaConfig : tareasConfig) {
			servicioTareaConfig.eliminarTareaConfig(tareaConfig.getId());
		}

		List<HistorialBBDD> historiales = servicioHistorial.findByIdHabitacionConfig(idHabitacion);
		for (HistorialBBDD historialBBDD : historiales) {
			servicioHistorial.eliminarHistorial(historialBBDD.getId());
		}

		servicioHabitacionConfig.eliminarHabitacion(idHabitacion);

		List<HabitacionConfig> habitacionesConfig = getHabitacionesConfig();
		return new ResponseEntity<List<HabitacionConfig>>(habitacionesConfig, HttpStatus.OK);
	}

	// -------------------- HISTORIAL --------------------

	/**
	 * Crea un historial de limpieza nuevo
	 * 
	 * @param idUsuario     id del usuario que desea crear el historial
	 * @param token         token ??nico del usuario para identificarlo
	 * @param historialBBDD historial con los datos necesarios para crearlo
	 * @return lista de todos los historiales
	 */
	@PostMapping(path = "/historial/anadir", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<Historial>> anadirHistorial(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody Historial historial) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioUsuario.tieneSesionActiva(usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_CREAR_HISTORIAL))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (servicioHistorial.crearHistorial(historial.getHabitacionConfig().getId(), historial.getNombreHabitacion()))
			return new ResponseEntity<List<Historial>>(getHistorial(), HttpStatus.OK);

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Modifica un historial
	 * 
	 * @param idUsuario id del usuario que desea modificar el historial
	 * @param token     token ??nico del usuario para identificarlo
	 * @param historial historial con los datos actualizados
	 * @return lista de todos los historiales
	 */
	@PutMapping(path = "/historial/modificar", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<Historial>> modificarHistorial(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody Historial historial) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioUsuario.tieneSesionActiva(usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_MODIFICAR_HISTORIAL))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (servicioHistorial.modificarHistorial(historial.getId(), historial.getNombreHabitacion()))
			return new ResponseEntity<List<Historial>>(getHistorial(), HttpStatus.OK);

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Elimina historiales
	 * 
	 * @param idUsuario id del usuario que desea eliminar los historiales
	 * @param token     token ??nico del usuario para identificarlo
	 * @param ids       lista de los IDs de los historiales a eliminar
	 * @return lista con todos los jistoriales
	 */
	@DeleteMapping(path = "/historial/eliminar", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<Historial>> eliminarHistorial(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestParam(name = "historial") Long idHistorial, @RequestHeader(name = CABECERA_TOKEN) String token) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioUsuario.tieneSesionActiva(usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_ELIMINAR_HISTORIAL))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		servicioHistorial.eliminarHistorial(idHistorial);

		return new ResponseEntity<>(getHistorial(), HttpStatus.OK);
	}

	// -------------------- TERMINOS --------------------

	/**
	 * Crea un t??rmino nuevo
	 * 
	 * @param idUsuario id del usuario que desea crear el t??rmino
	 * @param token     token ??nico del usuario para identificarlo
	 * @param termino   t??rmino a crear
	 * @return lista con todos los t??rminos
	 */
	@PostMapping(path = "/termino/anadir", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<Termino>> anadirTermino(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody Termino termino) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioUsuario.tieneSesionActiva(usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_ANADIR_TERMINOS))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (servicioTermino.anadirTermino(termino))
			return new ResponseEntity<List<Termino>>(getTerminos(servicioTermino.findAlll()), HttpStatus.OK);

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Modifica un t??rmino
	 * 
	 * @param idUsuario id del usuario que desea modificar un t??rmino
	 * @param token     token ??nico del usuario para identificarlo
	 * @param termino   t??rmino con los datos nuevos
	 * @return lista con todos los t??rminos
	 */
	@PutMapping(path = "/termino/modificar", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<Termino>> modificarTermino(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody Termino termino) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioUsuario.tieneSesionActiva(usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_MODIFICAR_TERMINOS))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (servicioTermino.modificarTermino(termino.getTitulo(), termino))
			return new ResponseEntity<List<Termino>>(getTerminos(servicioTermino.findAlll()), HttpStatus.OK);

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Elimina un t??rmino
	 * 
	 * @param idUsuario id del usuario que desea eliminar un t??rmino
	 * @param token     token ??nico del usuario para identificarlo
	 * @param termino   t??rmino con el t??tulo a eliminar
	 * @return lista con todos los t??rminos
	 */
	@DeleteMapping(path = "/termino/eliminar", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<Termino>> eliminarTermino(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestParam(name = "termino") String titulo, @RequestHeader(name = CABECERA_TOKEN) String token) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioUsuario.tieneSesionActiva(usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_ELIMINAR_TERMINOS))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (servicioTermino.eliminarTermino(titulo))
			return new ResponseEntity<List<Termino>>(getTerminos(servicioTermino.findAlll()), HttpStatus.OK);

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	// -------------------- CODIGOS --------------------

	/**
	 * Obtiene la lista de todos los c??digos de la aplicaci??n
	 * 
	 * @param idUsuario id del usuario que desea ver los c??digos
	 * @param token     token ??nico del usuario para identificarlo
	 * @return Lusta con todos los c??digos
	 */
	@GetMapping(path = "/codigos", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<Codigo>> getCodigos(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioUsuario.tieneSesionActiva(usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_VER_CODIGOS))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		return new ResponseEntity<List<Codigo>>(servicioCodigo.findAll(), HttpStatus.OK);
	}

	// -------------------- METODOS PRIVADOS --------------------

	/**
	 * Obtiene las tareas de configuraci??n de la habitaci??n de muestra
	 * 
	 * @return tareas de configuraci??n de la habitaci??n extra
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
	 * Obtiene todas las habitaciones de configuraci??n
	 * 
	 * @return todas las habitaciones de configuraci??n excepto la habitaci??n de
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
			Usuario usuarioHistorial = null;
			if (usuarioBBDDHistorial != null)
				usuarioHistorial = new Usuario(usuarioBBDDHistorial);

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

	/**
	 * Convierte una lista de {@link TerminoBBDD} en una lista de {@link Termino}
	 * 
	 * @param terminosBBDD t??rminos a convertir
	 * @return lista de {@link Termino} creada a partir de los t??rminos indicados
	 */
	private List<Termino> getTerminos(List<TerminoBBDD> terminosBBDD) {
		List<Termino> terminos = new ArrayList<>();
		for (TerminoBBDD terminoBBDD : terminosBBDD) {
			terminos.add(new Termino(terminoBBDD));
		}
		return terminos;
	}
}
