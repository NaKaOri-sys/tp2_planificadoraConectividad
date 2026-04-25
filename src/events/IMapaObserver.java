package events;

public interface IMapaObserver {
	void onLocalidadAgregada();
	void onCostoKmChanged(String nuevoValor);
	void onRecargoChanged(String nuevoValor);
	void onCostoDifProvChanged(String nuevoValor);
}
