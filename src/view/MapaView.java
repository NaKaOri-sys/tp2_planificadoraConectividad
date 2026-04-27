package view;

import javax.swing.JFrame;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;

import events.ILocalidadListener;
import events.IMapaListener;
import model.dtos.ConfigurationDto;
import model.dtos.LocalidadDto;
import util.Observable;
import view.components.AristaConCosto;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
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
	private JLabel lblCostoTotal;

	public MapaView() {
		observable = new Observable<>();
		setTitle("Redimensionador Red");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
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

		JButton btnCalcular = new JButton("Generar Red");
		btnCalcular.setToolTipText("Generar la red de conexiones entre localidades y mostrar el costo total");
		btnCalcular.setFont(new Font("SansSerif", Font.BOLD, 14));
		btnCalcular.setBounds(10, 481, 142, 42);
		btnCalcular.addActionListener(a -> {
			observable.notifyObservers(o -> o.onCalcular());
		});
		panel.add(btnCalcular);

		JButton btnCleanMapa = new JButton("Limpiar Mapa");
		btnCleanMapa.setToolTipText("Limpiar el mapa de localidades y conexiones");
		btnCleanMapa.setFont(new Font("SansSerif", Font.BOLD, 10));
		btnCleanMapa.setBounds(148, 10, 122, 42);
		btnCleanMapa.addActionListener(a -> {
			observable.notifyObservers(o -> o.onLimpiarMapa());
		});
		panel.add(btnCleanMapa);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 249, 142, 198);
		panel.add(scrollPane);

		list = new JList<>();
		list.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				String nombreLocalidad = list.getSelectedValue();
				if (nombreLocalidad != null) {
					observable.notifyObservers(o -> o.onLocalidadSeleccionada(nombreLocalidad));
				}
			}
		});
		scrollPane.setViewportView(list);

		JLabel lblNewLabel = new JLabel("Localidades");
		lblNewLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(36, 211, 85, 42);
		panel.add(lblNewLabel);

		txtCostoKm = new JTextField();
		txtCostoKm.setText("0");
		txtCostoKm.setToolTipText("Costo por kilómetro de conexión entre localidades");
		txtCostoKm.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtCostoKm.setBounds(10, 123, 96, 18);
		panel.add(txtCostoKm);
		txtCostoKm.setColumns(10);

		txtRecargo = new JTextField();
		txtRecargo.setText("0");
		txtRecargo.setToolTipText("Porcentaje de recargo si la conexión tiene más de 300 km de distancia");
		txtRecargo.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtRecargo.setBounds(10, 169, 96, 18);
		panel.add(txtRecargo);
		txtRecargo.setColumns(10);

		txtCostoDifProv = new JTextField();
		txtCostoDifProv.setText("0");
		txtCostoDifProv
				.setToolTipText("Costo adicional por conectar localidades que se encuentran en diferentes provincias");
		txtCostoDifProv.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtCostoDifProv.setBounds(126, 123, 96, 18);
		panel.add(txtCostoDifProv);
		txtCostoDifProv.setColumns(10);

		JLabel lblCostoXKm = new JLabel("Costo por KM ($)");
		lblCostoXKm.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblCostoXKm.setBounds(10, 102, 111, 18);
		panel.add(lblCostoXKm);

		JLabel lblCostoDifProv = new JLabel("Costo diferentes provincias");
		lblCostoDifProv.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblCostoDifProv.setBounds(126, 98, 190, 26);
		panel.add(lblCostoDifProv);

		JLabel lblPorcRecargo = new JLabel("% Exceso (>300KM)");
		lblPorcRecargo.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblPorcRecargo.setBounds(10, 150, 132, 16);
		panel.add(lblPorcRecargo);

		JButton btnUpdateLocalidad = new JButton("Actualizar");
		btnUpdateLocalidad.setToolTipText("Actualizar la información de una localidad seleccionada en la lista");
		btnUpdateLocalidad.setFont(new Font("SansSerif", Font.BOLD, 10));
		btnUpdateLocalidad.setBounds(162, 300, 85, 42);
		btnUpdateLocalidad.addActionListener(a -> {
			String nombreLocalidad = list.getSelectedValue();
			if (nombreLocalidad != null) {
				observable.notifyObservers(o -> o.onActualizarLocalidad(nombreLocalidad));
			}
		});
		panel.add(btnUpdateLocalidad);

		JButton btnDeleteLocalidad = new JButton("Eliminar ");
		btnDeleteLocalidad.setToolTipText("Eliminar una localidad seleccionada de la lista");
		btnDeleteLocalidad.setFont(new Font("SansSerif", Font.BOLD, 10));
		btnDeleteLocalidad.addActionListener(a -> {
			String nombreLocalidad = list.getSelectedValue();
			if (nombreLocalidad != null) {
				observable.notifyObservers(o -> o.onEliminarLocalidad(nombreLocalidad));
			}
		});
		btnDeleteLocalidad.setBounds(162, 352, 85, 42);
		panel.add(btnDeleteLocalidad);

		lblCostoTotal = new JLabel("$0");
		lblCostoTotal.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblCostoTotal.setBounds(162, 481, 79, 42);
		panel.add(lblCostoTotal);

		JLabel lblCostoTotalTitle = new JLabel("Costo Total");
		lblCostoTotalTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblCostoTotalTitle.setBounds(162, 458, 85, 42);
		panel.add(lblCostoTotalTitle);

		JLabel lblConfiguraciones = new JLabel("Configuraciones");
		lblConfiguraciones.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblConfiguraciones.setBounds(36, 70, 176, 22);
		panel.add(lblConfiguraciones);
	}

	public void hacerFocoEnLocalidadSeleccionada(LocalidadDto dto) {
		double latitud = Double.parseDouble(dto.getLatitud());
		double longitud = Double.parseDouble(dto.getLongitud());
		Coordinate coord = new Coordinate(latitud, longitud);
		mapa.setDisplayPosition(coord, 10);
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
		MapMarkerDot marker = new MapMarkerDot(dto.getNombre(), coord);
		mapa.addMapMarker(marker);
	}

	public void dibujarConexion(LocalidadDto origen, LocalidadDto destino, double costo) {
		double latOrigen = Double.parseDouble(origen.getLatitud());
		double longOrigen = Double.parseDouble(origen.getLongitud());
		Coordinate coordOrigen = new Coordinate(latOrigen, longOrigen);
		double latDestino = Double.parseDouble(destino.getLatitud());
		double longDestino = Double.parseDouble(destino.getLongitud());
		Coordinate coordDestino = new Coordinate(latDestino, longDestino);
		AristaConCosto conexion = new AristaConCosto(Arrays.asList(coordOrigen, coordDestino, coordOrigen), costo);
		conexion.setColor(Color.blue);
		conexion.setBackColor(null);
		mapa.addMapPolygon(conexion);
	}

	public void limpiarMapa() {
		mapa.removeAllMapMarkers();
		limpiarConexiones();
	}

	public void limpiarConexiones() {
		mapa.removeAllMapPolygons();
	}

	public void mostrarError(String message) {
		JOptionPane.showMessageDialog(this, message, "Error de Validación", JOptionPane.ERROR_MESSAGE);
	}

	public void actualizarTotal(double costoTotal) {
		BigDecimal total = new BigDecimal(costoTotal);
		total = total.setScale(2, RoundingMode.HALF_UP);
		lblCostoTotal.setText("$" + total);
	}
	
	
	public ConfigurationDto obtenerConfigurables() {
		ConfigurationDto dto = new ConfigurationDto(this.txtCostoKm.getText(), this.txtRecargo.getText(), this.txtCostoDifProv.getText());
		
		return dto;
	}

	public void resaltarPrimerVertice(LocalidadDto dto) {
		double latitud = Double.parseDouble(dto.getLatitud());
		double longitud = Double.parseDouble(dto.getLongitud());
		Coordinate coord = new Coordinate(latitud, longitud);
		MapMarkerDot primerVertice = new MapMarkerDot(dto.getNombre(), coord);
		mapa.removeMapMarker(primerVertice);
		primerVertice.setBackColor(Color.red);
		mapa.addMapMarker(primerVertice);
	}
}
