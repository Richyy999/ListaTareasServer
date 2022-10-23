package es.rbp.tareas_borderia.service.implement;

import org.joda.time.DurationFieldType;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.rbp.tareas_borderia.entidad.Usuario;
import es.rbp.tareas_borderia.entidad.bbdd.UsuarioBBDD;
import es.rbp.tareas_borderia.repositorio.RepositorioUsuario;
import es.rbp.tareas_borderia.service.ServicioUsuario;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static es.rbp.tareas_borderia.service.Acciones.Developer.*;
import static es.rbp.tareas_borderia.service.Acciones.Admin.*;
import static es.rbp.tareas_borderia.service.Acciones.Usuario.*;

@Service
public class ServicioUsuarioImpl implements ServicioUsuario {

	private static final int DURACION_SESION = 5;

	@Autowired
	private RepositorioUsuario repo;

	@Override
	public boolean estaAutorizado(UsuarioBBDD usuario, int accion, long idAfectado) {
		if (usuario == null)
			return false;

		switch (accion) {
		case ACCION_COBRAR:
			return autorizarCobrar(usuario);

		case ACCION_CAMBAR_PWD:
			return autorizarCambioPwd(usuario, idAfectado);

		case ACCION_OBTENER_MESES:
			return autorizarGetMeses(usuario);

		case ACCION_ANADIR_TAREA:
			return autorizarAnadirTarea(usuario);

		case ACCION_ACEPTAR_TERMINOS:
			return autorizarAceptarTerminos(usuario);

		case ACCION_VER_HABITACIONES:
			return autorizarVerHabitaciones(usuario);

		case ACCION_VER_HISTORIAL:
			return autorizarVerHistorial(usuario);

		case ACCION_VER_DEUDA:
			return autorizarVerDeuda(usuario, idAfectado);

		case ACCION_AUMENTAR_DEUDA:
			return autorizarAumentarDeuda(usuario);

		case ACCION_ELIMINAR_HABITACION:
			return autorizarEliminarHabitacion(usuario);

		case ACCION_VER_TERMINOS:
			return autorizarVerTermino(usuario);

		case ACCION_VER_TAREAS_CONFIG:
			return autorizarVerTareaConfig(usuario);

		case ACCION_CREAR_TAREA_CONFIG:
			return autorizarCrearTareaConfig(usuario);

		case ACCION_MODIFICAR_TAREA_CONFIG:
			return autorizarModificarTareaConfig(usuario);

		case ACCION_ELIMINAR_TAREA_CONFIG:
			return autorizarEliminarTareaConfig(usuario);

		case ACCION_VER_HABITACIONES_CONFIG:
			return autorizarVerHabitacionConfig(usuario);

		case ACCION_CREAR_HABITACION_CONFIG:
			return autorizarCrearHabitacionConfig(usuario);

		case ACCION_MODIFICAR_HABITACION_CONFIG:
			return autorizarModificarHabitacionConfig(usuario);

		case ACCION_ELIMINAR_HABITACION_CONFIG:
			return autorizarEliminarHabitacionConfig(usuario);

		case ACCION_CREAR_HISTORIAL:
			return autorizarCrearHistorial(usuario);

		case ACCION_MODIFICAR_HISTORIAL:
			return autorizarModificarHistorial(usuario);

		case ACCION_ELIMINAR_HISTORIAL:
			return autorizarEliminarHistorial(usuario);

		case ACCION_VER_CODIGOS:
			return autorizarVerCodigos(usuario);

		case ACCION_ANADIR_TERMINOS:
			return autorizarAnadirTermino(usuario);

		case ACCION_MODIFICAR_TERMINOS:
			return autorizarModificarTermino(usuario);

		case ACCION_ELIMINAR_TERMINOS:
			return autorizarEliminarTermino(usuario);

		case ACCION_VER_DEUDA_CONFIG:
			return autorizarVerDeudaConfig(usuario);

		case ACCION_CREAR_DEUDA_CONFIG:
			return autorizarCrearDeudaConfig(usuario);

		case ACCION_MODIFICAR_DEUDA_CONFIG:
			return autorizarModificarDeudaConfig(usuario);

		case ACCION_CREAR_CODIGOS:
			return autorizarCrearCodigo(usuario);

		case ACCION_MODIFICAR_CODIGOS:
			return autorizarModificarCodigos(usuario);

		case ACCION_ELIMINAR_CODIGOS:
			return autorizarEliminarCodigos(usuario);

		case ACCION_VER_USUARIOS:
			return autorizarVerUsuarios(usuario);

		case ACCION_MODIFICAR_USUARIOS:
			return autorizarModificarUsuarios(usuario);

		case ACCION_MODIFICAR_DEUDA:
			return autorizarModificarDeuda(usuario);
		}

		return false;
	}

	@Override
	public boolean estaAutorizado(UsuarioBBDD usuario, int accion) {
		return estaAutorizado(usuario, accion, usuario.getId());
	}

	@Override
	public boolean tieneSesionActiva(UsuarioBBDD usuario) {
		LocalDateTime localUltimaAccion = usuario.getUltimaAccion();
		LocalDateTime localAhora = LocalDateTime.now(ZoneId.systemDefault());

		Date ultimaPeticion = Date.from(localUltimaAccion.atZone(ZoneId.systemDefault()).toInstant());
		Date ahora = Date.from(localAhora.atZone(ZoneId.systemDefault()).toInstant());

		long ultimaPeticionMili = ultimaPeticion.getTime();
		long ahoraMili = ahora.getTime();

		Interval interval = new Interval(ultimaPeticionMili, ahoraMili);
		int diferencia = interval.toPeriod().get(DurationFieldType.minutes());

		if (diferencia >= DURACION_SESION)
			return false;

		usuario.setUltimaAccion(localAhora);
		repo.save(usuario);
		return true;
	}

	@Override
	public UsuarioBBDD cambiarContrasena(UsuarioBBDD usuario, long idAfectado, String contrasena) {
		Optional<UsuarioBBDD> optional = repo.findById(idAfectado);
		if (optional.isEmpty() || contrasena == null)
			return null;

		if (contrasena.length() < 4)
			return null;

		UsuarioBBDD user = optional.get();
		String hash = hashString(contrasena);
		user.setContrasena(hash);

		user.setCambiarPasswd(usuario.getId() != idAfectado);

		return repo.save(user);
	}

	@Override
	public UsuarioBBDD crearUsuario(String nombre, String contrasena) {
		UsuarioBBDD user = repo.findByNombre(nombre);
		if (user != null)
			return null;

		UsuarioBBDD usuario = new UsuarioBBDD();
		List<UsuarioBBDD> usuarios = repo.findAll();
		usuario.setAdmin(usuarios.size() == 0);
		usuario.setDeveloper(usuarios.size() == 0);
		usuario.setCambiarPasswd(false);
		usuario.setHabilitado(true);

		usuario.setBonificacion(0d);

		String token = generarToken();
		usuario.setToken(token);

		usuario.setNombre(nombre);

		if (contrasena.length() < 4)
			return null;

		String hash = hashString(contrasena);
		usuario.setContrasena(hash);

		return repo.save(usuario);
	}

	@Override
	public UsuarioBBDD aceptarTerminos(UsuarioBBDD usuarioBBDD) {
		Optional<UsuarioBBDD> optional = repo.findById(usuarioBBDD.getId());
		if (optional.isEmpty())
			return null;

		UsuarioBBDD usuario = optional.get();
		usuario.setTerminosAceptados(true);
		return repo.save(usuario);
	}

	@Override
	public UsuarioBBDD login(String nombre, String contrasena) {
		if (contrasena == null)
			return null;

		String hash = hashString(contrasena);
		UsuarioBBDD usuario = repo.findByNombreAndContrasena(nombre, hash);

		if (usuario == null || !usuario.isHabilitado())
			return null;

		String token = generarToken();
		usuario.setToken(token);

		return repo.save(usuario);
	}

	@Override
	public void logout(long id) {
		Optional<UsuarioBBDD> optional = repo.findById(id);
		if (optional.isEmpty())
			return;

		UsuarioBBDD usuarioBBDD = optional.get();
		usuarioBBDD.setToken(null);
	}

	@Override
	public UsuarioBBDD findByIdAndToken(long id, String token) {
		return repo.findByIdAndToken(id, token);
	}

	@Override
	public UsuarioBBDD findById(long id) {
		Optional<UsuarioBBDD> optional = repo.findById(id);
		if (optional.isEmpty())
			return null;

		return optional.get();
	}

	@Override
	public List<UsuarioBBDD> findAll() {
		return repo.findAll();
	}

	@Override
	public boolean modificarUsuario(Usuario usuario) {
		Optional<UsuarioBBDD> optional = repo.findById(usuario.getId());
		if (optional.isEmpty())
			return false;

		UsuarioBBDD usuarioBBDD = optional.get();
		if (usuario.isAdmin() != usuarioBBDD.isAdmin())
			usuarioBBDD.setAdmin(usuario.isAdmin());

		if (usuario.isDeveloper() != usuarioBBDD.isDeveloper())
			usuarioBBDD.setDeveloper(usuario.isDeveloper());

		if (usuario.isHabilitado() != usuarioBBDD.isHabilitado())
			usuarioBBDD.setHabilitado(usuario.isHabilitado());

		if (usuario.getBonificacion() != usuarioBBDD.getBonificacion())
			usuarioBBDD.setBonificacion(usuario.getBonificacion());

		return repo.save(usuarioBBDD) != null;
	}

	/**
	 * Obtiene el hash de una cadena de caracteres
	 * 
	 * @param string cadena de caracteres que se desea hashear
	 * @return cadena de caracteres generada por la función hash
	 */
	private String hashString(String string) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.reset();
			digest.update(string.getBytes("utf8"));
			return String.format("%064x", new BigInteger(1, digest.digest()));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Genera un token único de 10 caracteres alfanuméricos
	 * 
	 * @return token único de 10 caracteres alfanuméricos
	 */
	private String generarToken() {
		String token = "";
		do {
			while (token.length() < 10) {
				int tipoCaracter = (int) Math.floor(Math.random() * (3 - +1) + 1);
				if (tipoCaracter == 1) {
					char numero = (char) (int) Math.floor(Math.random() * (57 - 48 + 1) + 48);
					token += numero;
				} else if (tipoCaracter == 2) {
					char minuscula = (char) (int) Math.floor(Math.random() * (122 - 97 + 1) + 97);
					token += minuscula;
				} else if (tipoCaracter == 3) {
					char mayuscula = (char) (int) Math.floor(Math.random() * (90 - 65 + 1) + 65);
					token += mayuscula;
				}
			}
		} while (repo.findByToken(token) != null);

		return token;
	}

	// -------------------- USUARIOS --------------------

	/**
	 * Verifica si el usuario tiene permitido cobrar tareas
	 * 
	 * @param usuario usuario que desea obtener los meses
	 * @return true si tiene autorización, false en caso contrario
	 */
	private boolean autorizarCobrar(UsuarioBBDD usuario) {
		return existe(usuario);
	}

	/**
	 * Verifica si el usuario puede cambiar la contraseña de otro usuario
	 * 
	 * @param usuario    usuario que realiza la acción
	 * @param idAfectado id del usuario del que se desea cambiar la contraseña
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarCambioPwd(UsuarioBBDD usuario, long idAfectado) {
		return esElMismo(usuario, idAfectado) || isDeveloper(usuario);
	}

	/**
	 * Verifica si el usuario tiene permitido obtener los meses
	 * 
	 * @param usuario usuario que desea obtener los meses
	 * @return true si tiene autorización, false en caso contrario
	 */
	private boolean autorizarGetMeses(UsuarioBBDD usuario) {
		return existe(usuario);
	}

	/**
	 * Verifica si el usuario tiene permitido añadir tareas
	 * 
	 * @param usuario que desea anadir una tarea
	 * @return true si tiene autorización, false en caso contrario
	 */
	private boolean autorizarAnadirTarea(UsuarioBBDD usuario) {
		return existe(usuario);
	}

	/**
	 * Verifica si el usuario puede aceptar los términos y condiciones
	 * 
	 * @param usuario isiario que desea aceptar los términos
	 * @return true si tiene autorización, false en caso contrario
	 */
	private boolean autorizarAceptarTerminos(UsuarioBBDD usuario) {
		return existe(usuario);
	}

	/**
	 * Verifica si el usuario puede ver las habitaciones
	 * 
	 * @param usuario usuario que quiere ver las habitaciones
	 * @return true si tiene autorización, false en caso contrario
	 */
	private boolean autorizarVerHabitaciones(UsuarioBBDD usuario) {
		return existe(usuario);
	}

	/**
	 * Verifica si el usuario puede ver el historial
	 * 
	 * @param usuario usuario que quiere ver el historial
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarVerHistorial(UsuarioBBDD usuario) {
		return existe(usuario);
	}

	/**
	 * Verifica si un usuario puede ver una deuda
	 * 
	 * @param usuario    usuario que desea ver una deuda
	 * @param idAfectado id del usuario al que pertenece la deuda
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarVerDeuda(UsuarioBBDD usuario, long idAfectado) {
		Optional<UsuarioBBDD> optional = repo.findById(usuario.getId());
		if (optional.isEmpty())
			return false;

		return usuario.isDeveloper() || usuario.getId() == idAfectado;
	}

	/**
	 * Verifica si el usuario puede aumentar su deuda
	 * 
	 * @param usuario usuario que desea aumentar su deuda
	 * @return
	 */
	private boolean autorizarAumentarDeuda(UsuarioBBDD usuario) {
		return existe(usuario);
	}

	/**
	 * Verifica si el usuario puede eliminar habitaciones
	 * 
	 * @param usuario usuario que desea eliminar una habitación
	 * @return true si tiene autorización, false en caso contrario
	 */
	private boolean autorizarEliminarHabitacion(UsuarioBBDD usuario) {
		return existe(usuario);
	}

	/**
	 * Verifica si un usuario puede ver los términos
	 * 
	 * @param usuario usuario que desea ver los términos
	 * @return true si estáautorizado, false en caso contrario
	 */
	private boolean autorizarVerTermino(UsuarioBBDD usuario) {
		return existe(usuario);
	}

	// -------------------- ADMIN --------------------

	/**
	 * Verifica si un usuario puede ver las tareas de configuración
	 * 
	 * @param usuario usuario que desea ver las tareas de configuración
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarVerTareaConfig(UsuarioBBDD usuario) {
		return isAdmin(usuario);
	}

	/**
	 * Verifica si un usuario puede crear tareas de configuración
	 * 
	 * @param usuario usuario que desea crear la tarea de configuración
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarCrearTareaConfig(UsuarioBBDD usuario) {
		return isAdmin(usuario);
	}

	/**
	 * Verifica si un usuario puede modificar tareas de configuración
	 * 
	 * @param usuario usuario que desea modificar la tarea de configuración
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarModificarTareaConfig(UsuarioBBDD usuario) {
		return isAdmin(usuario);
	}

	/**
	 * Verifica si un usuario puede eliminar tareas de configuración
	 * 
	 * @param usuario usuario que desea eliminar la tarea de configuración
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarEliminarTareaConfig(UsuarioBBDD usuario) {
		return isAdmin(usuario);
	}

	/**
	 * Verifica si un usuario puede ver las habitaciones de configuración
	 * 
	 * @param usuario usuario que desea ver las habitaciones de configuración
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarVerHabitacionConfig(UsuarioBBDD usuario) {
		return isAdmin(usuario);
	}

	/**
	 * Verifica si un usuario puede crear habitaciones de configuración
	 * 
	 * @param usuario usuario que desea crear la habitación de configuración
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarCrearHabitacionConfig(UsuarioBBDD usuario) {
		return isAdmin(usuario);
	}

	/**
	 * Verifica si un usuario puede modificar habitaciones de configuración
	 * 
	 * @param usuario usuario que desea modificar la habitación de configuración
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarModificarHabitacionConfig(UsuarioBBDD usuario) {
		return isAdmin(usuario);
	}

	/**
	 * Verifica si un usuario puede eliminar habitaciones de configuración
	 * 
	 * @param usuario usuario que desea eliminar la habitación de configuración
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarEliminarHabitacionConfig(UsuarioBBDD usuario) {
		return isAdmin(usuario);
	}

	/**
	 * verifica si un usuario puede crear un historial
	 * 
	 * @param usuario usuario que desea crear el historial
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarCrearHistorial(UsuarioBBDD usuario) {
		return isAdmin(usuario);
	}

	/**
	 * Verifica si un usuario puede modificar un historial
	 * 
	 * @param usuario usuario que desea modificar el historial
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarModificarHistorial(UsuarioBBDD usuario) {
		return isAdmin(usuario);
	}

	/**
	 * Verifica si un usuario puede eliminar un historial
	 * 
	 * @param usuario usuario que desea eliminar el historial
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarEliminarHistorial(UsuarioBBDD usuario) {
		return isAdmin(usuario);
	}

	/**
	 * Verifica si un usuario puede ver los códigos
	 * 
	 * @param usuario usuario que desea ver los códigos
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarVerCodigos(UsuarioBBDD usuario) {
		return isAdmin(usuario);
	}

	/**
	 * Verifica si un usuario puede crear un término
	 * 
	 * @param usuario usuario que desea crear un término
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarAnadirTermino(UsuarioBBDD usuario) {
		return isAdmin(usuario);
	}

	/**
	 * Verifica si un usuario puede modificar los términos
	 * 
	 * @param usuario usuario que desea modificar los términos
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarModificarTermino(UsuarioBBDD usuario) {
		return isAdmin(usuario);
	}

	/**
	 * Verifica si un usuario puede eliminar términos
	 * 
	 * @param usuario usuario que desea eliminar un término
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarEliminarTermino(UsuarioBBDD usuario) {
		return isAdmin(usuario);
	}

	// -------------------- DEVELOPER --------------------

	/**
	 * Verifica si un usuario puede ver la deuda de configuración
	 * 
	 * @param usuario usuario que desea ver la deuda de configuración
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarVerDeudaConfig(UsuarioBBDD usuario) {
		return isDeveloper(usuario);
	}

	/**
	 * Verifica si un usuario puede crear una duda de configuración
	 * 
	 * @param usuario usuario que desea crear una deuda de configuración
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarCrearDeudaConfig(UsuarioBBDD usuario) {
		return isDeveloper(usuario);
	}

	/**
	 * verifica si un usuario puede modificar una deuda de configuración
	 * 
	 * @param usuario usuario que desea modificar la deuda de configuración
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarModificarDeudaConfig(UsuarioBBDD usuario) {
		return isDeveloper(usuario);
	}

	/**
	 * Verifica si un usuario puede crear un código
	 * 
	 * @param usuario usuario que desea crear un código
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarCrearCodigo(UsuarioBBDD usuario) {
		return isDeveloper(usuario);
	}

	/**
	 * Verifica si un usuario puede modificar los códigos
	 * 
	 * @param usuario usuario que desea ver los códigos
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarModificarCodigos(UsuarioBBDD usuario) {
		return isDeveloper(usuario);
	}

	/**
	 * Verifica si un usuario puede eliminar los códigos
	 * 
	 * @param usuario usuario que desea eliminar los códigos
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarEliminarCodigos(UsuarioBBDD usuario) {
		return isDeveloper(usuario);
	}

	/**
	 * Verifica si un usuario puede ver los usuarios
	 * 
	 * @param usuario usuario que desea ver los usuarios
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarVerUsuarios(UsuarioBBDD usuario) {
		return isDeveloper(usuario);
	}

	/**
	 * Verifica si un usuario puede modificar los usuarios
	 * 
	 * @param usuario usuario que desea modificar los usuarios
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarModificarUsuarios(UsuarioBBDD usuario) {
		return isDeveloper(usuario);
	}

	/**
	 * Verifica si un usuario puede modificar la deuda de otro usuario
	 * 
	 * @param usuario usuario que desea modificar la deuda de otro usuario
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarModificarDeuda(UsuarioBBDD usuario) {
		return isDeveloper(usuario);
	}

	// -------------------- METODOS PRIVADOS --------------------

	/**
	 * Verifica si el usuario que desea realizar la acción es el mismo al que afecta
	 * dicha acción
	 * 
	 * @param usuario    usuario que desea realizar la acción
	 * @param idAfectado oid del usuario afectado por la acción a realizar
	 * @return true si es el mismo, false en caso contrario
	 */
	private boolean esElMismo(UsuarioBBDD usuario, long idAfectado) {
		long id = usuario.getId();

		return id == idAfectado;
	}

	/**
	 * Verfiica si el usuario existe
	 * 
	 * @param usuario usuario que desea realizar la acción
	 * @return true si existe, false en caso contrario
	 */
	private boolean existe(UsuarioBBDD usuario) {
		Optional<UsuarioBBDD> optional = repo.findById(usuario.getId());
		return !optional.isEmpty();
	}

	/**
	 * Verifica si el usuario que desea realizar la acción es un administrador
	 * 
	 * @param usuario usuario que desea realizar la acción
	 * @return true si es un admin, false en caso contrario o que el usuario no
	 *         exista
	 */
	private boolean isAdmin(UsuarioBBDD usuario) {
		Optional<UsuarioBBDD> optional = repo.findById(usuario.getId());
		if (optional.isEmpty())
			return false;

		UsuarioBBDD usuarioBBDD = optional.get();
		return usuarioBBDD.isAdmin();
	}

	/**
	 * Verifica si el usuario que desea realizar la acción es un developer
	 * 
	 * @param usuario usuario que desea realizar la acción
	 * @return true si es un developer, false en caso contrario o que el usuario no
	 *         exista
	 */
	private boolean isDeveloper(UsuarioBBDD usuario) {
		Optional<UsuarioBBDD> optional = repo.findById(usuario.getId());
		if (optional.isEmpty())
			return false;

		UsuarioBBDD usuarioBBDD = optional.get();
		return usuarioBBDD.isDeveloper();
	}
}
