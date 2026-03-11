package domain.model;

public class Tarea extends Tarjeta {
	public Tarea(String titulo) {
		super(titulo);
	}
	
	@Override
    public boolean isChecklist() {
		return false;
	}
}
