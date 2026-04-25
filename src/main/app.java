package main;

import controller.MapaController;
import model.MapaModel;
import view.MapaView;

public class app {
	public static void main(String[] args) {
		MapaView mapa = new MapaView();
		MapaModel mapaModel = new MapaModel();
		new MapaController(mapaModel, mapa);
		mapa.setVisible(true);
	}

}
