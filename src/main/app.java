package main;


import controller.EditSolucionController;
import controller.EditSolucionDialogFacade;
import controller.LocalidadController;
import controller.LocalidadDialogFacade;
import controller.LocalidadIntegrationController;
import controller.MapEventController;
import controller.MapaStateController;
import model.GenerarRedModel;
import model.LocalidadModel;
import model.MapaModel;
import model.entities.AGM;
import model.interfaces.IGenerarRed;
import model.repository.ILocalidadRepository;
import model.repository.LocalidadRepositoryJson;
import model.strategy.IArbolGeneradorMinimo;
import model.strategy.Prim;
import view.MapaView;
import view.dialogs.LocalidadDialog;
import view.dialogs.EditSolucionDialog;

public class app {
	private static final String FILE_PATH = "localidades.json";
	
	public static void main(String[] args) {
		MapaView mapaView = new MapaView();
		LocalidadDialog localidadDialog = new LocalidadDialog(mapaView);
		EditSolucionDialog editSolucionDialog = new EditSolucionDialog(mapaView);
		AGM agm = new AGM();
		IArbolGeneradorMinimo arbolGeneradorMinimo = new Prim();
		agm.setAGM(arbolGeneradorMinimo);
		IGenerarRed red = new GenerarRedModel(agm);
		MapaModel mapaModel = new MapaModel(red);
		ILocalidadRepository repository = new LocalidadRepositoryJson(FILE_PATH);
		LocalidadModel localidadModel = new LocalidadModel(repository);
		
		LocalidadDialogFacade localidadFacade = new LocalidadDialogFacade(localidadDialog, localidadModel);
		EditSolucionDialogFacade editSolucionFacade = new EditSolucionDialogFacade(editSolucionDialog, mapaModel, localidadModel.getLocalidades());
		
		// Controllers
		new MapaStateController(mapaModel, mapaView);
		new MapEventController(mapaModel, mapaView, localidadFacade, localidadModel.getLocalidades(), localidadModel, editSolucionFacade);
		new LocalidadIntegrationController(localidadModel, mapaView);
		new LocalidadController(localidadDialog, localidadModel);
		new EditSolucionController(editSolucionDialog, mapaModel);
		
		mapaView.setVisible(true);
	}

}