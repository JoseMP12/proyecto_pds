package domain.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TableroTest {

    @Test
    void tableroSeCreaConEstadoInicialCorrecto() {
        Tablero t = new Tablero(new Usuario("a@a.com"), "url123");

        assertEquals(EstadoTablero.ACCESIBLE, t.getEstado());
        assertTrue(t.getListas().isEmpty());
        assertTrue(t.getHistorial().isEmpty());
        assertEquals("url123", t.getUrlUnica());
    }

    @Test
    void addListaAñadeUnaLista() {
        Tablero t = new Tablero(new Usuario("a@a.com"), "url");
        ListaTareas l = new ListaTareas("Pendientes", 5);

        t.addLista(l);

        assertEquals(1, t.getListas().size());
        assertEquals("Pendientes", t.getListas().get(0).getNombre());
    }

    @Test
    void findListaDevuelveLaListaCorrecta() {
        Tablero t = new Tablero(new Usuario("a@a.com"), "url");
        ListaTareas l = new ListaTareas("Pendientes", 5);
        t.addLista(l);

        ListaTareas encontrada = t.findLista(l.getId().toString());

        assertEquals(l, encontrada);
    }

    @Test
    void findListaLanzaExcepcionSiNoExiste() {
        Tablero t = new Tablero(new Usuario("a@a.com"), "url");

        assertThrows(IllegalStateException.class, () ->
            t.findLista("id-inexistente")
        );
    }

    @Test
    void moveTarjetaMueveCorrectamente() {
        Tablero t = new Tablero(new Usuario("a@a.com"), "url");

        ListaTareas origen = new ListaTareas("Pendientes", 5);
        ListaTareas destino = new ListaTareas("En proceso", 5);

        t.addLista(origen);
        t.addLista(destino);

        Tarjeta tarjeta = new Tarea("Tarea X");
        origen.addTarjeta(tarjeta);

        t.moveTarjeta(origen.getId().toString(), destino.getId().toString(), tarjeta.getId().toString());

        assertTrue(destino.getTarjetas().contains(tarjeta));
        assertFalse(origen.getTarjetas().contains(tarjeta));
        assertEquals("MOVER_TARJETA", t.getHistorial().get(0).getTipo());
    }

    @Test
    void moveTarjetaFallaSiDestinoLleno() {
        Tablero t = new Tablero(new Usuario("a@a.com"), "url");

        ListaTareas origen = new ListaTareas("Pendientes", 5);
        ListaTareas destino = new ListaTareas("Hecho", 0);

        t.addLista(origen);
        t.addLista(destino);

        Tarjeta tarjeta = new Tarea("Tarea X");
        origen.addTarjeta(tarjeta);

        assertThrows(IllegalStateException.class, () ->
            t.moveTarjeta(origen.getId().toString(), destino.getId().toString(), tarjeta.getId().toString())
        );
    }

    @Test
    void moveTarjetaFallaSiNoCumpleListasObligatorias() {
        Tablero t = new Tablero(new Usuario("a@a.com"), "url");

        ListaTareas origen = new ListaTareas("Pendientes", 5);
        ListaTareas destino = new ListaTareas("Revisión", 5);
        destino.añadirListaPrevia("En proceso");

        t.addLista(origen);
        t.addLista(destino);

        Tarjeta tarjeta = new Tarea("Tarea X");
        origen.addTarjeta(tarjeta);

        assertThrows(IllegalStateException.class, () ->
            t.moveTarjeta(origen.getId().toString(), destino.getId().toString(), tarjeta.getId().toString())
        );
    }

    @Test
    void bloquearYDesbloquearRegistraAcciones() {
        Tablero t = new Tablero(new Usuario("a@a.com"), "url");

        t.bloquear();
        assertEquals(EstadoTablero.BLOQUEADO, t.getEstado());
        assertEquals("BLOQUEAR_TABLERO", t.getHistorial().get(0).getTipo());

        t.desbloquear();
        assertEquals(EstadoTablero.ACCESIBLE, t.getEstado());
        assertEquals("DESBLOQUEAR_TABLERO", t.getHistorial().get(1).getTipo());
    }

    @Test
    void filtrarPorEtiquetaDevuelveTarjetasCorrectas() {
        Tablero t = new Tablero(new Usuario("a@a.com"), "url");

        ListaTareas l = new ListaTareas("Pendientes", 5);
        t.addLista(l);

        Tarjeta t1 = new Tarea("Tarea 1");
        Tarjeta t2 = new Tarea("Tarea 2");

        t1.addEtiqueta(new Etiqueta("bug", Color.ROJO));
        t2.addEtiqueta(new Etiqueta("feature", Color.VERDE));

        l.addTarjeta(t1);
        l.addTarjeta(t2);

        var filtradas = t.filtrarPorEtiqueta("bug");

        assertEquals(1, filtradas.size());
        assertEquals(t1, filtradas.get(0));
    }
}