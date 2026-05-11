package model;


import java.util.Map;

import events.IMapaObserver;
import events.IEditSolucionObserver;
import model.dtos.ConfigurationDto;
import model.entities.Conexion;
import model.entities.CostCalculator;
import model.entities.DistanceCalculator;
import model.entities.Grafo;
import model.entities.Localidad;
import model.interfaces.IGenerarRed;
import util.Observable;

public class MapaModel extends Observable<IMapaObserver> {
	private double costoKm;
	private double recargo;
	private double costoDifProv;
	private IGenerarRed generarRed;
	private Grafo agmActual;
	private Observable<IEditSolucionObserver> editSolucionObservable = new Observable<>();

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
			throw new IllegalArgumentException("El costo por ser de diferente provincia debe ser un número positivo.");
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
			if (localidades.isEmpty() || localidades.size() < 2)
				throw new IllegalArgumentException("Debe haber por lo menos 2 localidades cargadas.");
			setConfiguration(dto);
			this.agmActual = this.generarRed.generarRed(costoKm, recargo, costoDifProv, localidades);
			this.notifyObservers(
					o -> o.onRedCreated(this.agmActual));
		} catch (NumberFormatException e) {
			notifyObservers(o -> o.onMapaError("Los campos de costos configurables deben ser números validos"));
			return;
		} catch (IllegalArgumentException e) {
			notifyObservers(o -> o.onMapaError(e.getMessage()));
			return;
		}
	}

	public void modificarSolucion(Conexion conexionAEliminar, Conexion conexionAAgregar) {
		if (this.agmActual == null) {
				return;
		}
		this.agmActual = this.generarRed.recalcularRed(conexionAEliminar, conexionAAgregar,this.agmActual);
		this.notifyObservers(o -> o.onRedCreated(this.agmActual));
		this.editSolucionObservable.notifyObservers(o -> o.onSolucionModificada(this.agmActual));
	}
		
	public Conexion crearConexion(Localidad origen, Localidad destino) {
		DistanceCalculator distanceCalculator = new DistanceCalculator();
		double km = distanceCalculator.calcularDistanciaHaversine(origen, destino);
			
		CostCalculator costCalculator = new CostCalculator(this.costoKm, this.recargo, this.costoDifProv);
		double costoConexion = costCalculator.calcularCosto(origen, destino, km);
			
		return new Conexion(origen, destino, costoConexion);
	}
		
	public Grafo getAgmActual() {
		return this.agmActual;
	}
	
	public void addEditSolucionObserver(IEditSolucionObserver observer) {
		this.editSolucionObservable.addObserver(observer);
	}
}
