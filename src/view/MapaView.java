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
		panel.setBounds(533, 78, 249, 351);
		mapa.add(panel);
		panel.setLayout(null);
		
		JButton btnAddLocalidad = new JButton("Agregar Localidad");
		btnAddLocalidad.setBounds(10, 10, 123, 42);
		panel.add(btnAddLocalidad);
		
		JButton btnCalcular = new JButton("Calcular");
		btnCalcular.setBounds(143, 10, 96, 42);
		panel.add(btnCalcular);
		
		JButton btnCleanMapa = new JButton("Limpiar Mapa");
		btnCleanMapa.setBounds(143, 62, 96, 42);
		panel.add(btnCleanMapa);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 105, 123, 108);
		panel.add(scrollPane);
		
		JList list = new JList();
		scrollPane.setViewportView(list);
		
		JLabel lblNewLabel = new JLabel("Localidades");
		lblNewLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(20, 62, 85, 42);
		panel.add(lblNewLabel);
		
		txtCostoKm = new JTextField();
		txtCostoKm.setText("0");
		txtCostoKm.setBounds(10, 270, 96, 18);
		panel.add(txtCostoKm);
		txtCostoKm.setColumns(10);
		
		txtRecargo = new JTextField();
		txtRecargo.setText("0");
		txtRecargo.setBounds(143, 270, 96, 18);
		panel.add(txtRecargo);
		txtRecargo.setColumns(10);
		
		txtCostoDifProv = new JTextField();
		txtCostoDifProv.setText("0");
		txtCostoDifProv.setBounds(10, 323, 96, 18);
		panel.add(txtCostoDifProv);
		txtCostoDifProv.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Costo x KM");
		lblNewLabel_1.setBounds(10, 251, 95, 18);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Costo por diferentes provincias");
		lblNewLabel_2.setBounds(10, 298, 190, 26);
		panel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("% de recargo");
		lblNewLabel_3.setBounds(143, 252, 96, 16);
		panel.add(lblNewLabel_3);
	}
}
