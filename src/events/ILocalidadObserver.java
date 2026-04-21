package events;

import java.util.Map;

import model.Localidad;
import model.dtos.LocalidadDto;

public interface ILocalidadObserver {
	/**
	 * Método que se llama cuando ocurre un error relacionado con la localidad.
	 * 
	 * @param errorMessage El mensaje de error que describe el problema.
	 */
	void onError(String errorMessage);

	/**
	 * Método que se llama cuando se crea una nueva localidad.
	 * 
	 * @param localidades Un mapa de todas las localidades después de la creación.
	 */
	void onLocalidadCreated(Map<String, Localidad> localidades);

	/**
	 * Método que se llama cuando se elimina una localidad.
	 * 
	 * @param localidad Un mapa de las localidades restantes después de la
	 *                  eliminación.
	 */
	void onLocalidadDeleted(Map<String, Localidad> localidades);
}
