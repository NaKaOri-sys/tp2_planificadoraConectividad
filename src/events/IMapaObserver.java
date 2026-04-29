package events;

import model.entities.Grafo;

public interface IMapaObserver {
	void onRedCreated(Grafo grafo);
	void onMapaError(String message);
}
