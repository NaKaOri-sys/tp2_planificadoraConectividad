package main;

import controller.LocalidadDialogFacade;
import controller.LocalidadIntegrationController;
import controller.MapEventController;
import controller.MapaStateController;
import model.LocalidadModel;
import model.MapaModel;
import model.repository.LocalidadRepositoryJson;
import view.MapaView;
import view.dialogs.LocalidadDialog;

public class app {
	public static void main(String[] args) {
		MapaView mapaView = new MapaView();
		LocalidadDialog localidadDialog = new LocalidadDialog();
		
		MapaModel mapaModel = new MapaModel();
		LocalidadRepositoryJson repository = new LocalidadRepositoryJson();
		LocalidadModel localidadModel = new LocalidadModel(repository);
		
		LocalidadDialogFacade localidadFacade = new LocalidadDialogFacade(localidadDialog, localidadModel);
		
		// Controllers
		new MapaStateController(mapaModel, mapaView);
		new MapEventController(mapaModel, mapaView, localidadFacade, localidadModel.getLocalidades(), localidadModel);
		new LocalidadIntegrationController(localidadModel, mapaView);
		
		mapaView.setVisible(true);
	}

}