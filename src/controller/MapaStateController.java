package controller;

import events.IMapaObserver;
import model.MapaModel;
import model.dtos.LocalidadDto;
import model.entities.Grafo;
import view.MapaView;

public class MapaStateController implements IMapaObserver {
	private MapaModel model;
	private MapaView view;

	public MapaStateController(MapaModel model, MapaView view) {
		this.model = model;
		this.view = view;
		this.model.addObserver(this);
	}

	@Override
	public void onRedCreated(Grafo red) {
		LocalidadDto dto = red.getLocalidades().getFirst().toDto();
		this.view.resaltarPrimerVertice(dto);
		red.getTodasLasConexiones().forEach((conexion) -> {
			LocalidadDto origen = conexion.getOrigen().toDto();
			LocalidadDto destino = conexion.getDestino().toDto();
			this.view.dibujarConexion(origen, destino, conexion.getCosto());
		});
		this.view.actualizarTotal(red.calcularCostoTotal());
	}

	@Override
	public void onMapaError(String message) {
		this.view.mostrarError(message);
	}

}