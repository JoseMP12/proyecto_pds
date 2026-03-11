package domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

//Modificar Tarjeta

public class ListaTareas {
	private UUID id;
	private String nombre;
	private List<Tarjeta> tarjetas;
	private Color color;
	/// Reglas a nivel de lista:
	/// - una lista no puede tener más de N items (configurable)
	/// - una lista puede definir que una tarjeta tiene que haber pasado por otras listas antes de llegar a ella
    private int maxTarjetas;
    private List<String> listasObligatorias;
	
	public ListaTareas(String nombre, int maximo) {
		this.id = UUID.randomUUID();
        this.nombre = nombre;
	    this.tarjetas = new ArrayList<>();
	    this.maxTarjetas = maximo;
        this.listasObligatorias = new ArrayList<>();
	}
	
	public UUID getId() {
		return id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public List<Tarjeta> getTarjetas() {
		return Collections.unmodifiableList(tarjetas);
	}
	
	public int getMaxTarjetas() {
	    return maxTarjetas;
	}
	
	public void addTarjeta(Tarjeta tarjeta) {
        if (tarjetas.size() >= maxTarjetas) {
            throw new IllegalStateException("La lista ha alcanzado el máximo de tarjetas permitido.");
        }
        tarjetas.add(tarjeta);
    }
	
	public void removeTarjeta(Tarjeta tarjeta) {
        tarjetas.remove(tarjeta);
    }
	
	public List<String> getListasObligatorias() {
        return Collections.unmodifiableList(listasObligatorias);
    }
	
	public void añadirListaPrevia(String nombreLista) {
        listasObligatorias.add(nombreLista);
    }
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public List<Tarjeta> filtrarPorEtiqueta(String etiqueta) {
        List<Tarjeta> resultado = new ArrayList<>();
        for (Tarjeta t : tarjetas) {
            for (Etiqueta e : t.getEtiquetas()) {
                if (e.getCadena().equalsIgnoreCase(etiqueta)) {
                    resultado.add(t);
                    break;
                }
            }
        }
        return resultado;
    }
}
