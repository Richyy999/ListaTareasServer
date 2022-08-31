package es.rbp.tareas_borderia.controlador;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.rbp.tareas_borderia.entidad.Deuda;
import es.rbp.tareas_borderia.entidad.Habitacion;
import es.rbp.tareas_borderia.entidad.Historial;
import es.rbp.tareas_borderia.entidad.IDWrapper;
import es.rbp.tareas_borderia.entidad.Mes;
import es.rbp.tareas_borderia.entidad.Tarea;
import es.rbp.tareas_borderia.entidad.TareaEspecial;
import es.rbp.tareas_borderia.entidad.Usuario;
import es.rbp.tareas_borderia.entidad.bbdd.Codigo;
import es.rbp.tareas_borderia.entidad.bbdd.DeudaBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.HabitacionBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.HabitacionConfigBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.HistorialBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.MesBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.TareaBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.TareaConfigBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.UsuarioBBDD;
import es.rbp.tareas_borderia.entidad.config.HabitacionConfig;
import es.rbp.tareas_borderia.entidad.config.TareaConfig;
import es.rbp.tareas_borderia.service.ServicioCodigo;
import es.rbp.tareas_borderia.service.ServicioDeuda;
import es.rbp.tareas_borderia.service.ServicioHabitacion;
import es.rbp.tareas_borderia.service.ServicioHabitacionConfig;
import es.rbp.tareas_borderia.service.ServicioHistorial;
import es.rbp.tareas_borderia.service.ServicioMes;
import es.rbp.tareas_borderia.service.ServicioTarea;
import es.rbp.tareas_borderia.service.ServicioTareaConfig;
import es.rbp.tareas_borderia.service.ServicioUsuario;

import static es.rbp.tareas_borderia.controlador.ConstantesControlador.*;

import static es.rbp.tareas_borderia.service.Acciones.ACCION_OBTENER_MESES;
import static es.rbp.tareas_borderia.service.Acciones.ACCION_ANADIR_TAREA;
import static es.rbp.tareas_borderia.service.Acciones.ACCION_COBRAR;
import static es.rbp.tareas_borderia.service.Acciones.ACCION_VER_HABITACIONES;
import static es.rbp.tareas_borderia.service.Acciones.ACCION_ELIMINAR_HABITACION;
import static es.rbp.tareas_borderia.service.Acciones.ACCION_VER_HISTORIAL;
import static es.rbp.tareas_borderia.service.Acciones.ACCION_VER_DEUDA;
import static es.rbp.tareas_borderia.service.Acciones.ACCION_AUMENTAR_DEUDA;

@RestController
@RequestMapping("/tareas")
public class ControladorTareas {

	@Autowired
	private ServicioUsuario servicioUsuario;

	@Autowired
	private ServicioMes servicioMes;

	@Autowired
	private ServicioHabitacion servicioHabitacion;

	@Autowired
	private ServicioTarea servicioTarea;

	@Autowired
	private ServicioHabitacionConfig servicioHabitacionConfig;

	@Autowired
	private ServicioTareaConfig servicioTareaConfig;

	@Autowired
	private ServicioDeuda servicioDeuda;

	@Autowired
	private ServicioHistorial servicioHistorial;

	@Autowired
	private ServicioCodigo servicioCodigo;

	// -------------------- TAREAS --------------------

	/**
	 * Obtiene todos los meses del usuario
	 * 
	 * @param idUsuario usuario que desea obtener los meses
	 * @param token     token único del usuario para identificarlo
	 * @return lista con todos los meses del usuario
	 */
	@GetMapping(path = "", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<Mes>> getMeses(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_OBTENER_MESES))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		List<Mes> meses = getMeses(idUsuario);

		return new ResponseEntity<List<Mes>>(meses, HttpStatus.OK);
	}

	/**
	 * Añade una tarea nueva, reduciendo la deuda si la tuviese
	 * 
	 * @param idUsuario        id del usuario al que se le añade la tarea
	 * @param idHabitacion     id de la habitación del historial limpiada
	 * @param token            token único del usuario para identificarlo
	 * @param habitacionAnadir habitación a añadir
	 * @return lista con los meses actualizada
	 */
	@PostMapping(path = "/anadir", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<Mes>> anadirTarea(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestParam(name = "habitacion") Long idHabitacion, @RequestHeader(name = CABECERA_TOKEN) String token,
			@RequestBody(required = true) Habitacion habitacionAnadir) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_ANADIR_TAREA))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		MesBBDD mesActual = servicioMes.getMes(idUsuario);

		HabitacionBBDD habitacionBBDD = servicioHabitacion.anadirHabitacion(habitacionAnadir.getNombre(),
				mesActual.getId());
		if (habitacionBBDD == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		servicioDeuda.reducirDeuda(idUsuario, habitacionAnadir);

		for (Tarea tarea : habitacionAnadir.getTareas()) {
			TareaBBDD tareaLimpiezaBBDD = servicioTarea.anadirTarea(habitacionBBDD.getId(), tarea);
			if (tareaLimpiezaBBDD == null)
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		servicioHistorial.actualizarHistorial(idHabitacion, idUsuario);

		return new ResponseEntity<List<Mes>>(getMeses(idUsuario), HttpStatus.OK);
	}

	@PostMapping(path = "/especial/anadir", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<Mes>> anadirTareaEspecial(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody TareaEspecial tareaEspecial) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_ANADIR_TAREA))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		Codigo codigo = tareaEspecial.getCodigo();
		if (servicioCodigo.findByTipoAndCodigo(codigo.getTipoCodigo(), codigo.getCodigo()) == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		Habitacion habitacion = tareaEspecial.getHabitacion();

		MesBBDD mesActual = servicioMes.getMes(idUsuario);

		HabitacionBBDD habitacionBBDD = servicioHabitacion.anadirHabitacion(habitacion.getNombre(), mesActual.getId());
		if (habitacionBBDD == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		servicioDeuda.reducirDeuda(idUsuario, habitacion);

		for (Tarea tarea : habitacion.getTareas()) {
			TareaBBDD tareaLimpiezaBBDD = servicioTarea.anadirTarea(habitacionBBDD.getId(), tarea);
			if (tareaLimpiezaBBDD == null)
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<List<Mes>>(getMeses(idUsuario), HttpStatus.OK);
	}

	/**
	 * Elimina una habitación de un usuario
	 * 
	 * @param idUsuario    id del usuario al que pertenece la habitación
	 * @param idHabitacion id de la habitación a eliminar
	 * @param token        token único del usuario para identificarlo
	 * @return lista con los meses actualizada
	 */
	@DeleteMapping(path = "/eliminar", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<Mes>> eliminarHabitacion(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestParam(name = "habitacion") Long idHabitacion, @RequestHeader(name = CABECERA_TOKEN) String token) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_ELIMINAR_HABITACION))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (servicioHabitacion.eliminarHabitacion(idHabitacion))
			return new ResponseEntity<List<Mes>>(getMeses(idUsuario), HttpStatus.OK);

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	// -------------------- HABITACIONES DE MUESTRA ----------

	/**
	 * Obtiene las habitaciones con toda la información de sus tareas
	 * 
	 * @param idUsuario id del usuario que desea obtener las habitaciones
	 * @param token     token único del usuario para identificarlo
	 * @return lista con todas las habitaciones con su información
	 */
	@GetMapping(path = "/muestra", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<Habitacion>> getHabitacionesMuestra(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_VER_HABITACIONES))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

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

		List<Habitacion> habitaciones = new ArrayList<>();

		for (HabitacionConfig habitacionConfig : habitacionesConfig) {
			Habitacion habitacion = new Habitacion(habitacionConfig);
			habitaciones.add(habitacion);
		}

		return new ResponseEntity<List<Habitacion>>(habitaciones, HttpStatus.OK);
	}

	// -------------------- HISTORIAL --------------------

	/**
	 * Obtiene todos los historiales
	 * 
	 * @param idUsuario id del usuario que desea obtener los historiales
	 * @param token     token único del usuario para identificarlo
	 * @return Lista con los historiales
	 */
	@GetMapping(path = "/historial", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<Historial>> getHistorial(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestParam(name = "idHabitacion", required = false) Long idHabitacion,
			@RequestHeader(name = CABECERA_TOKEN) String token) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_VER_HISTORIAL))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (idHabitacion == null)
			return new ResponseEntity<List<Historial>>(getHistorial(servicioHistorial.findAll()), HttpStatus.OK);

		return new ResponseEntity<List<Historial>>(
				getHistorial(servicioHistorial.findByIdHabitacionConfig(idHabitacion)), HttpStatus.OK);
	}

	// -------------------- COBRAR --------------------

	/**
	 * Cobra las tareas de un usuario
	 * 
	 * @param idUsuario id del usuairo que desea cobrar
	 * @param token     token único del usuario para identificarlo
	 * @param ids       ids de las habitaciones que desea cobrar
	 * @return lista con los meses actualizada
	 */
	@PostMapping(path = "/cobrar", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<Mes>> cobrar(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody IDWrapper ids) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_COBRAR))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		List<Long> idHabitaciones = ids.getIds();
		for (long idHabitacion : idHabitaciones) {
			List<TareaBBDD> tarreasBBDD = servicioTarea.getTareas(idHabitacion);
			for (TareaBBDD tareaBBDD : tarreasBBDD) {
				servicioTarea.cobrarTarea(tareaBBDD.getId());
			}
		}

		return new ResponseEntity<List<Mes>>(getMeses(idUsuario), HttpStatus.OK);
	}

	/**
	 * Cobra una cantidad de dinero concreta, modificando el precio de las tareas si
	 * es necesario
	 * 
	 * @param idUsuario id del usuario que desea cobrar las tareas
	 * @param cantidad  cantidad de dinero que se desea cobrar
	 * @param token     token único del usuario para identificarlo
	 * @return Lista con los meses del usuario actualizados
	 */
	@PostMapping(path = "/cobrar/cantidad", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<Mes>> cobrarCantidad(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestParam(name = "cantidad") Double cantidad, @RequestHeader(name = CABECERA_TOKEN) String token) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_COBRAR))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (servicioTarea.cobrarCantidad(cantidad, idUsuario))
			return new ResponseEntity<List<Mes>>(getMeses(idUsuario), HttpStatus.OK);

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	// -------------------- DEUDA --------------------

	/**
	 * Obtiene la deuda de un usuario
	 * 
	 * @param idUsuario  id del usuario que desea ver la deuda
	 * @param idAfectado id del usuario al que pertenece la deuda
	 * @param token      token único del usuario para identificarlo
	 * @return deuda del usuario indicado
	 */
	@GetMapping(path = "/deuda", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<Deuda> getDeuda(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestParam(name = "usuario") Long idAfectado, @RequestHeader(name = CABECERA_TOKEN) String token) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_VER_DEUDA, idAfectado))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		DeudaBBDD deudaBBDD = servicioDeuda.findByIdUsuario(idAfectado);
		Deuda deuda = new Deuda(deudaBBDD);

		return new ResponseEntity<Deuda>(deuda, HttpStatus.OK);
	}

	/**
	 * Añade una deuda al usuario si se puede
	 * 
	 * @param idUsuarioid del usuario al que se le va a añadir la deuda
	 * @param token       token único del usuario para identificarlo
	 * @param deuda       Deuda con los datos
	 * @return lista de meses actualizada
	 */
	@PostMapping(path = "/deuda/anadir", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<Mes>> anadirDeuda(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody Deuda deuda) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_AUMENTAR_DEUDA))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		double cantidadSinCobrar = servicioTarea.getCantidadSinCobrar(usuarioBBDD.getId());
		double cantidadDeuda = deuda.getDeuda();

		if (cantidadSinCobrar > 0) {
			if (cantidadSinCobrar >= cantidadDeuda) {
				servicioTarea.cobrarCantidad(cantidadDeuda, usuarioBBDD.getId());
				cantidadDeuda = 0;
			} else {
				servicioTarea.cobrarCantidad(cantidadSinCobrar, usuarioBBDD.getId());
				cantidadDeuda -= cantidadSinCobrar;
			}
		}

		DeudaBBDD deudaBBDD = servicioDeuda.anadirDeuda(usuarioBBDD.getId(), cantidadDeuda);
		if (deudaBBDD == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		return new ResponseEntity<List<Mes>>(getMeses(usuarioBBDD.getId()), HttpStatus.OK);
	}

	// -------------------- METODOS PRIVADOS --------------------

	/**
	 * Obtiene todos los meses del usuario
	 * 
	 * @param idUsuario id del usuario que posee los meses
	 * @return List con todos los meses del usuario
	 */
	private List<Mes> getMeses(long idUsuario) {
		List<MesBBDD> mesesBBDD = servicioMes.getMeses(idUsuario);
		List<Mes> meses = new ArrayList<>();

		for (MesBBDD mesBBDD : mesesBBDD) {
			List<HabitacionBBDD> habitacionesBBDD = servicioHabitacion.getHabitaciones(mesBBDD.getId());
			List<Habitacion> habitaciones = new ArrayList<>();

			for (HabitacionBBDD habitacionBBDD : habitacionesBBDD) {
				List<TareaBBDD> tareasBBDD = servicioTarea.getTareas(habitacionBBDD.getId());
				List<Tarea> tareas = new ArrayList<>();

				for (TareaBBDD tareaBBDD : tareasBBDD) {
					Tarea tarea = new Tarea(tareaBBDD);
					tareas.add(tarea);
				}
				Habitacion habitacion = new Habitacion(habitacionBBDD, tareas);
				habitaciones.add(habitacion);
			}
			Mes mes = new Mes(mesBBDD, habitaciones);
			meses.add(mes);
		}
		return meses;
	}

	/**
	 * Obtiene una lista de {@link Historial} a partir de una lista de
	 * {@link HistorialBBDD}
	 * 
	 * @param historialesBBDD lista de historiales a transformar
	 * @return historial de limpieza
	 */
	private List<Historial> getHistorial(List<HistorialBBDD> historialesBBDD) {
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
