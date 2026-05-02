package controller;

import events.ILocalidadListener;
import model.LocalidadModel;
import model.dtos.LocalidadDto;
import model.entities.CoordinateValidator;
import view.dialogs.LocalidadDialog;

/**
 * Facade que gestiona la interacción entre LocalidadDialog, MapEventController
 * y LocalidadModel. Centraliza la lógica de agregar, actualizar y eliminar
 * localidades.
 */
public class LocalidadDialogFacade implements ILocalidadListener {
	private LocalidadDialog localidadDialog;
	private LocalidadModel localidadModel;

	public LocalidadDialogFacade(LocalidadDialog localidadDialog, LocalidadModel localidadModel) {
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
		// TODO Implementar: Obtener localidad del modelo, rellenar formulario, mostrar diálogo
		// Debe permitir editar solo coordenadas (latitud/longitud)
	}

	/**
	 * Elimina una localidad del modelo y actualiza la vista.
	 * 
	 * @param nombreLocalidad Nombre de la localidad a eliminar
	 */
	public void eliminar(String nombreLocalidad) {
		// TODO Implementar: Obtener localidad del modelo, llamar a eliminarLocalidad(), guardar cambios
		// El modelo dispara onLocalidadDeleted que hace saveToJson() automáticamente
	}

	@Override
	public void onInputLocalidad(LocalidadDto localidadDto) {
		try {
			CoordinateValidator coordenadas = new CoordinateValidator(localidadDto.getLatitud(),
					localidadDto.getLongitud());
			this.localidadModel.agregarLocalidad(localidadDto.getNombre(), localidadDto.getProvincia(),
					coordenadas.getLatitud(), coordenadas.getLongitud());
		} catch (NumberFormatException e) {
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
		// TODO Implementar: Buscar localidad en modelo, actualizar coordenadas, guardar cambios
		// Nota: Actualmente no existe un método updateLocalidad en LocalidadModel, debe crearse
	}

	/**
	 * Limpia los campos del formulario en el diálogo.
	 */
	private void limpiarFormulario() {
		// Esto requeriría agregar métodos en LocalidadDialog para acceder y limpiar los
		// campos
	}
}
