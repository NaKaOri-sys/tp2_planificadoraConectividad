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

	// TODO Implementar: Agregar método para onModificarSolucion()
	// Este método debería:
	// - Permitir al usuario seleccionar una conexión del AGM actual
	// - Mostrar las alternativas disponibles
	// - Calcular el cambio en el costo total
	// - Aplicar la modificación si el usuario lo aprueba
	// - Actualizar la visualización en el mapa
}