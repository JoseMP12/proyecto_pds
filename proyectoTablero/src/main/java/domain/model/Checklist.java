package domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Checklist extends Tarjeta {
	private List<ItemChecklist> items;
	
	public Checklist(String titulo) {
		super(titulo);
		this.items = new ArrayList<>();
	}
	
	@Override
    public boolean isChecklist() {
		return true;
	}
	
	public void addItem(String descripcion) {
        items.add(new ItemChecklist(descripcion));
    }

    public List<ItemChecklist> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void marcarItem(int index) {
        items.get(index).marcarCompletado();
    }

    public boolean isCompletado() {
        return items.stream().allMatch(ItemChecklist::isCompletado);
    }
}
