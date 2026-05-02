package model;

import model.entities.Grafo;
import model.strategy.IArbolGeneradorMinimo;

public class AGM {

	private IArbolGeneradorMinimo agm;

	public void setAGM(IArbolGeneradorMinimo agm) {
		this.agm = agm;
	}

	public Grafo generarAGM() {
		return this.agm.generarAGM();
	}
}