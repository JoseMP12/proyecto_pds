package domain.model;

public class Checklist extends Tarjeta {
	//private List<ItemChecklist> items;
	
	public Checklist(String titulo) {
		super(titulo);
	}
	
	@Override
    public boolean isChecklist() {
		return true;
	}
}
