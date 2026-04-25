package model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import dao.LocalidadDao;
import events.ILocalidadObserver;
import util.Observable;

public class LocalidadModel extends Observable<ILocalidadObserver> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, Localidad> localidades;

	public LocalidadModel() {
		this.localidades = loadFromJson();
	}

	public void agregarLocalidad(String nombre, String provincia, double latitud, double longitud) {
		try {
			Localidad localidad = new Localidad(nombre, provincia, latitud, longitud);
			this.localidades.put(localidad.getNombre(), localidad);
			notifyObservers(observer -> observer.onLocalidadCreated(this.localidades));
		} catch (IllegalArgumentException e) {
			notifyObservers(observer -> observer.onError(e.getMessage()));
			return;
		}
	}

	public void eliminarLocalidad(Localidad localidad) {
		this.localidades.remove(localidad.getNombre());
		notifyObservers(observer -> observer.onLocalidadDeleted(this.localidades));
	}

	public Map<String, Localidad> getLocalidades() {
		return localidades;
	}

	public LinkedHashMap<String, Localidad> loadFromJson() {
		LinkedHashMap<String, Localidad> localidades = LocalidadDao.cargarDesdeJson(LocalidadDao.FILE_PATH);
		if (localidades != null) {
			return localidades;
		}
		return new LinkedHashMap<>();
	}
	
	public void saveToJson() {
		LocalidadDao.generarJsonLocalidad(LocalidadDao.FILE_PATH, this.localidades);
	}
	
	public void deleteAllLocalidades() {
		this.localidades.clear();
		LocalidadDao.eliminarArchivoJson(LocalidadDao.FILE_PATH);
	}
}
