package programa;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Lista.EmptyListException;
import Lista.InvalidPositionException;
import Lista.PositionList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class GUI extends JFrame {
	private JPanel panel;

	private static GUI frame;
	private static Logica logica;
	private static Locale esLocale, enLocale;
	private static ResourceBundle textos;

	int xx, xy;
	private JComboBox comboBoxIdioma, comboBoxArchivos;
	private Button botonCerrar, botonCargar, botonAceptar;
	private JLabel etiquetaIdioma, etiquetaDirectorio, etiquetaResultado, etiquetaArchivos;
	private JTextArea textAreaDirectorio, textAreaResultado;
	private ActionListener AL;

	public static void main(String[] args) {

		esLocale = new Locale("es", "ES");
		enLocale = new Locale("en", "US");
		textos = ResourceBundle.getBundle("locales/MessagesBundle", esLocale);

		logica = new Logica();
		frame = new GUI();
		frame.setUndecorated(true);
		frame.setVisible(true);

	}

	public GUI() {

		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 898, 449);
		panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(SystemColor.activeCaptionBorder);
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panel);

		comboBoxIdioma = new JComboBox();
		comboBoxIdioma.setFont(new Font("Dialog", Font.BOLD, 18));
		comboBoxIdioma.setBounds(200, 14, 160, 36);
		panel.add(comboBoxIdioma);
		comboBoxIdioma.addItem("Español");
		comboBoxIdioma.addItem("Inglés");

		comboBoxIdioma.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox combo = (JComboBox) e.getSource();
				int sel = combo.getSelectedIndex();
				actualizar_idioma(sel);
			}
		});

		etiquetaIdioma = new JLabel(textos.getString("Lenguaje"));
		etiquetaIdioma.setFont(new Font("Dialog", Font.BOLD, 18));
		etiquetaIdioma.setBounds(37, 11, 153, 36);
		panel.add(etiquetaIdioma);

		etiquetaDirectorio = new JLabel(textos.getString("Directorio"));
		etiquetaDirectorio.setFont(new Font("Dialog", Font.BOLD, 18));
		etiquetaDirectorio.setBounds(37, 108, 323, 36);
		panel.add(etiquetaDirectorio);

		botonCargar = new Button(textos.getString("Cargar_Directorio"));
		botonCargar.setFont(new Font("Dialog", Font.BOLD, 18));
		botonCargar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargarDirectorio();
			}
		});

		botonCargar.setForeground(Color.WHITE);
		botonCargar.setBackground(SystemColor.textHighlight);
		botonCargar.setBounds(37, 66, 323, 36);
		panel.add(botonCargar);

		botonAceptar = new Button(textos.getString("Aceptar"));
		botonAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aceptar();
			}
		});
		botonAceptar.setForeground(Color.WHITE);
		botonAceptar.setFont(new Font("Dialog", Font.BOLD, 18));
		botonAceptar.setBackground(SystemColor.textHighlight);
		botonAceptar.setBounds(37, 232, 323, 36);
		panel.add(botonAceptar);

		botonCerrar = new Button(textos.getString("Cerrar"));
		botonCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		botonCerrar.setForeground(Color.WHITE);
		botonCerrar.setFont(new Font("Dialog", Font.BOLD, 18));
		botonCerrar.setBackground(new Color(165, 42, 42));
		botonCerrar.setBounds(37, 363, 323, 36);
		panel.add(botonCerrar);

		textAreaResultado = new JTextArea();
		textAreaResultado.setFont(new Font("Dialog", Font.BOLD, 18));
		textAreaResultado.setBounds(401, 66, 471, 333);
		textAreaResultado.setEditable(false);
		textAreaResultado.setLineWrap(true);
		textAreaResultado.setWrapStyleWord(true);
		panel.add(textAreaResultado);

		textAreaDirectorio = new JTextArea();
		textAreaDirectorio.setFont(new Font("Dialog", Font.BOLD, 18));
		textAreaDirectorio.setBounds(37, 155, 323, 71);
		textAreaDirectorio.setEditable(false);
		textAreaDirectorio.setLineWrap(true);
		textAreaDirectorio.setWrapStyleWord(true);
		textAreaDirectorio.setText("");
		panel.add(textAreaDirectorio);

		etiquetaResultado = new JLabel(textos.getString("Resultado"));
		etiquetaResultado.setFont(new Font("Dialog", Font.BOLD, 18));
		etiquetaResultado.setBounds(401, 14, 153, 36);
		panel.add(etiquetaResultado);

		comboBoxArchivos = new JComboBox();
		comboBoxArchivos.setFont(new Font("Dialog", Font.BOLD, 18));
		comboBoxArchivos.setBounds(37, 321, 323, 36);
		panel.add(comboBoxArchivos);

		etiquetaArchivos = new JLabel(textos.getString("Archivos"));
		etiquetaArchivos.setFont(new Font("Dialog", Font.BOLD, 18));
		etiquetaArchivos.setBounds(37, 274, 323, 36);
		panel.add(etiquetaArchivos);

		AL = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox combo = (JComboBox) e.getSource();
				String sel = (String) combo.getSelectedItem();
				String mensaje = Logica.contar_repetidos_archivo(textAreaDirectorio.getText() + "\\" + sel, textos);
				if (mensaje.equals(""))
					JOptionPane.showMessageDialog(getContentPane(), textos.getString("el_archivo_esta_vacio"),
							textos.getString("ERROR"), JOptionPane.PLAIN_MESSAGE);
				else
					JOptionPane.showMessageDialog(getContentPane(), mensaje, textos.getString("Resultado"),
							JOptionPane.PLAIN_MESSAGE);
			}
		};
	}

	private void cargarDirectorio() {
		String directorio = Logica.cargarDirectorio(frame);
		if (!directorio.equals("")) {
			textAreaDirectorio.setText(directorio);
			textAreaResultado.setText("");

			if (!textAreaDirectorio.getText().equals("")) {
				comboBoxArchivos.removeActionListener(AL);
				comboBoxArchivos.removeAllItems();
				cargarComboArchivos(Logica.cargarArchivos(textAreaDirectorio.getText()));
				comboBoxArchivos.addActionListener(AL);
			}
		}

	}

	private void cargarComboArchivos(PositionList<String> lista) {
		try {
			while (!lista.isEmpty()) {
				comboBoxArchivos.addItem(lista.remove(lista.last()));
			}

		} catch (InvalidPositionException | EmptyListException e) {
			e.printStackTrace();
		}
	}

	private void actualizar_idioma(int indice) {
		if (indice == 0) {
			textos = ResourceBundle.getBundle("locales/MessagesBundle", esLocale);
		}
		if (indice == 1) {
			textos = ResourceBundle.getBundle("locales/MessagesBundle", enLocale);
		}

		botonCerrar.setLabel(textos.getString("Cerrar"));
		botonCargar.setLabel(textos.getString("Cargar_Directorio"));
		botonAceptar.setLabel(textos.getString("Aceptar"));
		etiquetaIdioma.setText(textos.getString("Lenguaje"));
		etiquetaDirectorio.setText(textos.getString("Directorio"));
		etiquetaResultado.setText(textos.getString("Resultado"));
		etiquetaArchivos.setText(textos.getString("Archivos"));

		if (textAreaDirectorio.getText() != "" && !textAreaResultado.getText().equals("")) {
			aceptar();
		}
	}

	private void aceptar() {
		if (!textAreaDirectorio.getText().equals("")) {
			String mensaje = logica.contar_repetidos_directorio(textAreaDirectorio.getText(), textos);

			if (mensaje.equals(""))
				JOptionPane.showMessageDialog(getContentPane(), textos.getString("archivo_vacio"),
						textos.getString("ERROR"), JOptionPane.PLAIN_MESSAGE);

			textAreaResultado.setText(mensaje);
		} else {
			JOptionPane.showMessageDialog(getContentPane(), textos.getString("mensaje"), textos.getString("ERROR"),
					JOptionPane.PLAIN_MESSAGE);
		}
	}
}
