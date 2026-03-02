package domain.model;

public abstract class Tarjeta {
	protected String titulo;
	protected Color color;
	protected Etiqueta etiqueta;
	protected EstadoTarjeta estado;
	
	public Tarjeta(String titulo, Color color, Etiqueta etiqueta, EstadoTarjeta estado) {
		this.titulo = titulo;
		this.color = color;
		this.etiqueta = etiqueta;
		this.estado = estado;
	}
	
	public Tarjeta(String titulo) {
		this(titulo, Color.BLANCO, new Etiqueta(), EstadoTarjeta.INCOMPLETA);
	}
	
	public String getTitulo() {
		return titulo;
	}
	
	public Color getColor() {
		return color;
	}
	
	public Etiqueta getEtiqueta() {
		return etiqueta;
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
	
	public void setEtiqueta(Etiqueta etiqueta) {
		this.etiqueta = etiqueta;
	}
	
	public void setEstado(EstadoTarjeta estado) {
		this.estado = estado;
	}
	
	public boolean isCompletada() {
		return estado.equals(EstadoTarjeta.COMPLETADA);
	}
	
	/*public addEtiqueta(Etiqueta etiqueta) {
		etiquetas.add(etiqueta);
	}*/
}
