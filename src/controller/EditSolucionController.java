package controller;

import events.IEditSolucionObserver;
import model.MapaModel;
import model.entities.Conexion;
import model.dtos.ConexionDto;
import java.util.ArrayList;
import model.entities.Grafo;
import view.dialogs.EditSolucionDialog;

public class EditSolucionController implements IEditSolucionObserver {
	private EditSolucionDialog editSolucionDialog;
	private MapaModel mapaModel;
	
	public EditSolucionController(EditSolucionDialog editSolucionDialog, MapaModel mapaModel) {
		this.editSolucionDialog = editSolucionDialog;
		this.mapaModel = mapaModel;
		this.mapaModel.addEditSolucionObserver(this);
	}

	@Override
	public void onSolucionModificada(Grafo nuevoAGM) {
		ArrayList<ConexionDto> conexionesDto = new ArrayList<>();
		for(Conexion conexion : nuevoAGM.getTodasLasConexiones()) {
			conexionesDto.add(conexion.toDto());
		}
		this.editSolucionDialog.actualizarListaConexiones(conexionesDto);
	}

	@Override
	public void onSolucionError(String mensaje) {
		this.editSolucionDialog.mostrarError(mensaje);
	}
	
	
}
