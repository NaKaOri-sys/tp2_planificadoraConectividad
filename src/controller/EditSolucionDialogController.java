package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import events.IEditSolucionListener;
import model.MapaModel;
import model.entities.Conexion;
import model.entities.Localidad;
import view.dialogs.EditSolucionDialog;
import model.dtos.ConexionDto;
import model.dtos.LocalidadDto;

public class EditSolucionDialogController implements IEditSolucionListener {
	private EditSolucionDialog editSolucionDialog;
	private MapaModel mapaModel;
	private Map<String, Localidad> localidades;

	public EditSolucionDialogController(EditSolucionDialog editSolucionDialog, MapaModel mapaModel,
			Map<String, Localidad> localidades) {
		this.editSolucionDialog = editSolucionDialog;
		this.mapaModel = mapaModel;
		this.localidades = localidades;
		this.editSolucionDialog.obtenerObserver().addObserver(this);
	}

	public void mostrar() {
		ArrayList<LocalidadDto> localidadDto = new ArrayList<>();
		for (Localidad localidad : localidades.values()) {
			localidadDto.add(localidad.toDto());
		}
		this.editSolucionDialog.cargarLocalidades(localidadDto);
		actualizarListaVisual();
		this.editSolucionDialog.setVisible(true);
	}

	private void actualizarListaVisual() {
		if (mapaModel.getAgmActual() != null) {
			ArrayList<ConexionDto> conexionesDto = new ArrayList<>();
			for (Conexion conexion : mapaModel.getAgmActual().getTodasLasConexiones()) {
				conexionesDto.add(conexion.toDto());
			}
			this.editSolucionDialog.actualizarListaConexiones(conexionesDto);
		}
	}

	@Override
	public void onAgregarConexion(String nombreOrigen, String nombreDestino) {
		if (nombreOrigen.equals(nombreDestino)) {
			this.editSolucionDialog.mostrarError("No se puede conectar una localidad consigo misma.");
			return;
		}
		Localidad origen = localidades.get(nombreOrigen);
		Localidad destino = localidades.get(nombreDestino);
		if (origen == null || destino == null) {
			this.editSolucionDialog.mostrarError("No se encontraron las localidades seleccionadas.");
			return;
		}

		Conexion nuevaConexion = this.mapaModel.crearConexion(origen, destino);

		this.mapaModel.modificarSolucion(null, nuevaConexion);
		this.actualizarListaVisual();
	}

	@Override
	public void onEliminarConexion(ConexionDto conexionAEliminar) {
		if (this.mapaModel.getAgmActual() == null) {
			this.editSolucionDialog.mostrarError("No hay una red generada para modificar.");
			return;
		}
		if (this.mapaModel.getAgmActual().getTodasLasConexiones().size() == 1) {
			this.editSolucionDialog.mostrarError("No se puede eliminar la única conexión de la solución.");
			return;
		}
		Conexion aBorrar = encontrarConexion(conexionAEliminar);
		
		if (aBorrar == null) {
			this.editSolucionDialog.mostrarError("No se encontró la conexión a eliminar.");
			return;
		}

		this.mapaModel.modificarSolucion(aBorrar, null);
		this.actualizarListaVisual();
	}

	private Conexion encontrarConexion(ConexionDto conexionDto) {
		for (Conexion conexion : this.mapaModel.getAgmActual().getTodasLasConexiones()) {
			if (sonLasMismasConexiones(conexion, conexionDto)) {
				return conexion;
			}
		}
		return null;
	}

	private boolean sonLasMismasConexiones(Conexion conexion, ConexionDto conexionDto) {
		return conexion.getOrigen().getNombre().equals(conexionDto.getOrigen())
				&& conexion.getDestino().getNombre().equals(conexionDto.getDestino());
	}

	@Override
	public void onFinalizarEdicion() {
		this.editSolucionDialog.setVisible(false);
	}
}
