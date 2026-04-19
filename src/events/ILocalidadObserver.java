package events;

import model.Localidad;

public interface ILocalidadObserver {
	/**
	 * Método que se llama cuando ocurre un error relacionado con la localidad.
	 * 
	 * @param errorMessage El mensaje de error que describe el problema.
	 */
	void onError(String errorMessage);

	/**
	 * Método que se llama cuando se recibe una actualización de la localidad.
	 * 
	 * @param localidad La nueva localidad recibida.
	 */
	void onLocalidadUpdate(Localidad localidad);

	/**
	 * Método que se llama cuando se crea una nueva localidad.
	 * 
	 * @param localidad La nueva localidad creada.
	 */
	void onLocalidadCreated(Localidad localidad);

	/**
	 * Método que se llama cuando se elimina una localidad.
	 * 
	 * @param localidad La localidad que ha sido eliminada.
	 */
	void onLocalidadDeleted(Localidad localidad);

}
