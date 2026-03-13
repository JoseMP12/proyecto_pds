package domain.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ListaTareasTest {

    @Test
    void listaSeCreaConValoresInicialesCorrectos() {
        ListaTareas lista = new ListaTareas("Pendientes", 5);

        assertEquals("Pendientes", lista.getNombre());
        assertEquals(5, lista.getMaxTarjetas());
        assertTrue(lista.getTarjetas().isEmpty());
        assertTrue(lista.getListasObligatorias().isEmpty());
    }

    @Test
    void addTarjetaAñadeCorrectamente() {
        ListaTareas lista = new ListaTareas("Pendientes", 5);
        Tarjeta t = new Tarea("Tarea X");

        lista.addTarjeta(t);

        assertEquals(1, lista.getTarjetas().size());
        assertEquals(t, lista.getTarjetas().get(0));
    }

    @Test
    void addTarjetaFallaSiLaListaEstaLlena() {
        ListaTareas lista = new ListaTareas("Pendientes", 0);
        Tarjeta t = new Tarea("Tarea X");

        assertThrows(IllegalStateException.class, () -> lista.addTarjeta(t));
    }

    @Test
    void removeTarjetaEliminaCorrectamente() {
        ListaTareas lista = new ListaTareas("Pendientes", 5);
        Tarjeta t = new Tarea("Tarea X");

        lista.addTarjeta(t);
        lista.removeTarjeta(t);

        assertTrue(lista.getTarjetas().isEmpty());
    }

    @Test
    void añadirListaPreviaAñadeCorrectamente() {
        ListaTareas lista = new ListaTareas("Revisión", 5);

        lista.añadirListaPrevia("En proceso");

        assertEquals(1, lista.getListasObligatorias().size());
        assertEquals("En proceso", lista.getListasObligatorias().get(0));
    }

    @Test
    void getTarjetasDevuelveListaInmodificable() {
        ListaTareas lista = new ListaTareas("Pendientes", 5);
        Tarjeta t = new Tarea("Tarea X");
        lista.addTarjeta(t);

        var tarjetas = lista.getTarjetas();

        assertThrows(UnsupportedOperationException.class, () -> tarjetas.add(new Tarea("Otra")));
    }

    @Test
    void getListasObligatoriasDevuelveListaInmodificable() {
        ListaTareas lista = new ListaTareas("Revisión", 5);
        lista.añadirListaPrevia("En proceso");

        var obligatorias = lista.getListasObligatorias();

        assertThrows(UnsupportedOperationException.class, () -> obligatorias.add("Pendientes"));
    }

    @Test
    void filtrarPorEtiquetaDevuelveSoloLasCoincidentes() {
        ListaTareas lista = new ListaTareas("Pendientes", 5);

        Tarjeta t1 = new Tarea("Tarea 1");
        Tarjeta t2 = new Tarea("Tarea 2");

        t1.addEtiqueta(new Etiqueta("bug", Color.ROJO));
        t2.addEtiqueta(new Etiqueta("feature", Color.VERDE));

        lista.addTarjeta(t1);
        lista.addTarjeta(t2);

        var filtradas = lista.filtrarPorEtiqueta("bug");

        assertEquals(1, filtradas.size());
        assertEquals(t1, filtradas.get(0));
    }

    @Test
    void filtrarPorEtiquetaDevuelveListaVaciaSiNoHayCoincidencias() {
        ListaTareas lista = new ListaTareas("Pendientes", 5);

        Tarjeta t1 = new Tarea("Tarea 1");
        t1.addEtiqueta(new Etiqueta("bug", Color.ROJO));

        lista.addTarjeta(t1);

        var filtradas = lista.filtrarPorEtiqueta("feature");

        assertTrue(filtradas.isEmpty());
    }
}