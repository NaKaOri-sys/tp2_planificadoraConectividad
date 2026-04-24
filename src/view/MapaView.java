package view;

import javax.swing.JFrame;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MapaView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JMapViewer mapa;
	private JTextField txtCostoKm;
	private JTextField txtRecargo;
	private JTextField txtCostoDifProv;
	
	public MapaView() {
		setTitle("Redimensionador Red");
		setBounds(100, 100, 800, 600);
		setResizable(false);
		mapa = new JMapViewer();
		//Coordenada Argentina
		Coordinate cord = new Coordinate(-34.490150, -58.734755);
		mapa.setDisplayPosition(cord, 5);
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(mapa, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panel.setBounds(522, 78, 260, 430);
		mapa.add(panel);
		panel.setLayout(null);
		
		JButton btnAddLocalidad = new JButton("Agregar Localidad");
		btnAddLocalidad.setToolTipText("Agregar una localidad al mapa");
		btnAddLocalidad.setFont(new Font("SansSerif", Font.BOLD, 10));
		btnAddLocalidad.setBounds(10, 10, 132, 42);
		panel.add(btnAddLocalidad);
		
		JButton btnCalcular = new JButton("Calcular");
		btnCalcular.setToolTipText("Calcular el costo de transporte entre localidades");
		btnCalcular.setFont(new Font("SansSerif", Font.BOLD, 10));
		btnCalcular.setBounds(168, 10, 79, 42);
		panel.add(btnCalcular);
		
		JButton btnCleanMapa = new JButton("Limpiar Mapa");
		btnCleanMapa.setToolTipText("Limpiar el mapa de localidades");
		btnCleanMapa.setFont(new Font("SansSerif", Font.BOLD, 10));
		btnCleanMapa.setBounds(125, 377, 122, 42);
		panel.add(btnCleanMapa);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 114, 142, 198);
		panel.add(scrollPane);
		
		JList list = new JList();
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
		panel.add(txtCostoKm);
		txtCostoKm.setColumns(10);
		
		txtRecargo = new JTextField();
		txtRecargo.setText("0");
		txtRecargo.setToolTipText("Porcentaje de recargo por transporte urgente");
		txtRecargo.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtRecargo.setBounds(129, 336, 96, 18);
		panel.add(txtRecargo);
		txtRecargo.setColumns(10);
		
		txtCostoDifProv = new JTextField();
		txtCostoDifProv.setText("0");
		txtCostoDifProv.setToolTipText("Costo adicional por transportar entre diferentes provincias");
		txtCostoDifProv.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtCostoDifProv.setBounds(10, 377, 96, 18);
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
		panel.add(btnUpdateLocalidad);
		
		JButton btnDeleteLocalidad = new JButton("Eliminar ");
		btnDeleteLocalidad.setToolTipText("Eliminar una localidad seleccionada de la lista");
		btnDeleteLocalidad.setFont(new Font("SansSerif", Font.BOLD, 10));
		btnDeleteLocalidad.setBounds(162, 183, 85, 42);
		panel.add(btnDeleteLocalidad);
	}
}
