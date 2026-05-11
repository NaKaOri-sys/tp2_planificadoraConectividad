package events;

import model.dtos.ConexionDto;

/**
 * Listener para eventos relacionados con la edición de soluciones.
 * Son los botones o elementos de la view los que hay que incluir
 */
public interface IEditSolucionListener {
	
	/**
	 * Se dispara cuando el usuario solicita conectar dos localidades manualmente.
	 */
	void onAgregarConexion(String nombreOrigen, String nombreDestino);
	
	/**
	 * Se dispara cuando el usuario selecciona una conexión de la lista para borrarla.
	 */
	void onEliminarConexion(ConexionDto conexionAEliminar);
	
	/**
	 * Se dispara cuando el usuario termina de editar y cierra la ventana.
	 */
	void onFinalizarEdicion();

}
