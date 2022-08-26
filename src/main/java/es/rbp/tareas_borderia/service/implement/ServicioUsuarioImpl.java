package es.rbp.tareas_borderia.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.rbp.tareas_borderia.entidad.bbdd.UsuarioBBDD;
import es.rbp.tareas_borderia.repositorio.RepositorioUsuario;
import es.rbp.tareas_borderia.service.ServicioUsuario;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import static es.rbp.tareas_borderia.service.Acciones.*;

@Service
public class ServicioUsuarioImpl implements ServicioUsuario {

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

		case ACCION_ELIMINAR_HABITACION:
			return autorizarEliminarHabitacion(usuario);

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
		}

		return false;
	}

	@Override
	public UsuarioBBDD cambiarContrasena(UsuarioBBDD usuario, long idAfectado, String contrasena) {
		Optional<UsuarioBBDD> optional = repo.findById(idAfectado);
		if (optional.isEmpty() || contrasena == null)
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

		String token = generarToken();
		usuario.setToken(token);

		usuario.setNombre(nombre);

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
		UsuarioBBDD usuario = repo.findByNombre(nombre);
		if (usuario == null)
			return null;

		if (usuario.getToken() != null) {
			String token = generarToken();
			usuario.setToken(token);

			return repo.save(usuario);
		}

		if (contrasena == null)
			return null;

		String hash = hashString(contrasena);
		usuario = repo.findByNombreAndContrasena(nombre, hash);

		if (usuario == null)
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

	/**
	 * Verifica si el usuario tiene permitido cobrar tareas
	 * 
	 * @param usuario usuario que desea obtener los meses
	 * @return true si tiene autorización, false en caso contrario
	 */
	private boolean autorizarCobrar(UsuarioBBDD usuario) {
		Optional<UsuarioBBDD> optional = repo.findById(usuario.getId());
		return !optional.isEmpty();
	}

	/**
	 * Verifica si el usuario puede cambiar la contraseña de otro usuario
	 * 
	 * @param usuario    usuario que realiza la acción
	 * @param idAfectado id del usuario del que se desea cambiar la contraseña
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarCambioPwd(UsuarioBBDD usuario, long idAfectado) {
		long id = usuario.getId();

		if (id == idAfectado || usuario.isDeveloper())
			return true;

		return false;
	}

	/**
	 * Verifica si el usuario tiene permitido obtener los meses
	 * 
	 * @param usuario usuario que desea obtener los meses
	 * @return true si tiene autorización, false en caso contrario
	 */
	private boolean autorizarGetMeses(UsuarioBBDD usuario) {
		Optional<UsuarioBBDD> optional = repo.findById(usuario.getId());
		return !optional.isEmpty();
	}

	/**
	 * Verifica si el usuario tiene permitido añadir tareas
	 * 
	 * @param usuario que desea anadir una tarea
	 * @return true si tiene autorización, false en caso contrario
	 */
	private boolean autorizarAnadirTarea(UsuarioBBDD usuario) {
		Optional<UsuarioBBDD> optional = repo.findById(usuario.getId());
		return !optional.isEmpty();
	}

	/**
	 * Verifica si el usuario puede aceptar los términos y condiciones
	 * 
	 * @param usuario isiario que desea aceptar los términos
	 * @return true si tiene autorización, false en caso contrario
	 */
	private boolean autorizarAceptarTerminos(UsuarioBBDD usuario) {
		Optional<UsuarioBBDD> optional = repo.findById(usuario.getId());
		return !optional.isEmpty();
	}

	/**
	 * Verifica si el usuario puede ver las habitaciones
	 * 
	 * @param usuario usuario que quiere ver las habitaciones
	 * @return true si tiene autorización, false en caso contrario
	 */
	private boolean autorizarVerHabitaciones(UsuarioBBDD usuario) {
		Optional<UsuarioBBDD> optional = repo.findById(usuario.getId());
		return !optional.isEmpty();
	}

	/**
	 * Verifica si el usuario puede ver el historial
	 * 
	 * @param usuario usuario que quiere ver el historial
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarVerHistorial(UsuarioBBDD usuario) {
		Optional<UsuarioBBDD> optional = repo.findById(usuario.getId());
		return !optional.isEmpty();
	}

	/**
	 * Verifica si el usuario puede eliminar habitaciones
	 * 
	 * @param usuario usuario que desea eliminar una habitación
	 * @return true si tiene autorización, false en caso contrario
	 */
	private boolean autorizarEliminarHabitacion(UsuarioBBDD usuario) {
		Optional<UsuarioBBDD> optional = repo.findById(usuario.getId());
		return !optional.isEmpty();
	}

	/**
	 * Verifica si un usuario puede ver las tareas de configuración
	 * 
	 * @param usuario usuario que desea ver las tareas de configuración
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarVerTareaConfig(UsuarioBBDD usuario) {
		Optional<UsuarioBBDD> optional = repo.findById(usuario.getId());
		if (optional.isEmpty())
			return false;

		UsuarioBBDD usuarioBBDD = optional.get();
		return usuarioBBDD.isAdmin();
	}

	/**
	 * Verifica si un usuario puede crear tareas de configuración
	 * 
	 * @param usuario usuario que desea crear la tarea de configuración
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarCrearTareaConfig(UsuarioBBDD usuario) {
		Optional<UsuarioBBDD> optional = repo.findById(usuario.getId());
		if (optional.isEmpty())
			return false;

		UsuarioBBDD usuarioBBDD = optional.get();
		return usuarioBBDD.isAdmin();
	}

	/**
	 * Verifica si un usuario puede modificar tareas de configuración
	 * 
	 * @param usuario usuario que desea modificar la tarea de configuración
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarModificarTareaConfig(UsuarioBBDD usuario) {
		Optional<UsuarioBBDD> optional = repo.findById(usuario.getId());
		if (optional.isEmpty())
			return false;

		UsuarioBBDD usuarioBBDD = optional.get();
		return usuarioBBDD.isAdmin();
	}

	/**
	 * Verifica si un usuario puede eliminar tareas de configuración
	 * 
	 * @param usuario usuario que desea eliminar la tarea de configuración
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarEliminarTareaConfig(UsuarioBBDD usuario) {
		Optional<UsuarioBBDD> optional = repo.findById(usuario.getId());
		if (optional.isEmpty())
			return false;

		UsuarioBBDD usuarioBBDD = optional.get();
		return usuarioBBDD.isAdmin();
	}

	/**
	 * Verifica si un usuario puede ver las habitaciones de configuración
	 * 
	 * @param usuario usuario que desea ver las habitaciones de configuración
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarVerHabitacionConfig(UsuarioBBDD usuario) {
		Optional<UsuarioBBDD> optional = repo.findById(usuario.getId());
		if (optional.isEmpty())
			return false;

		UsuarioBBDD usuarioBBDD = optional.get();
		return usuarioBBDD.isAdmin();
	}

	/**
	 * Verifica si un usuario puede crear habitaciones de configuración
	 * 
	 * @param usuario usuario que desea crear la habitación de configuración
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarCrearHabitacionConfig(UsuarioBBDD usuario) {
		Optional<UsuarioBBDD> optional = repo.findById(usuario.getId());
		if (optional.isEmpty())
			return false;

		UsuarioBBDD usuarioBBDD = optional.get();
		return usuarioBBDD.isAdmin();
	}

	/**
	 * Verifica si un usuario puede modificar habitaciones de configuración
	 * 
	 * @param usuario usuario que desea modificar la habitación de configuración
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarModificarHabitacionConfig(UsuarioBBDD usuario) {
		Optional<UsuarioBBDD> optional = repo.findById(usuario.getId());
		if (optional.isEmpty())
			return false;

		UsuarioBBDD usuarioBBDD = optional.get();
		return usuarioBBDD.isAdmin();
	}

	/**
	 * Verifica si un usuario puede eliminar habitaciones de configuración
	 * 
	 * @param usuario usuario que desea eliminar la habitación de configuración
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarEliminarHabitacionConfig(UsuarioBBDD usuario) {
		Optional<UsuarioBBDD> optional = repo.findById(usuario.getId());
		if (optional.isEmpty())
			return false;

		UsuarioBBDD usuarioBBDD = optional.get();
		return usuarioBBDD.isAdmin();
	}

	/**
	 * verifica si un usuario puede crear un historial
	 * 
	 * @param usuario usuario que desea crear el historial
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarCrearHistorial(UsuarioBBDD usuario) {
		Optional<UsuarioBBDD> optional = repo.findById(usuario.getId());
		if (optional.isEmpty())
			return false;

		UsuarioBBDD usuarioBBDD = optional.get();
		return usuarioBBDD.isAdmin();
	}

	/**
	 * Verifica si un usuario puede modificar un historial
	 * 
	 * @param usuario usuario que desea modificar el historial
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarModificarHistorial(UsuarioBBDD usuario) {
		Optional<UsuarioBBDD> optional = repo.findById(usuario.getId());
		if (optional.isEmpty())
			return false;

		UsuarioBBDD usuarioBBDD = optional.get();
		return usuarioBBDD.isAdmin();
	}

	/**
	 * Verifica si un usuario puede eliminar un historial
	 * 
	 * @param usuario usuario que desea eliminar el historial
	 * @return true si está autorizado, false en caso contrario
	 */
	private boolean autorizarEliminarHistorial(UsuarioBBDD usuario) {
		Optional<UsuarioBBDD> optional = repo.findById(usuario.getId());
		if (optional.isEmpty())
			return false;

		UsuarioBBDD usuarioBBDD = optional.get();
		return usuarioBBDD.isAdmin();
	}
}
