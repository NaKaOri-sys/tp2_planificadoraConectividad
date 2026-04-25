package controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import events.ILocalidadObserver;
import events.IMapaListener;
import events.IMapaObserver;
import model.Localidad;
import model.LocalidadModel;
import model.MapaModel;
import model.dtos.LocalidadDto;
import view.MapaView;
import view.dialogs.LocalidadDialog;

public class MapaController implements IMapaListener, IMapaObserver, ILocalidadObserver {
	private MapaModel model;
	private MapaView view;
	private LocalidadDialog localidadDialog;
	private LocalidadController localidadController;
	private LocalidadModel localidadModel;
	private Map<String, Localidad> localidades;

	public MapaController(MapaModel model, MapaView view) {
		this.model = model;
		this.view = view;

		this.localidades = new LinkedHashMap<>();
		this.localidadDialog = new LocalidadDialog();
		this.localidadModel = new LocalidadModel();
		this.localidadController = new LocalidadController(this.localidadDialog, this.localidadModel);

		this.view.getObservable().addObserver(this);
		this.localidadDialog.getObservable().addObserver(this.localidadController);
		this.localidadModel.addObserver(this);
		initializarLocalidades();
	}

	private void initializarLocalidades() {
		this.localidades = this.localidadModel.loadFromJson();
		if (this.localidades == null || this.localidades.isEmpty()) {
			return;
		}
		this.view.actualizarLocalidades(this.localidades.keySet());
		dibujarLocalidadesEnMapa();
	}

	@Override
	public void onAgregarLocalidad() {
		this.localidadDialog.setVisible(true);
	}

	@Override
	public void onCalcular() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLimpiarMapa() {
		this.view.limpiarMapa();
		this.localidades.clear();
		this.view.actualizarLocalidades(new HashSet<String>());
		this.localidadModel.deleteAllLocalidades();
	}

	@Override
	public void onLocalidadSeleccionada(int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCostoKmChanged(String nuevoValor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRecargoChanged(String nuevoValor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCostoDifProvChanged(String nuevoValor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(String errorMessage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocalidadCreated(Map<String, Localidad> localidades) {
		for (Entry<String, Localidad> entry : localidades.entrySet()) {
			this.localidades.put(entry.getKey(), entry.getValue());
		}
		this.view.actualizarLocalidades(this.localidades.keySet());
		dibujarLocalidadesEnMapa();
	}

	private void dibujarLocalidadesEnMapa() {
		this.localidades.forEach((nombre, localidad) -> {
			LocalidadDto dto = new LocalidadDto(localidad.getNombre(), localidad.getProvincia(),
					String.valueOf(localidad.getLatitud()), String.valueOf(localidad.getLongitud()));
			this.view.agregarLocalidadAlMapa(dto);
		});
	}

	@Override
	public void onLocalidadDeleted(Map<String, Localidad> localidades) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocalidadAgregada() {
		// TODO Auto-generated method stub

	}

}
