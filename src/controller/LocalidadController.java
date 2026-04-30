package controller;

import java.util.Map;

import events.ILocalidadListener;
import events.ILocalidadObserver;
import model.LocalidadModel;
import model.dtos.LocalidadDto;
import model.entities.CoordinateValidator;
import model.entities.Localidad;
import view.dialogs.LocalidadDialog;

public class LocalidadController implements ILocalidadObserver {
	private LocalidadDialog localidadDialog;
	private LocalidadModel localidadModel;

	public LocalidadController(LocalidadDialog localidadDialog, LocalidadModel localidadModel) {
		this.localidadDialog = localidadDialog;
		this.localidadModel = localidadModel;
		this.localidadModel.addObserver(this);
	}

	@Override
	public void onError(String errorMessage) {
		this.localidadDialog.mostrarError(errorMessage);
	}

	@Override
	public void onLocalidadCreated(Map<String, Localidad> localidades) {
		this.localidadModel.saveToJson();
	}

	@Override
	public void onLocalidadDeleted(Map<String, Localidad> localidades) {
		this.localidadModel.saveToJson();
	}
}