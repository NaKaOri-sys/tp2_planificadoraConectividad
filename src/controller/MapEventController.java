package controller;

import java.util.HashSet;
import java.util.Map;

import events.IMapaListener;
import model.LocalidadModel;
import model.MapaModel;
import model.dtos.ConfigurationDto;
import model.dtos.LocalidadDto;
import model.entities.Localidad;
import view.MapaView;

public class MapEventController implements IMapaListener {
	// TODO SOLID - SRP Violation: Clase con múltiples responsabilidades
	// Esta clase maneja: 1) Lógica de localidades (onAgregarLocalidad, onEliminarLocalidad, onActualizarLocalidad, onLocalidadSeleccionada)
	// 2) Lógica de mapa (onCalcular, onLimpiarMapa)
	// SOLUCIÓN: Separar en dos controllers: LocalidadEventController (para localidades) y MapCalculationController (para cálculos del mapa)
	private MapaModel model;
	private MapaView view;
	private LocalidadDialogFacade localidadFacade;
	private Map<String, Localidad> localidades;
	private LocalidadModel localidadModel;

	public MapEventController(MapaModel model, MapaView view, LocalidadDialogFacade localidadFacade,
			Map<String, Localidad> localidades, LocalidadModel localidadModel) {
		this.model = model;
		this.view = view;
		this.localidadFacade = localidadFacade;
		this.localidades = localidades;
		this.localidadModel = localidadModel;
		view.getObservable().addObserver(this);
	}

	@Override
	public void onAgregarLocalidad() {
		this.localidadFacade.mostrar();
	}

	@Override
	public void onCalcular() {
		ConfigurationDto dto = this.view.obtenerConfigurables();
		this.model.generarRed(dto, this.localidades);
	}

	@Override
	public void onLimpiarMapa() {
		this.view.limpiarMapa();
		this.localidades.clear();
		this.view.actualizarLocalidades(new HashSet<String>());
		this.localidadModel.deleteAllLocalidades();
	}

	@Override
	public void onLocalidadSeleccionada(String nombreLocalidad) {
		Localidad localidad = this.localidades.get(nombreLocalidad);
		if (localidad != null) {
			LocalidadDto dto = localidad.toDto();
			this.view.hacerFocoEnLocalidadSeleccionada(dto);
		}
	}

	@Override
	public void onEliminarLocalidad(String nombreLocalidad) {
		this.localidadFacade.eliminar(nombreLocalidad);
	}

	@Override
	public void onActualizarLocalidad(String nombreLocalidad) {
		this.localidadFacade.mostrarParaEditar(nombreLocalidad);
	}
}
