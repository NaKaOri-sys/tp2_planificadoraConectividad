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
	private LocalidadDialogController localidadDialog;
	private EditSolucionDialogController editSolucionDialog;
	private Map<String, Localidad> localidades;
	private LocalidadModel localidadModel;

	public MapEventController(MapaModel model, MapaView view, LocalidadDialogController localidadDialog,
			Map<String, Localidad> localidades, LocalidadModel localidadModel, EditSolucionDialogController editSolucionDialog) {
		this.model = model;
		this.view = view;
		this.localidadDialog = localidadDialog;
		this.localidades = localidades;
		this.localidadModel = localidadModel;
		this.editSolucionDialog = editSolucionDialog;
		view.getObservable().addObserver(this);
	}

	@Override
	public void onAgregarLocalidad() {
		this.localidadDialog.mostrar();
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
		this.view.actualizarTotal(0);
		this.view.mostrarEditar(false);
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
		if (nombreLocalidad == null || nombreLocalidad.isBlank()) {
			this.view.mostrarError("Debe seleccionar al menos una localidad para eliminarla.");
			return;
		}
		this.localidadDialog.eliminar(nombreLocalidad);
	}

	@Override
	public void onActualizarLocalidad(String nombreLocalidad) {
		if (nombreLocalidad == null || nombreLocalidad.isBlank()) {
			this.view.mostrarError("Debe seleccionar al menos una localidad para actualizar.");
			return;
		}
		this.localidadDialog.mostrarParaEditar(nombreLocalidad);
	}

	@Override
	public void onEditSolucion() {
		this.editSolucionDialog.mostrar();
	}

}