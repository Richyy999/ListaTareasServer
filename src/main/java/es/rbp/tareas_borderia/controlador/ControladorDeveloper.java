package es.rbp.tareas_borderia.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.rbp.tareas_borderia.entidad.Usuario;
import es.rbp.tareas_borderia.entidad.bbdd.DeudaConfigBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.UsuarioBBDD;
import es.rbp.tareas_borderia.entidad.config.DeudaConfig;
import es.rbp.tareas_borderia.service.Acciones;
import es.rbp.tareas_borderia.service.ServicioDeudaConfig;
import es.rbp.tareas_borderia.service.ServicioUsuario;

import static es.rbp.tareas_borderia.controlador.ConstantesControlador.*;
import static es.rbp.tareas_borderia.service.Acciones.ACCION_CREAR_DEUDA_CONFIG;
import static es.rbp.tareas_borderia.service.Acciones.ACCION_MODIFICAR_DEUDA_CONFIG;
import static es.rbp.tareas_borderia.service.Acciones.ACCION_VER_DEUDA_CONFIG;

@RestController
@RequestMapping("/develop")
public class ControladorDeveloper {

	@Autowired
	private ServicioUsuario servicioUsuario;

	@Autowired
	private ServicioDeudaConfig servicioDeudaConfig;

	/**
	 * Obtiene la última deuda de configuración
	 * 
	 * @param idUsuario id del usuario que desea obtener la deuda de configuración
	 * @param token     token único del usuario para identificarlo
	 * @return última deuda de configuración
	 */
	@GetMapping(path = "/deuda", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<DeudaConfig> getDeudaConfig(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_VER_DEUDA_CONFIG, idUsuario))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		DeudaConfigBBDD deudaConfigBBDD = servicioDeudaConfig.getUltimaDeudaConfig();

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
	@PostMapping(path = "/deuda/anadir", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<DeudaConfig> crearDeuda(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody DeudaConfig deudaConfig) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_CREAR_DEUDA_CONFIG, idUsuario))
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
	@PutMapping(path = "/deuda/modificar", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<DeudaConfig> modificarDeudaConfig(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody DeudaConfig deudaConfig) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_MODIFICAR_DEUDA_CONFIG, idUsuario))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (servicioDeudaConfig.modificarDeudaConfig(deudaConfig)) {
			DeudaConfigBBDD deudaConfigBBDD = servicioDeudaConfig.getUltimaDeudaConfig();
			return new ResponseEntity<DeudaConfig>(new DeudaConfig(deudaConfigBBDD), HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Cambia la contraseña de un usuario
	 * 
	 * @param idUsuario       id del usuario que realiza la petición
	 * @param token           token único del usuario que realiza la acción
	 * @param usuarioAfectado usuario al que se le va a cambiar la contraseña
	 * @return usuario con la contraseña actualizada
	 */
	@PutMapping(path = "/usuario/cambiar_pwd", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<Usuario> cambiarContrasena(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody UsuarioBBDD usuarioAfectado) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, Acciones.ACCION_CAMBAR_PWD, usuarioAfectado.getId()))
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
}
