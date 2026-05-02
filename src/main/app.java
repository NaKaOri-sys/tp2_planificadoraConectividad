package main;

import controller.LocalidadController;
import controller.LocalidadDialogFacade;
import controller.LocalidadIntegrationController;
import controller.MapEventController;
import controller.MapaStateController;
import model.GenerarRedModel;
import model.LocalidadModel;
import model.MapaModel;
import model.entities.Grafo;
import model.interfaces.IGenerarRed;
import model.repository.ILocalidadRepository;
import model.repository.LocalidadRepositoryJson;
import model.strategy.IArbolGeneradorMinimo;
import model.strategy.Prim;
import view.MapaView;
import view.dialogs.LocalidadDialog;

public class app {
	public static void main(String[] args) {
		MapaView mapaView = new MapaView();
		LocalidadDialog localidadDialog = new LocalidadDialog();
		IArbolGeneradorMinimo agm = new Prim();
		IGenerarRed red = new GenerarRedModel(agm);
		MapaModel mapaModel = new MapaModel(red);
		ILocalidadRepository repository = new LocalidadRepositoryJson();
		LocalidadModel localidadModel = new LocalidadModel(repository);
		
		LocalidadDialogFacade localidadFacade = new LocalidadDialogFacade(localidadDialog, localidadModel);
		
		// Controllers
		new MapaStateController(mapaModel, mapaView);
		new MapEventController(mapaModel, mapaView, localidadFacade, localidadModel.getLocalidades(), localidadModel);
		new LocalidadIntegrationController(localidadModel, mapaView);
		new LocalidadController(localidadDialog, localidadModel);
		
		mapaView.setVisible(true);
	}

}