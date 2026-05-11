package controller;

import java.util.ArrayList;
import java.util.Map;

import events.IEditSolucionListener;
import model.MapaModel;
import model.entities.Conexion;
import model.entities.Localidad;
import view.dialogs.EditSolucionDialog;
import model.dtos.ConexionDto;
import model.dtos.LocalidadDto;

public class EditSolucionDialogFacade implements IEditSolucionListener {
	private EditSolucionDialog editSolucionDialog;
	private MapaModel mapaModel;
	private Map<String, Localidad> localidades;
	
	public EditSolucionDialogFacade(EditSolucionDialog editSolucionDialog,MapaModel mapaModel, Map<String, Localidad> localidades) {
		this.editSolucionDialog = editSolucionDialog;
		this.mapaModel = mapaModel;
		this.localidades = localidades;
		this.editSolucionDialog.obtenerObserver().addObserver(this);
	}
	public void mostrar() {
		ArrayList<LocalidadDto> localidadDto = new ArrayList<>();
		for(Localidad localidad : localidades.values()) {
			localidadDto.add(localidad.toDto());
		}
		this.editSolucionDialog.cargarLocalidades(localidadDto);
		actualizarListaVisual();
		this.editSolucionDialog.setVisible(true);
	}
	
	private void actualizarListaVisual() {
		if(mapaModel.getAgmActual() != null) {
			ArrayList<ConexionDto> conexionesDto = new ArrayList<>();
			for(Conexion conexion : mapaModel.getAgmActual().getTodasLasConexiones()) {
				conexionesDto.add(conexion.toDto());
			}
			this.editSolucionDialog.actualizarListaConexiones(conexionesDto);
		}
	}
	
	@Override
	public void onAgregarConexion(String nombreOrigen, String nombreDestino) {
		if(nombreOrigen.equals(nombreDestino)) {
			this.editSolucionDialog.mostrarError("No se puede conectar una localidad consigo misma.");
			return;
		}
		Localidad origen = null;
		Localidad destino = null;
		for(Localidad localidad : localidades.values()) {
			if(localidad.getNombre().equals(nombreOrigen)) {
				origen = localidad;
			}
			if(localidad.getNombre().equals(nombreDestino)) {
				destino = localidad;
			}
		}
		
		if(origen != null && destino != null) {
			Conexion nuevaConexion = this.mapaModel.crearConexion(origen, destino);
			
			this.mapaModel.modificarSolucion(null, nuevaConexion);
			this.actualizarListaVisual();
		}else {
			this.editSolucionDialog.mostrarError("No se encontraron las localidades seleccionadas.");
		}
	}
	
	@Override
	public void onEliminarConexion(ConexionDto conexionAEliminar) {
		if(this.mapaModel.getAgmActual() == null) {
			this.editSolucionDialog.mostrarError("No hay una red generada para modificar.");
			return;
		}
		
		Conexion aBorrar = null;
		Conexion aAgregar = null;
		
		for(Conexion conexion : this.mapaModel.getAgmActual().getTodasLasConexiones()) {
			if(conexion.getOrigen().getNombre().equals(conexionAEliminar.getOrigen()) && 
					conexion.getDestino().getNombre().equals(conexionAEliminar.getDestino())) {
				aBorrar = conexion;
				break;
			}
		}
		
		if(aBorrar == null) {
			this.editSolucionDialog.mostrarError("No se encontró la conexión a eliminar.");
		}
		
		this.mapaModel.modificarSolucion(aBorrar, aAgregar);
		this.actualizarListaVisual();
	}

	@Override
	public void onFinalizarEdicion() {
		this.editSolucionDialog.setVisible(false);
	}
}
