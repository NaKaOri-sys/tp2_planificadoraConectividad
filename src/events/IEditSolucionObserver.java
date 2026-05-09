package events;

import model.entities.Grafo;

/**
 * Observer para cambios en la solución (AGM).
 * Notifica cuando la solución ha sido modificada.
 */
public interface IEditSolucionObserver {
	
	/**
	 * Se dispara cuando se ha aceptado una modificación en la solución
	 */
	void onSolucionModificada(Grafo nuevoAGM);
	
	/**
	 * Se dispara cuando hay un error al intentar modificar la solución
	 */
	void onSolucionError(String mensaje);
}
