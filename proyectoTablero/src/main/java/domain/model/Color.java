package domain.model;

public enum Color {
	ROJO("F76C4D"),
	VINO("B30944"),
	GRANATE("8F133E"),
	VERDE("92CF6D"),
	AZUL("6DCFCF"),
	AZUL_OCEANO("6DA1CF"),
	TURQUESA("6DCFC4"),
	LILA("A184D7"),
	LILA_PALO("D784D4"),
	MORADO("A10389"),
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
