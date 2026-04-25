package view;

import javax.swing.JFrame;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

import events.ILocalidadListener;
import events.IMapaListener;
import model.dtos.LocalidadDto;
import util.Observable;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.util.Set;
import java.awt.event.ActionEvent;

public class MapaView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JMapViewer mapa;
	private JTextField txtCostoKm;
	private JTextField txtRecargo;
	private JTextField txtCostoDifProv;
	private JList<String> list;
	private Observable<IMapaListener> observable;

	public MapaView() {
		observable = new Observable<>();
		setTitle("Redimensionador Red");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		setResizable(false);
		mapa = new JMapViewer();
		// Coordenada Argentina
		Coordinate cord = new Coordinate(-34.490150, -58.734755);
		mapa.setDisplayPosition(cord, 5);
		getContentPane().add(mapa, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.EAST);
		panel.setPreferredSize(new Dimension(280, 0));
		panel.setBounds(522, 78, 260, 430);
		panel.setLayout(null);

		JButton btnAddLocalidad = new JButton("Agregar Localidad");
		btnAddLocalidad.setToolTipText("Agregar una localidad al mapa");
		btnAddLocalidad.setFont(new Font("SansSerif", Font.BOLD, 10));
		btnAddLocalidad.setBounds(10, 10, 132, 42);
		btnAddLocalidad.addActionListener(a -> {
			observable.notifyObservers(o -> o.onAgregarLocalidad());
		});
		panel.add(btnAddLocalidad);

		JButton btnCalcular = new JButton("Calcular");
		btnCalcular.setToolTipText("Calcular el costo de transporte entre localidades");
		btnCalcular.setFont(new Font("SansSerif", Font.BOLD, 10));
		btnCalcular.setBounds(168, 10, 79, 42);
		btnCalcular.addActionListener(a -> {
			observable.notifyObservers(o -> o.onCalcular());
		});
		panel.add(btnCalcular);

		JButton btnCleanMapa = new JButton("Limpiar Mapa");
		btnCleanMapa.setToolTipText("Limpiar el mapa de localidades");
		btnCleanMapa.setFont(new Font("SansSerif", Font.BOLD, 10));
		btnCleanMapa.setBounds(125, 377, 122, 42);
		btnCleanMapa.addActionListener(a -> {
			observable.notifyObservers(o -> o.onLimpiarMapa());
		});
		panel.add(btnCleanMapa);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 114, 142, 198);
		panel.add(scrollPane);

		list = new JList<>();
		scrollPane.setViewportView(list);

		JLabel lblNewLabel = new JLabel("Localidades");
		lblNewLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(33, 75, 85, 42);
		panel.add(lblNewLabel);

		txtCostoKm = new JTextField();
		txtCostoKm.setText("0");
		txtCostoKm.setToolTipText("Costo por kilómetro de transporte");
		txtCostoKm.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtCostoKm.setBounds(10, 336, 96, 18);
		txtCostoKm.addActionListener(a -> {
			observable.notifyObservers(o -> o.onCostoKmChanged(txtCostoKm.getText()));
		});
		panel.add(txtCostoKm);
		txtCostoKm.setColumns(10);

		txtRecargo = new JTextField();
		txtRecargo.setText("0");
		txtRecargo.setToolTipText("Porcentaje de recargo por transporte urgente");
		txtRecargo.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtRecargo.setBounds(129, 336, 96, 18);
		txtRecargo.addActionListener(a -> {
			observable.notifyObservers(o -> o.onRecargoChanged(txtRecargo.getText()));
		});
		panel.add(txtRecargo);
		txtRecargo.setColumns(10);

		txtCostoDifProv = new JTextField();
		txtCostoDifProv.setText("0");
		txtCostoDifProv.setToolTipText("Costo adicional por transportar entre diferentes provincias");
		txtCostoDifProv.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtCostoDifProv.setBounds(10, 377, 96, 18);
		txtCostoDifProv.addActionListener(a -> {
			observable.notifyObservers(o -> o.onCostoDifProvChanged(txtCostoDifProv.getText()));
		});
		panel.add(txtCostoDifProv);
		txtCostoDifProv.setColumns(10);

		JLabel lblCostoXKm = new JLabel("Costo x KM");
		lblCostoXKm.setFont(new Font("SansSerif", Font.PLAIN, 11));
		lblCostoXKm.setBounds(10, 315, 95, 18);
		panel.add(lblCostoXKm);

		JLabel lblCostoDifProv = new JLabel("Costo por diferentes provincias");
		lblCostoDifProv.setFont(new Font("SansSerif", Font.PLAIN, 11));
		lblCostoDifProv.setBounds(10, 354, 190, 26);
		panel.add(lblCostoDifProv);

		JLabel lblPorcRecargo = new JLabel("% de recargo");
		lblPorcRecargo.setFont(new Font("SansSerif", Font.PLAIN, 11));
		lblPorcRecargo.setBounds(129, 316, 96, 16);
		panel.add(lblPorcRecargo);

		JButton btnUpdateLocalidad = new JButton("Actualizar");
		btnUpdateLocalidad.setToolTipText("Actualizar la información de una localidad seleccionada en la lista");
		btnUpdateLocalidad.setFont(new Font("SansSerif", Font.BOLD, 10));
		btnUpdateLocalidad.setBounds(162, 135, 85, 42);
		btnUpdateLocalidad.addActionListener(a -> {
			int selectedIndex = list.getSelectedIndex();
			if (selectedIndex != -1) {
				observable.notifyObservers(o -> o.onLocalidadSeleccionada(selectedIndex));
			}
		});
		panel.add(btnUpdateLocalidad);

		JButton btnDeleteLocalidad = new JButton("Eliminar ");
		btnDeleteLocalidad.setToolTipText("Eliminar una localidad seleccionada de la lista");
		btnDeleteLocalidad.setFont(new Font("SansSerif", Font.BOLD, 10));
		btnDeleteLocalidad.addActionListener(a -> {
			int selectedIndex = list.getSelectedIndex();
			if (selectedIndex != -1) {
				observable.notifyObservers(o -> o.onLocalidadSeleccionada(selectedIndex));
			}
		});
		btnDeleteLocalidad.setBounds(162, 183, 85, 42);
		panel.add(btnDeleteLocalidad);
	}

	public Observable<IMapaListener> getObservable() {
		return this.observable;
	}

	public void actualizarLocalidades(Set<String> localidades) {
		list.setListData(localidades.toArray(new String[0]));
	}
	

	public void agregarLocalidadAlMapa(LocalidadDto dto) {
		double latitud = Double.parseDouble(dto.getLatitud());
		double longitud = Double.parseDouble(dto.getLongitud());
		Coordinate coord = new Coordinate(latitud, longitud);
		mapa.addMapMarker(new MapMarkerDot(dto.getNombre(), coord));
	}

	public void limpiarMapa() {
		mapa.removeAllMapMarkers();
	}
}
