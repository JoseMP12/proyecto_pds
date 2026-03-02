package domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//Modificar el tablero?

public class Tablero {
	private List<ListaTareas> listas;
	
	public Tablero(ListaTareas... tareas) {
		this.listas = new ArrayList<ListaTareas>();
		for(ListaTareas l : tareas) {
			listas.add(l);
		}
	}
	
	public Tablero() {}
	
	public List<ListaTareas> getListas() {
		return Collections.unmodifiableList(listas);
	}
	
	public void setListas(List<ListaTareas> listas) {
		this.listas = listas;
	}
}
