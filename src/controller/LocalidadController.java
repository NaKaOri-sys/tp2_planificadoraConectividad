package controller;

import java.util.Map;

import dao.LocalidadDao;
import events.ILocalidadListener;
import events.ILocalidadObserver;
import model.Localidad;
import model.LocalidadModel;
import model.dtos.LocalidadDto;
import view.dialogs.LocalidadDialog;

public class LocalidadController implements ILocalidadListener, ILocalidadObserver {
	private LocalidadDialog localidadDialog;
	private LocalidadModel localidadModel;

	public LocalidadController(LocalidadDialog localidadDialog, LocalidadModel localidadModel) {
		this.localidadDialog = localidadDialog;
		this.localidadModel = localidadModel;
		this.localidadDialog.getObservable().addObserver(this);
		this.localidadModel.addObserver(this);
	}

	@Override
	public void onError(String errorMessage) {
		this.localidadDialog.mostrarError(errorMessage);
	}

	@Override
	public void onInputLocalidad(LocalidadDto localidadDto) {
		try {
			double latitud = Double.parseDouble(localidadDto.getLatitud());
			double longitud = Double.parseDouble(localidadDto.getLongitud());
			this.localidadModel.agregarLocalidad(localidadDto.getNombre(), localidadDto.getProvincia(), latitud,
					longitud);
		} catch (NumberFormatException e) {
			this.localidadDialog.mostrarError(e.getMessage());
			return;
		}
	}

	@Override
	public void onLocalidadCreated(Map<String, Localidad> localidades) {
		LocalidadDao.generarJsonLocalidad("localidades.json", localidades);
	}

	@Override
	public void onLocalidadDeleted(Map<String, Localidad> localidades) {
		LocalidadDao.generarJsonLocalidad("localidades.json", localidades);
	}
}
