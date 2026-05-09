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
		inicializarComponentes();
	}

	private void inicializarComponentes() {
		JPanel panelAgregar = new JPanel(new GridLayout(3, 2, 5, 5));
		panelAgregar.setBorder(BorderFactory.createTitledBorder("Agregar Conexión Manual"));

		comboOrigen = new JComboBox<>();
		comboDestino = new JComboBox<>();
		btnAgregar = new JButton("Conectar Localidades");
		btnAgregar.addActionListener(e -> {

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
			//TODO metodo para esto
		});
		btnEliminar.setForeground(Color.RED);

		panelLista.add(scroll, BorderLayout.CENTER);
		panelLista.add(btnEliminar, BorderLayout.SOUTH);

		btnFinalizar = new JButton("Finalizar Edición");
		btnFinalizar.addActionListener(e -> {
			//TODO metodo
		});

		getContentPane().add(panelAgregar, BorderLayout.NORTH);
		getContentPane().add(panelLista, BorderLayout.CENTER);
		getContentPane().add(btnFinalizar, BorderLayout.SOUTH);
	}

	public void cargarLocalidades(ArrayList<LocalidadDto> localidades) {
		// TODO: Limpiar combos y cargarlos con la lista
	}

	public void actualizarListaConexiones(ArrayList<ConexionDto> conexiones) {
		// TODO: Limpiar listModel y cargar las conexiones actuales
	}
	
	public Observable<IEditSolucionListener> obtenerObserver(){
		return this.observable;
	}
}