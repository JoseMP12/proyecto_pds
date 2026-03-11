package domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public abstract class Tarjeta {
	private UUID id;
	protected String titulo;
	protected Color color;
	private List<Etiqueta> etiquetas;
	protected EstadoTarjeta estado;
	
	protected Tarjeta(String titulo) {
		this.id = UUID.randomUUID();
		this.titulo = titulo;
		this.color = Color.BLANCO;
		this.etiquetas = new ArrayList<>();
		this.estado = EstadoTarjeta.INCOMPLETA;
	}
	
	public UUID getId() {
		return id;
	}
	
	public String getTitulo() {
		return titulo;
	}
	
	public Color getColor() {
		return color;
	}
	
	public List<Etiqueta> getEtiquetas() {
		return Collections.unmodifiableList(etiquetas);
	}
	
	public EstadoTarjeta getEstado() {
		return estado;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public boolean isCompletada() {
		return estado.equals(EstadoTarjeta.COMPLETADA);
	}
	
	public void marcarCompletada() {
        this.estado = EstadoTarjeta.COMPLETADA;
    }
	
	public void addEtiqueta(Etiqueta etiqueta) {
		etiquetas.add(etiqueta);
	}
	
    public abstract boolean isChecklist();
}
