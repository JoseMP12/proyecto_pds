package domain.model;

//Añadir más colores
public enum Color {
	ROJO("F76C4D"),
	GRANATE("B30944"),
	VERDE("92CF6D"),
	AZUL("6DCFCF"),
	AZUL_OCEANO("6DA1CF"),
	LILA("A184D7"),
	ROSA("FC74F0"),
	ROSADO("FC74A8"),
	DESERTICO("FC8374"),
	AMARILLO("FFFF8A"),
	BLANCO("FFFFFF");
	
	private final String color;

	Color(String codigoColor) {
		this.color = codigoColor;
	}
	
	public String getColor() {
		return color;
	}
}
