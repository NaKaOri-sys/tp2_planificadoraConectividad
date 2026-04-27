package controller;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import events.ILocalidadObserver;
import events.IMapaListener;
import events.IMapaObserver;
import model.Grafo;
import model.Localidad;
import model.LocalidadModel;
import model.MapaModel;
import model.dtos.ConfigurationDto;
import model.dtos.LocalidadDto;
import view.MapaView;
import view.dialogs.LocalidadDialog;

public class MapaController implements IMapaListener, IMapaObserver, ILocalidadObserver {
	private MapaModel model;
	private MapaView view;
	private LocalidadDialog localidadDialog;
	private LocalidadModel localidadModel;
	private Map<String, Localidad> localidades;

	public MapaController(MapaModel model, MapaView view) {
		this.model = model;
		this.view = view;
		this.model.addObserver(this);

		this.localidades = new LinkedHashMap<>();
		this.localidadDialog = new LocalidadDialog();
		this.localidadModel = new LocalidadModel();
		new LocalidadController(this.localidadDialog, this.localidadModel);

		this.localidadModel.addObserver(this);
		this.view.getObservable().addObserver(this);
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
	public void onError(String errorMessage) {
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
			LocalidadDto dto = localidadToDto(localidad);
			this.view.agregarLocalidadAlMapa(dto);
		});
	}

	@Override
	public void onLocalidadDeleted(Map<String, Localidad> localidades) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocalidadSeleccionada(String nombreLocalidad) {
		Localidad localidad = this.localidades.get(nombreLocalidad);
		if (localidad != null) {
			LocalidadDto dto = localidadToDto(localidad);
			this.view.hacerFocoEnLocalidadSeleccionada(dto);
		}
	}

	@Override
	public void onEliminarLocalidad(String nombreLocalidad) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActualizarLocalidad(String nombreLocalidad) {
		// TODO Auto-generated method stub

	}

	public LocalidadDto localidadToDto(Localidad localidad) {
		LocalidadDto dto = new LocalidadDto(localidad.getNombre(), localidad.getProvincia(),
				String.valueOf(localidad.getLatitud()), String.valueOf(localidad.getLongitud()));
		return dto;
	}

	@Override
	public void onRedCreated(Grafo red) {
		LocalidadDto dto = localidadToDto(red.getLocalidades().getFirst());
		this.view.resaltarPrimerVertice(dto);
		red.getTodasLasConexiones().forEach((conexion) -> {
			LocalidadDto origen = localidadToDto(conexion.getOrigen());
			LocalidadDto destino = localidadToDto(conexion.getDestino());
			this.view.dibujarConexion(origen, destino, conexion.getCosto());
		});
		this.view.actualizarTotal(red.calcularCostoTotal());
	}

	@Override
	public void onMapaError(String message) {
		this.view.mostrarError(message);
	}

}
