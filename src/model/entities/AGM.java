package model.entities;

import model.strategy.IArbolGeneradorMinimo;

public class AGM {

	private IArbolGeneradorMinimo agm;
	
	public void setAGM(IArbolGeneradorMinimo agm) {
		this.agm = agm;
	}

	public Grafo generarAGM(Grafo g) {
		return this.agm.generarAGM(g);
	}
}