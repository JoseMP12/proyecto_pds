package domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//Añadir Tarjeta
//Eliminar Tarjeta
//Modificar Tarjeta
//Mover Tarjetas

public class ListaTareas {
	private List<Tarjeta> tarjetas;
	private Color color;
	
	public ListaTareas(Tarjeta...tarjeta) {
		this.tarjetas = new ArrayList<Tarjeta>();
		for(Tarjeta t : tarjetas) {
			tarjetas.add(t);
		}
	}
	
	public List<Tarjeta> getTarjetas() {
		return Collections.unmodifiableList(tarjetas);
	}
	
	public void setTarjetas(List<Tarjeta> tarjetas) {
		this.tarjetas = tarjetas;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
}
