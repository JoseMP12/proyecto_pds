package domain.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TarjetaTest {

    // ---------------------------------------------------------
    //   TESTS COMUNES A TODAS LAS TARJETAS (HEREDADOS)
    // ---------------------------------------------------------

    @Test
    void tarjetaSeCreaConValoresInicialesCorrectos() {
        Tarjeta t = new Tarea("Mi tarea");

        assertNotNull(t.getId());
        assertEquals("Mi tarea", t.getTitulo());
        assertEquals(Color.BLANCO, t.getColor());
        assertEquals(EstadoTarjeta.INCOMPLETA, t.getEstado());
        assertTrue(t.getEtiquetas().isEmpty());
    }

    @Test
    void setTituloModificaElTitulo() {
        Tarjeta t = new Tarea("Inicial");
        t.setTitulo("Modificado");

        assertEquals("Modificado", t.getTitulo());
    }

    @Test
    void setColorModificaElColor() {
        Tarjeta t = new Tarea("Tarea");
        t.setColor(Color.ROJO);

        assertEquals(Color.ROJO, t.getColor());
    }

    @Test
    void marcarCompletadaCambiaElEstado() {
        Tarjeta t = new Tarea("Tarea");

        t.marcarCompletada();

        assertEquals(EstadoTarjeta.COMPLETADA, t.getEstado());
        assertTrue(t.isCompletada());
    }

    @Test
    void addEtiquetaAñadeCorrectamente() {
        Tarjeta t = new Tarea("Tarea");
        Etiqueta e = new Etiqueta("bug", Color.ROJO);

        t.addEtiqueta(e);

        assertEquals(1, t.getEtiquetas().size());
        assertEquals(e, t.getEtiquetas().get(0));
    }

    @Test
    void getEtiquetasDevuelveListaInmodificable() {
        Tarjeta t = new Tarea("Tarea");
        t.addEtiqueta(new Etiqueta("bug", Color.ROJO));

        var etiquetas = t.getEtiquetas();

        assertThrows(UnsupportedOperationException.class, () ->
            etiquetas.add(new Etiqueta("feature", Color.VERDE))
        );
    }

    // ---------------------------------------------------------
    //   TESTS ESPECÍFICOS DE TAREA
    // ---------------------------------------------------------

    @Test
    void tareaIsChecklistDevuelveFalse() {
        Tarjeta t = new Tarea("Mi tarea");

        assertFalse(t.isChecklist());
    }

    // ---------------------------------------------------------
    //   TESTS ESPECÍFICOS DE CHECKLIST
    // ---------------------------------------------------------

    @Test
    void checklistIsChecklistDevuelveTrue() {
        Tarjeta c = new Checklist("Mi checklist");

        assertTrue(c.isChecklist());
    }

    @Test
    void checklistHeredaEstadoInicialCorrecto() {
        Tarjeta c = new Checklist("Checklist X");

        assertEquals("Checklist X", c.getTitulo());
        assertEquals(Color.BLANCO, c.getColor());
        assertEquals(EstadoTarjeta.INCOMPLETA, c.getEstado());
        assertTrue(c.getEtiquetas().isEmpty());
    }

    // ---------------------------------------------------------
    //   TESTS DE ÍTEMS DEL CHECKLIST (cuando los añadas)
    // ---------------------------------------------------------

    @Test
    void checklistPuedeAñadirItems() {
        Checklist c = new Checklist("Mi checklist");
        c.addItem("Primero");

        assertEquals(1, c.getItems().size());
        assertEquals("Primero", c.getItems().get(0).getDescripcion());
        assertFalse(c.getItems().get(0).isCompletado());
    }

    @Test
    void checklistPuedeMarcarItems() {
        Checklist c = new Checklist("Mi checklist");
        c.addItem("Primero");

        c.marcarItem(0);

        assertTrue(c.getItems().get(0).isCompletado());
    }

    @Test
    void checklistEstaCompletadoCuandoTodosLosItemsLoEstan() {
        Checklist c = new Checklist("Mi checklist");
        c.addItem("A");
        c.addItem("B");

        c.marcarItem(0);
        c.marcarItem(1);

        assertTrue(c.isCompletado());
    }

    @Test
    void checklistNoEstaCompletadoSiAlgúnItemNoLoEstá() {
        Checklist c = new Checklist("Mi checklist");
        c.addItem("A");
        c.addItem("B");

        c.marcarItem(0);

        assertFalse(c.isCompletado());
    }
}
