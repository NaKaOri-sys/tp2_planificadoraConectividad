package model;

import java.util.HashMap;
import java.util.Map;

import events.ILocalidadObserver;
import util.Observable;

public class LocalidadModel extends Observable<ILocalidadObserver> {
	private Map<String, Localidad> localidades;

	public LocalidadModel() {
		this.localidades = new HashMap<>();
	}

	public void agregarLocalidad(Localidad localidad) {
		this.localidades.put(localidad.getNombre(), localidad);
		notifyObservers(observer -> observer.onLocalidadCreated(this.localidades));
	}

	public void eliminarLocalidad(Localidad localidad) {
		this.localidades.remove(localidad.getNombre());
		notifyObservers(observer -> observer.onLocalidadDeleted(this.localidades));
	}
}
