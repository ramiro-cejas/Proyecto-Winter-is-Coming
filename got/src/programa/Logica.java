package programa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import Lista.DoubleLinkedList;
import Lista.PositionList;
import Mapeo.Entry;
import Mapeo.InvalidKeyException;
import Mapeo.Map;
import Mapeo.Mapeo_hash_abierto;
import PriorityQueue.Default_comparator;
import PriorityQueue.EmptyPriorityQueueException;
import PriorityQueue.Entrada;
import PriorityQueue.PriorityQ_heap;

public class Logica {

	public static String cargarDirectorio(GUI gui) {
		String retorno = "";

		JFileChooser directorio = new JFileChooser();
		directorio.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int seleccion = directorio.showOpenDialog(gui);
		File dir = directorio.getSelectedFile();
		if (seleccion == JFileChooser.APPROVE_OPTION) {

			retorno = directorio.getSelectedFile().toString();
		}
		return retorno;
	}

	public static PositionList<String> cargarArchivos(String directorio) {
		PositionList<String> retorno = new DoubleLinkedList<String>();

		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String fileName) {
				return fileName.endsWith("txt");
			}
		};

		File d = new File(directorio);
		String[] lista = d.list(filter);

		for (int i = 0; i < lista.length; i++) {
			retorno.addLast(lista[i]);
		}

		return retorno;
	}

	public String contar_repetidos_directorio(String ruta, ResourceBundle textos) {

		String retorno = "";
		Map<String, Integer> mapeo = new Mapeo_hash_abierto<String, Integer>();

		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String fileName) {
				return fileName.endsWith("txt");
			}
		};

		File d = new File(ruta);
		String[] lista = d.list(filter);

		File archivo = null;

		for (int i = 0; i < lista.length; i++) {
			archivo = new File(ruta + "\\" + lista[i]);
			cargarArchivo(archivo, mapeo);
		}

		Default_comparator default_comparator = new Default_comparator();
		PriorityQ_heap<Integer, String> cola = new PriorityQ_heap<Integer, String>(default_comparator);

		double cantidad_palabras = 0;

		// cargarCola
		try {

			for (Entry<String, Integer> entrada : mapeo.entries()) {
				cola.insert(entrada.getValue(), entrada.getKey());
				cantidad_palabras += entrada.getValue();
			}

		} catch (PriorityQueue.InvalidKeyException e1) {
			e1.printStackTrace();
		}

		// Vaciar primeros 5 de la cola
		Entrada<Integer, String> entrada = null;
		try {
			for (int i = 0; i < 5; i++) {
				DecimalFormat formato1 = new DecimalFormat("#.00");
				String porcentaje = "";
				entrada = cola.removeMin();

				porcentaje = formato1.format((entrada.getKey() * 100) / cantidad_palabras);

				retorno = retorno + textos.getString("La_palabra") + " '" + entrada.getValue() + "' "
						+ textos.getString("representa_un") + " " + porcentaje + "% "
						+ textos.getString("del_total") + "\n";
			}
		} catch (EmptyPriorityQueueException e) {
		} finally {
			return retorno;
		}
	}

	private static void cargarArchivo(File archivo, Map<String, Integer> mapeo) {

		String texto = "";
		String aux = "";

		try {
			if (archivo != null) {
				FileReader archivos = new FileReader(archivo);
				BufferedReader lee = new BufferedReader(archivos);
				while ((aux = lee.readLine()) != null) {
					texto += aux + " ";
				}
				lee.close();
				archivos.close();
			}
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		texto = texto.replace('.', ' ');

		String[] textoPartido = texto.split(" ");

		// Creo y cargo un mapeo con cada palabra y su cantidad de apariciones

		try {
			for (int i = 0; i < textoPartido.length; i++) {
				textoPartido[i] = textoPartido[i].toLowerCase();
				if (!textoPartido[i].equals("")) {
					if (mapeo.get(textoPartido[i]) == null) {
						mapeo.put(textoPartido[i], 1);
					} else {
						mapeo.put(textoPartido[i], mapeo.get(textoPartido[i]) + 1);
					}
				}

			}
		} catch (InvalidKeyException e1) {
			e1.printStackTrace();
		}
	}

	public static String contar_repetidos_archivo(String ruta, ResourceBundle textos) {
		String retorno = "";
		Map<String, Integer> mapeo = new Mapeo_hash_abierto<String, Integer>();
		cargarArchivo(new File(ruta), mapeo);

		Default_comparator default_comparator = new Default_comparator();
		PriorityQ_heap<Integer, String> cola = new PriorityQ_heap<Integer, String>(default_comparator);

		double cantidad_palabras = 0;

		// cargarCola
		try {

			for (Entry<String, Integer> entrada : mapeo.entries()) {
				cola.insert(entrada.getValue(), entrada.getKey());
				cantidad_palabras += entrada.getValue();
			}

		} catch (PriorityQueue.InvalidKeyException e1) {
			e1.printStackTrace();
		}

		// Vaciar primeros 5 de la cola
		Entrada<Integer, String> entrada = null;
		try {
			for (int i = 0; i < 5; i++) {
				DecimalFormat formato1 = new DecimalFormat("#.00");
				String porcentaje = "";
				entrada = cola.removeMin();

				porcentaje = formato1.format((entrada.getKey() * 100) / cantidad_palabras);

				retorno = retorno + textos.getString("La_palabra") + " '" + entrada.getValue() + "' "
						+ textos.getString("representa_un") + " " + porcentaje + "% "
						+ textos.getString("del_archivo") + "\n";
			}
		} catch (EmptyPriorityQueueException e) {
		} finally {
			return retorno;
		}

	}
}
