package model;

import java.util.LinkedHashMap;

import events.IMapaObserver;
import util.Observable;

public class MapaModel extends Observable<IMapaObserver> {
	private double costoKm;
	private double recargo;
	private double costoDifProv;
	private LinkedHashMap<String, Localidad> localidades;
	public MapaModel() {
		this.costoKm = 0.0;
		this.recargo = 0.0;
		this.costoDifProv = 0.0;
		this.localidades = new LinkedHashMap<>();
	}

	public double getCostoKm() {
		return costoKm;
	}

	public void setCostoKm(double costoKm) {
		this.costoKm = costoKm;
		notifyObservers(observer -> observer.onCostoKmChanged(String.valueOf(costoKm)));
	}

	public double getRecargo() {
		return recargo;
	}

	public void setRecargo(double recargo) {
		this.recargo = recargo;
		notifyObservers(observer -> observer.onRecargoChanged(String.valueOf(recargo)));
	}

	public double getCostoDifProv() {
		return costoDifProv;
	}

	public void setCostoDifProv(double costoDifProv) {
		this.costoDifProv = costoDifProv;
		notifyObservers(observer -> observer.onCostoDifProvChanged(String.valueOf(costoDifProv)));
	}
	
	public LinkedHashMap<String, Localidad> getLocalidades() {
		return localidades;
	}
	
	public void agregarLocalidad(Localidad localidad) {
		this.localidades.put(localidad.getNombre(), localidad);
		notifyObservers(observer -> observer.onLocalidadAgregada());
	}

}
