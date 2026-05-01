package model;

import java.util.Map;

import events.IMapaObserver;
import model.dtos.ConfigurationDto;
import model.entities.Localidad;
import model.interfaces.IGenerarRed;
import util.Observable;

public class MapaModel extends Observable<IMapaObserver> {
	// TODO SOLID - SRP Violation: Clase con responsabilidades mixtas
	// Responsabilidades: 1) Guardar configuración 2) Validar configuración 3) Orquestar generación de red
	// SOLUCIÓN: Crear una clase ConfigurationValidator separada para encapsular toda la lógica de validación
	// Esto haría MapaModel más simple y la validación reutilizable
	private double costoKm;
	private double recargo;
	private double costoDifProv;
	private IGenerarRed generarRed;
	
	public MapaModel(IGenerarRed generarRed) {
		this.costoKm = 0.0;
		this.recargo = 0.0;
		this.costoDifProv = 0.0;
		this.generarRed = generarRed;
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
			// TODO SOLID - DIP: El modelo accede directamente a una interfaz inyectada pero también
			// realiza validaciones de negocio. Considerar extraer un servicio de validación de configuración
			// para hacer la lógica más testeable y reutilizable en otros contextos
			this.notifyObservers(o -> o.onRedCreated(this.generarRed.generarRed(costoKm, recargo, costoDifProv, localidades)));
		} catch (NumberFormatException e) {
			notifyObservers(o -> o.onMapaError("Los campos de costos configurables deben ser números validos"));
			return;
		} catch (IllegalArgumentException e) {
			notifyObservers(o -> o.onMapaError(e.getMessage()));
			return;
		}
	}

}
