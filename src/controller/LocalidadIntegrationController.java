package controller;

import java.util.Map;

import events.ILocalidadObserver;
import model.LocalidadModel;
import model.dtos.LocalidadDto;
import model.entities.Localidad;
import view.MapaView;

public class LocalidadIntegrationController implements ILocalidadObserver{
	 private final MapaView view;
	    
	    public LocalidadIntegrationController(LocalidadModel model, MapaView view) {
	        this.view = view;
	        model.addObserver(this);
	        this.view.actualizarLocalidades(model.getLocalidades().keySet());
	        dibujarLocalidadesEnMapa(model.getLocalidades());
	    }
	    
	    @Override
	    public void onLocalidadCreated(Map<String, Localidad> localidades) {
	        view.actualizarLocalidades(localidades.keySet());
	        dibujarLocalidadesEnMapa(localidades);
	    }
	    
	    @Override
	    public void onLocalidadDeleted(Map<String, Localidad> localidades) {
	        view.actualizarLocalidades(localidades.keySet());
	        view.limpiarMapa();
	        dibujarLocalidadesEnMapa(localidades);
	    }
	    
	    @Override
	    public void onError(String errorMessage) {
	        view.mostrarError(errorMessage);
	    }
	    
	    private void dibujarLocalidadesEnMapa(Map<String, Localidad> localidades) {
			localidades.forEach((nombre, localidad) -> {
				LocalidadDto dto = localidad.toDto();
				this.view.agregarLocalidadAlMapa(dto);
			});
		}
}
