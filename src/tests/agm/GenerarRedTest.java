package tests.agm;

import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

import model.GenerarRedModel;
import model.entities.AGM;
import model.entities.Conexion;
import model.entities.Grafo;
import model.entities.Localidad;
import model.strategy.IArbolGeneradorMinimo;
import model.strategy.Prim;

public class GenerarRedTest {

    private GenerarRedModel generarRed;
    private Grafo agmPrueba;
    private Localidad loc1, loc2, loc3, loc4;

    @Before
    public void setUp() {
        IArbolGeneradorMinimo arbolGenerador = new Prim();
        AGM agm = new AGM();
        agm.setAGM(arbolGenerador);
        
        this.generarRed = new GenerarRedModel(agm);
        this.agmPrueba = new Grafo();
        
        this.loc1 = new Localidad("Buenos Aires", "Buenos Aires", -34.6, -58.4);
        this.loc2 = new Localidad("Córdoba", "Córdoba", -31.4, -64.2);
        this.loc3 = new Localidad("Mendoza", "Mendoza", -32.9, -68.9);
        this.loc4 = new Localidad("La Plata", "Buenos Aires", -34.9, -57.95);
    }

    @Test(expected = IllegalArgumentException.class)
    public void lanzarExcepcionSiLocalidadesEsNull() {
    	generarRed.generarRed(100, 0.1, 50, null);
    }
    
    @Test
    public void validarGrafoNulo() {
        assertFalse(generarRed.verificarValidezDeLaSolucion(null));
    }

    @Test
    public void validarGrafoVacio() {
        assertFalse(generarRed.verificarValidezDeLaSolucion(agmPrueba));
    }

    @Test
    public void validarNodoSolitario() {
        agmPrueba.agregarLocalidad(loc1);
        assertTrue(generarRed.verificarValidezDeLaSolucion(agmPrueba));
    }

    @Test
    public void validarArbolEstandar() {
        agmPrueba.agregarLocalidad(loc1);
        agmPrueba.agregarLocalidad(loc2);
        agmPrueba.agregarLocalidad(loc3);
        agmPrueba.agregarConexion(new Conexion(loc1, loc2, 100));
        agmPrueba.agregarConexion(new Conexion(loc2, loc3, 150));

        assertTrue(generarRed.verificarValidezDeLaSolucion(agmPrueba));
    }

    @Test
    public void detectarCicloEnGrafo() {
        agmPrueba.agregarLocalidad(loc1);
        agmPrueba.agregarLocalidad(loc2);
        agmPrueba.agregarLocalidad(loc3);
        // 3 nodos con 3 aristas = Ciclo
        agmPrueba.agregarConexion(new Conexion(loc1, loc2, 100));
        agmPrueba.agregarConexion(new Conexion(loc2, loc3, 150));
        agmPrueba.agregarConexion(new Conexion(loc1, loc3, 200));

        assertFalse(generarRed.verificarValidezDeLaSolucion(agmPrueba));
    }

    @Test
    public void detectarGrafoDesconectado() {
        agmPrueba.agregarLocalidad(loc1);
        agmPrueba.agregarLocalidad(loc2);
        agmPrueba.agregarLocalidad(loc3);
        agmPrueba.agregarLocalidad(loc4);
        // Dos islas separadas
        agmPrueba.agregarConexion(new Conexion(loc1, loc2, 100));
        agmPrueba.agregarConexion(new Conexion(loc3, loc4, 150));

        assertFalse(generarRed.verificarValidezDeLaSolucion(agmPrueba));
    }

    @Test
    public void repararArbolConAlternativaMinima() {
        agmPrueba.agregarLocalidad(loc1);
        agmPrueba.agregarLocalidad(loc2);
        agmPrueba.agregarLocalidad(loc3);

        Conexion c1 = new Conexion(loc1, loc2, 100);
        Conexion c2 = new Conexion(loc2, loc3, 150);
        agmPrueba.agregarConexion(c1);
        agmPrueba.agregarConexion(c2);

        Grafo reparado = generarRed.repararArbolAlEliminar(c1, agmPrueba);
        assertNotNull(reparado);
        assertTrue(generarRed.verificarValidezDeLaSolucion(reparado));
    }

    @Test
    public void repararConParametrosNulos() {
        assertNull(generarRed.repararArbolAlEliminar(new Conexion(loc1, loc2, 10), null));
        
        agmPrueba.agregarLocalidad(loc1);
        assertEquals(agmPrueba, generarRed.repararArbolAlEliminar(null, agmPrueba));
    }

    @Test
    public void encontrarConexionMasCaraDelCiclo() {
        agmPrueba.agregarLocalidad(loc1);
        agmPrueba.agregarLocalidad(loc2);
        agmPrueba.agregarLocalidad(loc3);

        Conexion barata = new Conexion(loc1, loc2, 50);
        Conexion cara = new Conexion(loc2, loc3, 200);
        agmPrueba.agregarConexion(barata);
        agmPrueba.agregarConexion(cara);

        Conexion nueva = new Conexion(loc1, loc3, 100);
        Conexion resultado = generarRed.obtenerConexionMasCaraDelCiclo(nueva, agmPrueba);
        
        assertNotNull(resultado);
        assertEquals(200.0, resultado.getCosto(), 0.001);
    }


    @Test
    public void generarRedDosNodos() {
        Map<String, Localidad> locales = new HashMap<>();
        locales.put("BA", loc1);
        locales.put("CBA", loc2);

        Grafo res = generarRed.generarRed(100, 0.0, 0, locales);

        assertEquals(2, res.getLocalidades().size());
        assertEquals(1, res.getTodasLasConexiones().size());
        assertTrue(generarRed.verificarValidezDeLaSolucion(res));
    }

    @Test
    public void verificarEstructuraArbolCompleto() {
        Map<String, Localidad> locales = new HashMap<>();
        locales.put("L1", loc1);
        locales.put("L2", loc2);
        locales.put("L3", loc3);
        locales.put("L4", loc4);

        Grafo res = generarRed.generarRed(100, 0.5, 20, locales);

        int n = res.getLocalidades().size();
        assertEquals(4, n);
        assertEquals(n - 1, res.getTodasLasConexiones().size());
    }
}