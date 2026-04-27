package events;

import model.Grafo;

public interface IMapaObserver {
	void onRedCreated(Grafo grafo);
	void onMapaError(String message);
}
