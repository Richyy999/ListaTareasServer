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

import es.rbp.tareas_borderia.entidad.IDWrapper;
import es.rbp.tareas_borderia.entidad.bbdd.HabitacionConfigBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.TareaConfigBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.UsuarioBBDD;
import es.rbp.tareas_borderia.entidad.config.HabitacionConfig;
import es.rbp.tareas_borderia.entidad.config.TareaConfig;
import es.rbp.tareas_borderia.service.ServicioHabitacionConfig;
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

@RestController
@RequestMapping("/admin")
public class ControladorAdmin {

	@Autowired
	private ServicioUsuario servicioUsuario;

	@Autowired
	private ServicioTareaConfig servicioTareaConfig;

	@Autowired
	private ServicioHabitacionConfig servicioHabitacionConfig;

	@GetMapping(path = "/tareas", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<TareaConfig>> getTareasConfig(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_VER_TAREAS_CONFIG, idUsuario))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		List<TareaConfig> tareasConfig = getTareasConfig();
		return new ResponseEntity<List<TareaConfig>>(tareasConfig, HttpStatus.OK);
	}

	@PostMapping(path = "/anadir/tarea", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<TareaConfig>> anadirTarea(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody TareaConfig tareaConfig) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_CREAR_TAREA_CONFIG, idUsuario))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (servicioTareaConfig.crearTareaConfig(tareaConfig, usuarioBBDD))
			return new ResponseEntity<List<TareaConfig>>(getTareasConfig(), HttpStatus.OK);

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PutMapping(path = "/modificar/tarea", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<TareaConfig>> modificarTareas(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody TareaConfig tareaConfig) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_MODIFICAR_TAREA_CONFIG, idUsuario))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (servicioTareaConfig.modificarTareaLimpiezaConfig(tareaConfig, usuarioBBDD))
			return new ResponseEntity<List<TareaConfig>>(getTareasConfig(), HttpStatus.OK);

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping(path = "/eliminar/tarea", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
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

	@GetMapping(path = "/habitaciones", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<HabitacionConfig>> getHabitacionesConfig(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_VER_HABITACIONES_CONFIG, idUsuario))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		List<HabitacionConfig> habitacionesConfig = getHabitacionesConfig();
		return new ResponseEntity<List<HabitacionConfig>>(habitacionesConfig, HttpStatus.OK);
	}

	@PostMapping(path = "/anadir/habitacion", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
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

	@PutMapping(path = "/modificar/habitacion", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
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

	@DeleteMapping(name = "/eliminar/habitacion", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
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
			servicioHabitacionConfig.eliminarHabitacion(id);
		}

		List<HabitacionConfig> habitacionesConfig = getHabitacionesConfig();
		return new ResponseEntity<List<HabitacionConfig>>(habitacionesConfig, HttpStatus.OK);
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
}
