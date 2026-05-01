package model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import events.ILocalidadObserver;
import model.entities.Localidad;
import model.repository.LocalidadRepositoryJson;
import util.Observable;

public class LocalidadModel extends Observable<ILocalidadObserver> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// TODO SOLID - SRP Violation: Esta clase maneja tanto lógica de negocio como persistencia (JSON)
	// Responsabilidades actuales: 1) Gestionar estado de localidades 2) Cargar/guardar en JSON 3) Notificar observadores
	// SOLUCIÓN: Crear una interfaz ILocalidadRepository (ya existe LocalidadRepositoryJson) y usar inyección de dependencias
	// La persistencia debería estar completamente separada. Considerar pattern Repository o DAO
	private Map<String, Localidad> localidades;
	private final LocalidadRepositoryJson repositoryJson;

	public LocalidadModel(LocalidadRepositoryJson localidadRepositoryJson) {
		this.repositoryJson = localidadRepositoryJson;
		this.localidades = loadFromJson();
	}

	public void agregarLocalidad(String nombre, String provincia, double latitud, double longitud) {
		try {
			Localidad localidad = new Localidad(nombre, provincia, latitud, longitud);
			this.localidades.put(localidad.getNombre(), localidad);
			notifyObservers(observer -> observer.onLocalidadCreated(this.localidades));
		} catch (IllegalArgumentException e) {
			notifyObservers(observer -> observer.onError(e.getMessage()));
		}
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
			return this.repositoryJson.loadAll();
		} catch (RuntimeException e) {
			System.err.println(e.getMessage());
			return new LinkedHashMap<>();
		}
	}

	public void saveToJson() {
		try {
			this.repositoryJson.saveAll(this.localidades);
		} catch (RuntimeException e) {
			notifyObservers(observer -> observer.onError("Error al persistir la localidad " + e.getMessage()));
		}
	}

	public void deleteAllLocalidades() {
		try {
			this.localidades.clear();
			this.repositoryJson.cleanAll();
		} catch (RuntimeException e) {
			System.err.println("Hubo un error al limpiar el archivo " + e.getMessage());
		}
	}
}
