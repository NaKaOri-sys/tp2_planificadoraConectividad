package controller;

import events.ILocalidadListener;
import model.LocalidadModel;
import model.dtos.LocalidadDto;
import model.entities.CoordinateValidator;
import model.entities.Localidad;
import view.dialogs.LocalidadDialog;

public class LocalidadDialogController implements ILocalidadListener {
	private LocalidadDialog localidadDialog;
	private LocalidadModel localidadModel;

	public LocalidadDialogController(LocalidadDialog localidadDialog, LocalidadModel localidadModel) {
		this.localidadDialog = localidadDialog;
		this.localidadModel = localidadModel;

		this.localidadDialog.getObservable().addObserver(this);
	}

	/**
	 * Muestra el diálogo para agregar una nueva localidad.
	 */
	public void mostrar() {
		limpiarFormulario();
		this.localidadDialog.setVisible(true);
	}

	/**
	 * Muestra el diálogo para editar una localidad existente.
	 * 
	 * @param nombreLocalidad Nombre de la localidad a editar
	 */
	public void mostrarParaEditar(String nombreLocalidad) {
		Localidad localidad = localidadModel.getLocalidades().get(nombreLocalidad);
		if (localidad == null) {
			this.localidadDialog.mostrarError("Se debe seleccionar una localidad en la lista.");
			return;
		}
		this.localidadDialog.modoEdicion(true);
		this.localidadDialog.cargarDatosParaEditar(localidad.getNombre(), localidad.getProvincia(),
				String.valueOf(localidad.getLatitud()), String.valueOf(localidad.getLongitud()));
		this.localidadDialog.setVisible(true);
	}

	/**
	 * Elimina una localidad del modelo y actualiza la vista.
	 * 
	 * @param nombreLocalidad Nombre de la localidad a eliminar
	 */
	public void eliminar(String nombreLocalidad) {
		Localidad localidad = localidadModel.getLocalidades().get(nombreLocalidad);
		if (localidad == null) {
			this.localidadDialog.mostrarError("No se encontró la localidad: " + nombreLocalidad);
			return;
		}
		localidadModel.eliminarLocalidad(localidad);
	}

	@Override
	public void onInputLocalidad(LocalidadDto localidadDto) {
		if (this.localidadModel.getLocalidades().containsKey(localidadDto.getNombre())) {
			actualizarLocalidad(localidadDto);
			return;
		}
		try {
			CoordinateValidator coordenadas = new CoordinateValidator(localidadDto.getLatitud(),
					localidadDto.getLongitud());
			this.localidadModel.agregarLocalidad(localidadDto.getNombre(), localidadDto.getProvincia(),
					coordenadas.getLatitud(), coordenadas.getLongitud());
		} catch (Exception e) {
			this.localidadDialog.mostrarError(e.getMessage());
		}
	}

	/**
	 * Actualiza una localidad existente en el modelo. Solo puede modificar longitud
	 * y latitud.
	 * 
	 * @param localidadDto Nuevos datos de la localidad
	 */
	private void actualizarLocalidad(LocalidadDto localidadDto) {
		try {
			CoordinateValidator coordenadas = new CoordinateValidator(localidadDto.getLatitud(),
					localidadDto.getLongitud());
			this.localidadModel.actualizarLocalidad(localidadDto.getNombre(), coordenadas.getLatitud(),
					coordenadas.getLongitud());
			this.localidadDialog.setVisible(false);
			this.localidadDialog.modoEdicion(false);

		} catch (NumberFormatException e) {
			this.localidadDialog.mostrarError(e.getMessage());
		}
	}

	/**
	 * Limpia los campos del formulario en el diálogo.
	 */
	private void limpiarFormulario() {
		this.localidadDialog.limpiarFormulario();
	}
}
