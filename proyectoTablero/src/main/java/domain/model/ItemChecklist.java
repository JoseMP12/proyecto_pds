package domain.model;

public class ItemChecklist {
    private final String descripcion;
    private boolean completado;

    public ItemChecklist(String descripcion) {
        this.descripcion = descripcion;
        this.completado = false;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void marcarCompletado() {
        this.completado = true;
    }
}