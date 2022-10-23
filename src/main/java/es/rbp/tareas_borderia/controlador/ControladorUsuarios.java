package es.rbp.tareas_borderia.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.rbp.tareas_borderia.entidad.Usuario;
import es.rbp.tareas_borderia.entidad.bbdd.UsuarioBBDD;
import es.rbp.tareas_borderia.service.ServicioDeuda;
import es.rbp.tareas_borderia.service.ServicioUsuario;

import static es.rbp.tareas_borderia.controlador.ConstantesControlador.*;

import static es.rbp.tareas_borderia.service.Acciones.Usuario.ACCION_ACEPTAR_TERMINOS;

@RestController
@RequestMapping("/usuario")
public class ControladorUsuarios {

	@Autowired
	private ServicioUsuario servicioUsuario;

	@Autowired
	private ServicioDeuda servicioDeuda;

	/**
	 * Inicia la sesión del usuario cambiando su token único
	 * 
	 * @param usuario usuario que hace login
	 * @return Usuario logueado
	 */
	@PostMapping(path = "/login", produces = PRODUCES_JSON)
	public ResponseEntity<Usuario> login(@RequestBody UsuarioBBDD usuario) {
		String nombre = usuario.getNombre();
		String contrasena = usuario.getContrasena();

		UsuarioBBDD usuarioLogueado = servicioUsuario.login(nombre, contrasena);
		if (usuarioLogueado == null)
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		return new ResponseEntity<Usuario>(new Usuario(usuarioLogueado), HttpStatus.OK);
	}

	/**
	 * Crea un usuario nuevo a partir de su nombre y contraseña
	 * 
	 * @param usuarioBBDD usuario nuevo
	 * @return usuario creado, {@link HttpStatus#CONFLICT} si el nombre del usuario
	 *         ya existe
	 */
	@PostMapping(path = "/registro", produces = PRODUCES_JSON)
	public ResponseEntity<Usuario> registro(@RequestBody UsuarioBBDD usuarioBBDD) {
		String nombre = usuarioBBDD.getNombre();
		String contrasena = usuarioBBDD.getContrasena();

		UsuarioBBDD usuarioNuevo = servicioUsuario.crearUsuario(nombre, contrasena);
		if (usuarioNuevo == null)
			return new ResponseEntity<>(HttpStatus.CONFLICT);

		servicioDeuda.crearDeuda(usuarioNuevo.getId());

		return new ResponseEntity<Usuario>(new Usuario(usuarioNuevo), HttpStatus.OK);
	}

	/**
	 * Cierra la sesión de un usuario
	 * 
	 * @param idUsuario id del usuario que desea cerrar sesión
	 * @param token     token único del usuario para identificarlo
	 * @return Código HTTP con el resultado del proceso
	 */
	@PostMapping(path = "/logout", headers = CABECERA_TOKEN)
	public ResponseEntity<Void> logout(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		servicioUsuario.logout(usuarioBBDD.getId());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Acepta los términos y condiciones del usuario
	 * 
	 * @param idUsuario id del usuario que realiza la petición
	 * @param token     token único del usuario que realiza la acción
	 * @param usuario   usuario que va a aceptar los términos y condiciones
	 * @return Usuario que ha aceptado los términos y condiciones
	 */
	@PostMapping(path = "/aceptar-terminos", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<Usuario> aceptarTerminos(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody Usuario usuario) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (usuarioBBDD == null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		if (!servicioUsuario.tieneSesionActiva(usuarioBBDD))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_ACEPTAR_TERMINOS))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		UsuarioBBDD usuarioActualizado = servicioUsuario.aceptarTerminos(usuarioBBDD);
		return new ResponseEntity<>(new Usuario(usuarioActualizado), HttpStatus.OK);
	}
}
