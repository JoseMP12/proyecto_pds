package domain.model;

public class Usuario {
	private final String correo;
	
	public Usuario(String correo) {
		this.correo = correo;
	}
	
	public String getCorreo() {
		return correo;
	}
}
