package model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import events.ILocalidadObserver;
import model.entities.Localidad;
import model.repository.ILocalidadRepository;
import util.Observable;

public class LocalidadModel extends Observable<ILocalidadObserver> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, Localidad> localidades;
	private final ILocalidadRepository repository;

	public LocalidadModel(ILocalidadRepository repository) {
		this.repository = repository;
		this.localidades = loadFromJson();
	}

	public void agregarLocalidad(String nombre, String provincia, double latitud, double longitud) {
		try {
			Localidad localidad = new Localidad(nombre, provincia, latitud, longitud);
			this.localidades.put(localidad.buildKeyLocalidad(), localidad);
		} catch (IllegalArgumentException e) {
			notifyObservers(observer -> observer.onError(e.getMessage()));
			return;
		}
		notifyObservers(observer -> observer.onLocalidadCreated(this.localidades));
	}

	public void eliminarLocalidad(Localidad localidad) {
		this.localidades.remove(localidad.getNombre());
		notifyObservers(observer -> observer.onLocalidadDeleted(this.localidades));
	}

	public Map<String, Localidad> getLocalidades() {
		return localidades;
	}

	public Map<String, Localidad> loadFromJson() {
		try {
			return this.repository.loadAll();
		} catch (RuntimeException e) {
			System.err.println(e.getMessage());
			return new LinkedHashMap<>();
		}
	}

	public void saveToJson() {
		try {
			this.repository.saveAll(this.localidades);
		} catch (RuntimeException e) {
			notifyObservers(observer -> observer.onError("Error al persistir la localidad " + e.getMessage()));
		}
	}

	public void deleteAllLocalidades() {
		try {
			this.localidades.clear();
			this.repository.cleanAll();
		} catch (RuntimeException e) {
			System.err.println("Hubo un error al limpiar el archivo " + e.getMessage());
		}
	}
}
