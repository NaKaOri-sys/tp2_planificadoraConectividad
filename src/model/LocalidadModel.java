package model;

import java.util.ArrayList;
import java.util.List;

import events.ILocalidadObserver;
import util.Observable;

public class LocalidadModel extends Observable<ILocalidadObserver> {
	private List<Localidad> localidades;

	public LocalidadModel() {
		this.localidades = new ArrayList<>();
	}

	public void agregarLocalidad(Localidad localidad) {
		this.localidades.add(localidad);
		notifyObservers(observer -> observer.onLocalidadCreated(localidad));
	}

}
