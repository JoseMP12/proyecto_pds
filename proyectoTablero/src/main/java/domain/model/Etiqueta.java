package domain.model;

public class Etiqueta {
	private String cadena;
	private Color color;
	
	public Etiqueta(String cadena, Color color) {
		this.cadena = cadena;
		this.color = color;
	}
	
	public Etiqueta(String cadena) {
		this(cadena, Color.BLANCO);
	}
	
	public Etiqueta() {}
	
	public String getCadena() {
		return cadena;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setCadena(String cadena) {
		this.cadena = cadena;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
}
