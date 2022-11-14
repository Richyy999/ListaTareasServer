package es.rbp.tareas_borderia.controlador;

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

import es.rbp.tareas_borderia.entidad.Deuda;
import es.rbp.tareas_borderia.entidad.Usuario;
import es.rbp.tareas_borderia.entidad.bbdd.Codigo;
import es.rbp.tareas_borderia.entidad.bbdd.DeudaConfigBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.UsuarioBBDD;
import es.rbp.tareas_borderia.entidad.config.DeudaConfig;
import es.rbp.tareas_borderia.service.ServicioCodigo;
import es.rbp.tareas_borderia.service.ServicioDeuda;
import es.rbp.tareas_borderia.service.ServicioDeudaConfig;
import es.rbp.tareas_borderia.service.ServicioUsuario;

import static es.rbp.tareas_borderia.controlador.ConstantesControlador.*;
import static es.rbp.tareas_borderia.service.Acciones.Developer.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/developer")
public class ControladorDeveloper {

	@Autowired
	private ServicioUsuario servicioUsuario;

	@Autowired
	private ServicioDeudaConfig servicioDeudaConfig;

	@Autowired
	private ServicioDeuda servicioDeuda;

	@Autowired
	private ServicioCodigo servicioCodigo;

	// -------------------- DEUDA CONFIG --------------------

	/**
	 * Obtiene la última deuda de configuración
	 * 
	 * @param idUsuario id del usuario que desea obtener la deuda de configuración
	 * @param token     token único del usuario para identificarlo
	 * @return última deuda de configuración
	 */
	@GetMapping(path = "/deuda-config", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<DeudaConfig> getDeudaConfig(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioUsuario.tieneSesionActiva(usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_VER_DEUDA_CONFIG))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		DeudaConfigBBDD deudaConfigBBDD = servicioDeudaConfig.getUltimaDeudaConfig();
		if (deudaConfigBBDD == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		return new ResponseEntity<DeudaConfig>(new DeudaConfig(deudaConfigBBDD), HttpStatus.OK);
	}

	/**
	 * Crea una deuda de configuración, si ya existe alguna, la modifica. Si ya
	 * existe una, usar el método
	 * {@link ControladorDeveloper#modificarDeudaConfig(Long, String, DeudaConfig)
	 * 
	 * @see ControladorDeveloper#modificarDeudaConfig(Long, String, DeudaConfig)
	 * @param idUsuario   id del usuario que desea crear una deuda de configuración
	 * @param token       token único del usuario para identificarlo
	 * @param deudaConfig deuda de configuración con los datos
	 * @return última deuda de configuración
	 */
	@PostMapping(path = "/deuda-config/anadir", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<DeudaConfig> crearDeudaConfig(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody DeudaConfig deudaConfig) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioUsuario.tieneSesionActiva(usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_CREAR_DEUDA_CONFIG))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (servicioDeudaConfig.crearDeudaConfig(deudaConfig, usuarioBBDD)) {
			DeudaConfigBBDD deudaConfigBBDD = servicioDeudaConfig.getUltimaDeudaConfig();
			return new ResponseEntity<DeudaConfig>(new DeudaConfig(deudaConfigBBDD), HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Modifica la deuda de configuración
	 * 
	 * @param idUsuario   id del usuario que desea modificar la deuda de
	 *                    configuración
	 * @param token       token único del usuario para identificarlo
	 * @param deudaConfig deuda de configuración con los datos actualizados
	 * @return última deuda de configuración
	 */
	@PutMapping(path = "/deuda-config/modificar", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<DeudaConfig> modificarDeudaConfig(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody DeudaConfig deudaConfig) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioUsuario.tieneSesionActiva(usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_MODIFICAR_DEUDA_CONFIG))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (servicioDeudaConfig.modificarDeudaConfig(deudaConfig, usuarioBBDD)) {
			DeudaConfigBBDD deudaConfigBBDD = servicioDeudaConfig.getUltimaDeudaConfig();
			return new ResponseEntity<DeudaConfig>(new DeudaConfig(deudaConfigBBDD), HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	// -------------------- CODIGOS --------------------

	/**
	 * Crea un código nuevo
	 * 
	 * @param idUsuario id del usuario que desea crear el código
	 * @param token     token único del usuario para identificarlo
	 * @param codigo    código a crear
	 * @return lista de todos los códigos
	 */
	@PostMapping(path = "/codigo/anadir", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<Codigo>> crearCodigo(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody Codigo codigo) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioUsuario.tieneSesionActiva(usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_CREAR_CODIGOS))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (servicioCodigo.crearCodigo(codigo))
			return new ResponseEntity<List<Codigo>>(servicioCodigo.findAll(), HttpStatus.OK);

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Modifica un código
	 * 
	 * @param idUsuario usuario que desea modificar un código
	 * @param token     token único del usuario para identificarlo
	 * @param codigo    código con los datos actualizados
	 * @return lista con todos los códigos
	 */
	@PutMapping(path = "/codigo/modificar", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<Codigo>> modificarCodigo(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody Codigo codigo) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioUsuario.tieneSesionActiva(usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_MODIFICAR_CODIGOS))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (servicioCodigo.modificarCodigo(codigo))
			return new ResponseEntity<List<Codigo>>(servicioCodigo.findAll(), HttpStatus.OK);

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Elimina un código
	 * 
	 * @param idUsuario usuario que desea eliminar el código
	 * @param token     token único del usuario para identificarlo
	 * @param codigo    código con el ID del código a eliminar
	 * @return lista con todos los códigos
	 */
	@DeleteMapping(path = "/codigo/eliminar", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<Codigo>> eliminarCodigo(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestParam(name = "codigo") Long idCodigo, @RequestHeader(name = CABECERA_TOKEN) String token) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioUsuario.tieneSesionActiva(usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_ELIMINAR_CODIGOS))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (servicioCodigo.eliminarCodigo(idCodigo))
			return new ResponseEntity<List<Codigo>>(servicioCodigo.findAll(), HttpStatus.OK);

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	// -------------------- USUARIO --------------------

	/**
	 * Busca todos los usuarios
	 * 
	 * @param idUsuario id del usuario que desea obtener los usuarios
	 * @param token     token único del usuario para identificarlo
	 * @return todos los usuarios
	 */
	@GetMapping(path = "/usuarios", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<Usuario>> getUsuarios(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioUsuario.tieneSesionActiva(usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_VER_USUARIOS))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		return new ResponseEntity<List<Usuario>>(getUsuarios(), HttpStatus.OK);
	}

	/**
	 * Cambia la contraseña de un usuario
	 * 
	 * @param idUsuario       id del usuario que realiza la petición
	 * @param token           token único del usuario que realiza la acción
	 * @param usuarioAfectado usuario al que se le va a cambiar la contraseña
	 * @return Si el usuario que realiza la petición es el usuario afectado devuelve
	 *         una lista con el usuario con la contraseña actualizada.
	 *         <p/>
	 *         Si el usuario que realiza la petición no es el usuario afectado
	 *         devuelve una lista con todos los usuarios
	 */
	@PutMapping(path = "/usuario/cambiar-pwd", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<Usuario> cambiarContrasena(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody UsuarioBBDD usuarioAfectado) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioUsuario.tieneSesionActiva(usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_CAMBAR_PWD, usuarioAfectado.getId()))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		String contrasena = usuarioAfectado.getContrasena();
		if (contrasena == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		UsuarioBBDD usuarioContrasenaNueva = servicioUsuario.cambiarContrasena(usuarioBBDD, usuarioAfectado.getId(),
				usuarioAfectado.getContrasena());
		if (usuarioContrasenaNueva == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		return new ResponseEntity<Usuario>(new Usuario(usuarioContrasenaNueva), HttpStatus.OK);
	}

	/**
	 * Modifica la deuda de un usuario
	 * 
	 * @param idUsuario  id del usuario que desea modificar la deuda
	 * @param idAfectado id del usuario al que pertenece la deuda
	 * @param token      token único del usuario para identificarlo
	 * @param deuda      deuda con los datos actualizados
	 * @return Lista con todos los usuarios
	 */
	@PutMapping(path = "/deuda/modificar", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<Usuario> modificarDeuda(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestParam(name = "usuario") Long idAfectado, @RequestHeader(name = CABECERA_TOKEN) String token,
			@RequestBody Deuda deuda) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioUsuario.tieneSesionActiva(usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_MODIFICAR_DEUDA, idAfectado))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioDeuda.modificarDeuda(idAfectado, deuda))
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		UsuarioBBDD usuarioDeuda = servicioUsuario.findById(idAfectado);

		return new ResponseEntity<>(new Usuario(usuarioDeuda), HttpStatus.OK);
	}

	@PutMapping(path = "/usuario/modificar", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<List<Usuario>> modificarUsuario(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody Usuario usuario) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioUsuario.tieneSesionActiva(usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_MODIFICAR_USUARIOS))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (servicioUsuario.modificarUsuario(usuario))
			return new ResponseEntity<List<Usuario>>(getUsuarios(), HttpStatus.OK);

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	// -------------------- METODOS PRIVADOS --------------------

	/**
	 * Crea una lista con todos los usuarios
	 * 
	 * @return lista con todos los usuarios
	 */
	private List<Usuario> getUsuarios() {
		List<UsuarioBBDD> usuariosBBDD = servicioUsuario.findAll();
		List<Usuario> usuarios = new ArrayList<>();

		for (UsuarioBBDD usuarioBBDDLista : usuariosBBDD) {
			usuarios.add(new Usuario(usuarioBBDDLista));
		}

		return usuarios;
	}
}
