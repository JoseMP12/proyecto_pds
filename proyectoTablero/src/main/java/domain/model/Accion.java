package domain.model;

import java.time.LocalDateTime;

public class Accion {
	// TODO: necesita id
    private String tipo;
    private String descripcion;
    private LocalDateTime fecha;

    public Accion(String tipo, String descripcion) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = LocalDateTime.now();
    }

    public String getTipo() {
    	return tipo;
    }
    
    public String getDescripcion() {
    	return descripcion;
    }
    
    public LocalDateTime getFecha() {
    	return fecha;
    }
}
