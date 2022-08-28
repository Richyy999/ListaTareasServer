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

import static es.rbp.tareas_borderia.service.Acciones.ACCION_ACEPTAR_TERMINOS;

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
	 * @return usuario creado, {@link HttpStatus#UNAUTHORIZED} si el nombre del
	 *         usuario ya existe
	 */
	@PostMapping(path = "/registro", produces = PRODUCES_JSON)
	public ResponseEntity<Usuario> registro(@RequestBody UsuarioBBDD usuarioBBDD) {
		String nombre = usuarioBBDD.getNombre();
		String contrasena = usuarioBBDD.getContrasena();

		UsuarioBBDD usuarioNuevo = servicioUsuario.crearUsuario(nombre, contrasena);
		if (usuarioNuevo == null)
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		servicioDeuda.crearDeuda(usuarioNuevo.getId());

		return new ResponseEntity<Usuario>(new Usuario(usuarioNuevo), HttpStatus.OK);
	}

	@PostMapping(path = "/logout", headers = CABECERA_TOKEN)
	public ResponseEntity<UsuarioBBDD> logout(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody Usuario usuario) {

		return null;
	}

	/**
	 * Acepta los términos y condiciones del usuario
	 * 
	 * @param idUsuario id del usuario que realiza la petición
	 * @param token     token único del usuario que realiza la acción
	 * @param usuario   usuario que va a aceptar los términos y condiciones
	 * @return Usuario que ha aceptado los términos y condiciones
	 */
	@PostMapping(path = "/aceptar_terminos", headers = CABECERA_TOKEN, produces = PRODUCES_JSON)
	public ResponseEntity<Usuario> aceptarTerminos(@RequestParam(name = ID_USUARIO) Long idUsuario,
			@RequestHeader(name = CABECERA_TOKEN) String token, @RequestBody Usuario usuario) {
		UsuarioBBDD usuarioBBDD = servicioUsuario.findByIdAndToken(idUsuario, token);
		if (!servicioUsuario.estaAutorizado(usuarioBBDD, ACCION_ACEPTAR_TERMINOS, usuario.getId()))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		UsuarioBBDD usuarioActualizado = servicioUsuario.aceptarTerminos(usuarioBBDD);
		return new ResponseEntity<>(new Usuario(usuarioActualizado), HttpStatus.OK);
	}
}
