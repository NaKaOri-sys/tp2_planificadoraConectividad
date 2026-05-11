package view.dialogs;

import javax.swing.*;

import events.IEditSolucionListener;
import model.dtos.ConexionDto;
import model.dtos.LocalidadDto;
import util.Observable;

import java.awt.*;
import java.util.ArrayList;

public class EditSolucionDialog extends JDialog {
	private JComboBox<LocalidadDto> comboOrigen;
	private JComboBox<LocalidadDto> comboDestino;
	private JList<ConexionDto> listaConexiones;
	private DefaultListModel<ConexionDto> listModel;
	private JButton btnEliminar;
	private JButton btnAgregar;
	private JButton btnFinalizar;

	private Observable<IEditSolucionListener> observable;

	public EditSolucionDialog(Frame padre) {
		super(padre, "Modificar Solución de Red", true);
		getContentPane().setLayout(new BorderLayout(10, 10));
		setSize(500, 400);
		this.observable = new Observable<>();
		inicializarComponentes();
	}

	private void inicializarComponentes() {
		JPanel panelAgregar = new JPanel(new GridLayout(3, 2, 5, 5));
		panelAgregar.setBorder(BorderFactory.createTitledBorder("Agregar Conexión Manual"));

		comboOrigen = new JComboBox<>();
		comboDestino = new JComboBox<>();
		btnAgregar = new JButton("Conectar Localidades");
		btnAgregar.addActionListener(e -> {
			LocalidadDto origen = (LocalidadDto) comboOrigen.getSelectedItem();
			LocalidadDto destino = (LocalidadDto) comboDestino.getSelectedItem();
			if (origen != null && destino != null) {
				observable.notifyObservers(listener -> listener.onAgregarConexion(origen.getNombre(),destino.getNombre()));
			}
		});

		panelAgregar.add(new JLabel("Localidad Origen:"));
		panelAgregar.add(comboOrigen);
		panelAgregar.add(new JLabel("Localidad Destino:"));
		panelAgregar.add(comboDestino);
		panelAgregar.add(new JLabel("")); // Espacio vacío
		panelAgregar.add(btnAgregar);

		JPanel panelLista = new JPanel(new BorderLayout());
		panelLista.setBorder(BorderFactory.createTitledBorder("Conexiones en la Solución Actual"));

		listModel = new DefaultListModel<>();
		listaConexiones = new JList<>(listModel);
		JScrollPane scroll = new JScrollPane(listaConexiones);

		btnEliminar = new JButton("Eliminar Conexión Seleccionada");
		btnEliminar.addActionListener(e -> {
			ConexionDto seleccionada = listaConexiones.getSelectedValue();
			if (seleccionada != null) {
				observable.notifyObservers(o -> o.onEliminarConexion(seleccionada));
			}else {
				mostrarError("Debe seleccionar una conexión para eliminar.");
			}
		});
		btnEliminar.setForeground(Color.RED);

		panelLista.add(scroll, BorderLayout.CENTER);
		panelLista.add(btnEliminar, BorderLayout.SOUTH);

		btnFinalizar = new JButton("Finalizar Edición");
		btnFinalizar.addActionListener(e -> {
			this.observable.notifyObservers(o -> o.onFinalizarEdicion());
		});

		getContentPane().add(panelAgregar, BorderLayout.NORTH);
		getContentPane().add(panelLista, BorderLayout.CENTER);
		getContentPane().add(btnFinalizar, BorderLayout.SOUTH);
	}

	public void cargarLocalidades(ArrayList<LocalidadDto> localidades) {
		comboOrigen.removeAllItems();
		comboDestino.removeAllItems();
		
		for (LocalidadDto localidad : localidades) {
			comboOrigen.addItem(localidad);
			comboDestino.addItem(localidad);
		}
	}

	public void actualizarListaConexiones(ArrayList<ConexionDto> conexiones) {
		listModel.clear();
		for (ConexionDto conexion : conexiones) {
			listModel.addElement(conexion);
		}
	}
	
	public void mostrarError(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Error de Validación", JOptionPane.ERROR_MESSAGE);
	}
	
	public Observable<IEditSolucionListener> obtenerObserver(){
		return this.observable;
	}
}