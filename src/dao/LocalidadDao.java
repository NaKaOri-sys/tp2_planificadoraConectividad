package dao;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import model.Localidad;

public class LocalidadDao {
	public static final String FILE_PATH = "localidades.json";
	public static void generarJsonLocalidad(String nombreArchivo, Map<String, Localidad> localidades)
			throws IllegalArgumentException {
		if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre del archivo no puede ser nulo o vacío.");
		}

		if (localidades == null || localidades.isEmpty()) {
			throw new IllegalArgumentException("No hay localidades para generar el JSON.");
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(localidades);

		try {
			FileWriter writer = new FileWriter(nombreArchivo);
			writer.write(json);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static LinkedHashMap<String, Localidad> cargarDesdeJson(String nombreArchivo) {
		if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre del archivo no puede ser nulo o vacío.");
		}
		Gson gson = new Gson();
		try {
			Reader reader = new FileReader(nombreArchivo);
			Type localidadesType = new TypeToken<Map<String, Localidad>>() {
			}.getType();
			LinkedHashMap<String, Localidad> localidades = gson.fromJson(reader, localidadesType);
			reader.close();
			return localidades;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void eliminarArchivoJson(String nombreArchivo) {
		if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre del archivo no puede ser nulo o vacío.");
		}
		try {
			FileWriter writer = new FileWriter(nombreArchivo);
			writer.write("{}");
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
