package model;

import java.util.Map;

import events.IMapaObserver;
import model.dtos.ConfigurationDto;
import util.Observable;

public class MapaModel extends Observable<IMapaObserver> {
	private double costoKm;
	private double recargo;
	private double costoDifProv;

	public MapaModel() {
		this.costoKm = 0.0;
		this.recargo = 0.0;
		this.costoDifProv = 0.0;
	}

	public void setConfiguration(ConfigurationDto dto) {
		this.costoKm = Double.parseDouble(dto.getCostoKm());
		if (costoKm <= 0)
			throw new IllegalArgumentException("El costo por KM debe ser un número mayor a 0.");
		this.recargo = Double.parseDouble(dto.getRecargo());
		if (recargo < 0)
			throw new IllegalArgumentException("El recargo debe ser un número positivo.");
		this.costoDifProv = Double.parseDouble(dto.getCostoDifProv());
		if (costoDifProv < 0)
			throw new IllegalArgumentException(
					"El costo por ser de diferente provincia debe ser un número positivo.");
	}

	public double getCostoKm() {
		return costoKm;
	}

	public double getRecargo() {
		return recargo;
	}

	public double getCostoDifProv() {
		return costoDifProv;
	}

	public void generarRed(ConfigurationDto dto, Map<String, Localidad> localidades) {
		try {
			if (localidades.size() < 2)
				throw new IllegalArgumentException("Debe haber por lo menos 2 localidades cargadas.");
			setConfiguration(dto);
			GenerarRedModel red = new GenerarRedModel(costoKm, recargo, costoDifProv, localidades);
			this.notifyObservers(o -> o.onRedCreated(red.generarRed()));
		} catch (NumberFormatException e) {
			notifyObservers(o -> o.onMapaError("Los campos de costos configurables deben ser números validos"));
			return;
		} catch (IllegalArgumentException e) {
			notifyObservers(o -> o.onMapaError(e.getMessage()));
			return;
		}
	}

}
