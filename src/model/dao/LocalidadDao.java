package model.dao;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import model.entities.Localidad;

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

		try (FileWriter writer = new FileWriter(nombreArchivo)) {
			writer.write(json);
			writer.close();
		} catch (Exception e) {
			System.err.println("No se pudo generar el archivo JSON, error:" + e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	public static LinkedHashMap<String, Localidad> cargarDesdeJson(String nombreArchivo) {
		if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre del archivo no puede ser nulo o vacío.");
		}

		Gson gson = new Gson();
		try (Reader reader = new FileReader(nombreArchivo)) {
			Type localidadesType = new TypeToken<Map<String, Localidad>>() {
			}.getType();
			LinkedHashMap<String, Localidad> localidades = gson.fromJson(reader, localidadesType);
			reader.close();
			return localidades;
		} catch (Exception e) {
			System.err.println("No se pudo cargar los datos del archivo JSON, error:" + e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	public static void limpiarArchivoJson(String nombreArchivo) {
		if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre del archivo no puede ser nulo o vacío.");
		}
		try (FileOutputStream stream = new FileOutputStream(nombreArchivo)) {
			stream.close();
		} catch (Exception e) {
			System.err.println("No se pudo limpiar el archivo JSON, error:" + e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}
}
