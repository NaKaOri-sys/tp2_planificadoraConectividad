package view.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import events.ILocalidadListener;
import events.ILocalidadObserver;
import model.dtos.LocalidadDto;
import util.Observable;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;

public class LocalidadDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtProvincia;
	private JTextField txtLatitud;
	private JTextField txtLongitud;
	private JTextField txtLocalidad;
	private JButton okButton;
	private JButton cancelButton;
	private Observable<ILocalidadListener> observable;

	/**
	 * Create the dialog.
	 */
	public LocalidadDialog() {
		this.observable = new Observable<>();
		setTitle("Agregar Localidad");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		txtProvincia = new JTextField();
		txtProvincia.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtProvincia.setBounds(10, 57, 107, 18);
		contentPanel.add(txtProvincia);
		txtProvincia.setColumns(10);

		txtLatitud = new JTextField();
		txtLatitud.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtLatitud.setBounds(10, 153, 182, 18);
		contentPanel.add(txtLatitud);
		txtLatitud.setColumns(10);

		txtLongitud = new JTextField();
		txtLongitud.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtLongitud.setBounds(232, 153, 182, 18);
		contentPanel.add(txtLongitud);
		txtLongitud.setColumns(10);

		JLabel lblProvincia = new JLabel("Provincia");
		lblProvincia.setFont(new Font("SansSerif", Font.BOLD, 15));
		lblProvincia.setBounds(10, 19, 90, 37);
		contentPanel.add(lblProvincia);

		JLabel lblLatitud = new JLabel("Latitud");
		lblLatitud.setFont(new Font("SansSerif", Font.BOLD, 15));
		lblLatitud.setBounds(10, 116, 96, 35);
		contentPanel.add(lblLatitud);

		JLabel lblLongitud = new JLabel("Longitud");
		lblLongitud.setFont(new Font("SansSerif", Font.BOLD, 15));
		lblLongitud.setBounds(232, 116, 90, 35);
		contentPanel.add(lblLongitud);

		txtLocalidad = new JTextField();
		txtLocalidad.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtLocalidad.setBounds(170, 57, 115, 18);
		contentPanel.add(txtLocalidad);
		txtLocalidad.setColumns(10);

		JLabel lblLocalidad = new JLabel("Localidad");
		lblLocalidad.setFont(new Font("SansSerif", Font.BOLD, 15));
		lblLocalidad.setBounds(170, 19, 99, 37);
		contentPanel.add(lblLocalidad);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("Agregar");
				okButton.setFont(new Font("SansSerif", Font.BOLD, 15));
				okButton.addActionListener(e -> {
					LocalidadDto localidadDto = getLocalidadFromInput();
					observable.notifyObservers(observer -> observer.onInputLocalidad(localidadDto));
					dispose();
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancelar");
				cancelButton.setFont(new Font("SansSerif", Font.BOLD, 15));
				cancelButton.addActionListener(e -> dispose());
				buttonPane.add(cancelButton);
			}
		}
	}

	public JButton getOkButton() {
		return okButton;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public LocalidadDto getLocalidadFromInput() {
		String nombre = txtLocalidad.getText().trim();
		String provincia = txtProvincia.getText().trim();
		String latitudStr = txtLatitud.getText().trim();
		String longitudStr = txtLongitud.getText().trim();

		return new LocalidadDto(nombre, provincia, latitudStr, longitudStr);
	}

	public void mostrarError(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Error de Validación", JOptionPane.ERROR_MESSAGE);
	}

	public Observable<ILocalidadListener> getObservable() {
		return this.observable;
	}
}
