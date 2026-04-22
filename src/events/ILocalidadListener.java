package events;

import model.dtos.LocalidadDto;

public interface ILocalidadListener {
	/**
	 * Método que se llama para manejar la entrada de datos de una localidad desde el diálogo.
	 * 
	 * @param localidadDto Un objeto LocalidadDto que contiene los datos de la
	 *                     localidad ingresada en el diálogo.
	 */
	void onInputLocalidad(LocalidadDto localidadDto);
}
