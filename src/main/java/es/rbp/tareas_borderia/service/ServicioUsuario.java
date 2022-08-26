package es.rbp.tareas_borderia.service;

import es.rbp.tareas_borderia.entidad.bbdd.UsuarioBBDD;

public interface ServicioUsuario {

	/**
	 * Verifica si un usuario tiene autorización para realizar una acción concreta
	 * 
	 * @param usuario    Usuario que realiza la acción
	 * @param accion     Acción que desea realizar el usuario
	 * @param idAfectado Id del usuario al que podría afectar la acción que desea
	 *                   realizar el usuario. 0 o el id del usuario que realiza la
	 *                   acción si no hay usuarios afectados
	 * 
	 * @return true si tiene autorización, false en caso contrario
	 */
	boolean estaAutorizado(UsuarioBBDD usuario, int accion, long idAfectado);

	/**
	 * Cambia la contraseña de un usuario y actualiza el campo cambiarPasswd en
	 * función de quien cambie la contraseña.
	 * 
	 * @param usuario    usuario que cambia la contraseña
	 * @param idAfectado usuario al que se le cambiará la contraseña
	 * @param contrasena contraseña nueva del usuario
	 * 
	 * @return Usuario con la contraseña actualizada, null en caso de que no exista
	 *         el usuario al que se le quiere cambiar la contraseña
	 */
	UsuarioBBDD cambiarContrasena(UsuarioBBDD usuario, long idAfectado, String contrasena);

	/**
	 * Acepta los términos y condiciones de un usuario
	 * 
	 * @param usuarioBBDD usuario que acepta los términos y condiciones
	 * @return usuario actualizado
	 */
	UsuarioBBDD aceptarTerminos(UsuarioBBDD usuarioBBDD);

	/**
	 * Crea un usuario nuevo si no hay ninguno con el mismo nombre
	 * 
	 * @param nombre     nombre del nuevo usuario
	 * @param contrasena contraseña sin hashear del nuevo usuario
	 * 
	 * @return Usuario creado, null si existe uno con el mismo nombre
	 */
	UsuarioBBDD crearUsuario(String nombre, String contrasena);

	/**
	 * Verifica si existe un usuario con el nombre y contraseña indicadas
	 * 
	 * @param nombre     nombre del usuairo
	 * @param contrasena contraseña sin hashear del usuario
	 * 
	 * @return Usuario que posea ese nombre y contraseña, null en caso de no haya
	 *         coincidencias
	 */
	UsuarioBBDD login(String nombre, String contrasena);

	/**
	 * Cierra la sesión del usuario eliminando su token
	 * 
	 * @param id id del usuario que cierra sesión
	 */
	void logout(long id);

	/**
	 * Busca un usuario por su id y token
	 * 
	 * @param id    id del usuario
	 * @param token token del usuario
	 * 
	 * @return usuario que posea ese id y token, null en caso de que no haya
	 *         coincidencias
	 */
	UsuarioBBDD findByIdAndToken(long id, String token);

	/**
	 * Busca un usuario por su id
	 * 
	 * @param id id del usuario a buscar
	 * @return Usuario con el id indicado, null si no existe
	 */
	UsuarioBBDD findById(long id);
}
