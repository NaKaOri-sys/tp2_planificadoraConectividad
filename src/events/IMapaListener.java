package events;

public interface IMapaListener {
	/**
	 *  Evento que se dispara cuando el usuario hace clic en el botón "Agregar Localidad" en la vista del mapa.
	 **/
	void onAgregarLocalidad();
	
	/**
	 * Evento que se dispara cuando el usuario hace clic en el botón "Generar Red" en la vista del mapa.
	 **/
	void onCalcular();

	/**
	 * Evento que se dispara cuando el usuario hace clic en el botón "Limpiar Mapa" en la vista del mapa.
	 **/
	void onLimpiarMapa();
	
	/**
	 * Evento que se dispara cuando el usuario selecciona una localidad en la lista.
	 * @param nombreLocalidad El nombre de la localidad seleccionada.
	 **/
	void onLocalidadSeleccionada(String nombreLocalidad);
	
	/**
	 * Evento que se dispara cuando el usuario hace clic en el botón "Eliminar" en la vista del mapa.
	 * Solamente cuando hay una localidad seleccionada en la lista.
	 * @param nombreLocalidad El nombre de la localidad a eliminar.
	 **/
	void onEliminarLocalidad(String nombreLocalidad);
	
	/**
	 * Evento que se dispara cuando el usuario hace clic en el botón "Actualizar" en la vista del mapa.
	 * Solamente cuando hay una localidad seleccionada en la lista.
	 * @param nombreLocalidad El nombre de la localidad a actualizar.
	 **/
	void onActualizarLocalidad(String nombreLocalidad);
}
