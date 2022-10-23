package es.rbp.tareas_borderia.service;

public final class Acciones {

	public static final class Developer {
		public static final int ACCION_CAMBAR_PWD = 1;
		public static final int ACCION_CREAR_CODIGOS = 2;
		public static final int ACCION_MODIFICAR_CODIGOS = 3;
		public static final int ACCION_ELIMINAR_CODIGOS = 4;
		public static final int ACCION_CREAR_DEUDA_CONFIG = 5;
		public static final int ACCION_MODIFICAR_DEUDA_CONFIG = 6;
		public static final int ACCION_VER_DEUDA_CONFIG = 7;
		public static final int ACCION_VER_USUARIOS = 8;
		public static final int ACCION_MODIFICAR_USUARIOS = 9;
		public static final int ACCION_MODIFICAR_DEUDA = 10;
	}

	public static final class Usuario {
		public static final int ACCION_ACEPTAR_TERMINOS = 101;
		public static final int ACCION_OBTENER_MESES = 102;
		public static final int ACCION_ANADIR_TAREA = 103;
		public static final int ACCION_COBRAR = 104;
		public static final int ACCION_VER_HABITACIONES = 105;
		public static final int ACCION_ELIMINAR_HABITACION = 106;
		public static final int ACCION_VER_HISTORIAL = 107;
		public static final int ACCION_VER_DEUDA = 108;
		public static final int ACCION_AUMENTAR_DEUDA = 109;
		public static final int ACCION_VER_TERMINOS = 110;
	}

	public static final class Admin {
		public static final int ACCION_VER_TAREAS_CONFIG = 201;
		public static final int ACCION_CREAR_TAREA_CONFIG = 202;
		public static final int ACCION_MODIFICAR_TAREA_CONFIG = 203;
		public static final int ACCION_ELIMINAR_TAREA_CONFIG = 204;
		public static final int ACCION_VER_HABITACIONES_CONFIG = 205;
		public static final int ACCION_CREAR_HABITACION_CONFIG = 206;
		public static final int ACCION_MODIFICAR_HABITACION_CONFIG = 207;
		public static final int ACCION_ELIMINAR_HABITACION_CONFIG = 208;
		public static final int ACCION_CREAR_HISTORIAL = 209;
		public static final int ACCION_MODIFICAR_HISTORIAL = 210;
		public static final int ACCION_ELIMINAR_HISTORIAL = 211;
		public static final int ACCION_VER_CODIGOS = 212;
		public static final int ACCION_ANADIR_TERMINOS = 213;
		public static final int ACCION_MODIFICAR_TERMINOS = 214;
		public static final int ACCION_ELIMINAR_TERMINOS = 215;
	}
}
