package events;

public interface IMapaListener {
	void onAgregarLocalidad();
	void onCalcular();
	void onLimpiarMapa();
	void onLocalidadSeleccionada(int index);
	void onCostoKmChanged(String nuevoValor);
	void onRecargoChanged(String nuevoValor);
	void onCostoDifProvChanged(String nuevoValor);
}
