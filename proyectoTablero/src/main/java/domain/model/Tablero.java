package domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Tablero {
	private UUID id;
	private String urlUnica;
    private Usuario propietario;
    private EstadoTablero estado;
	private List<ListaTareas> listas;
    private ListaTareas listaCompletadas;
    private List<Accion> historial;
	
	public Tablero(Usuario propietario, String url) {
	    this.id = UUID.randomUUID();
        this.propietario = propietario;
		this.listas = new ArrayList<>();
        this.estado = EstadoTablero.ACCESIBLE;
        this.urlUnica = url;
        this.historial = new ArrayList<>();
	}
	
	public UUID getId() {
	    return id;
	}
		
	public List<ListaTareas> getListas() {
		return Collections.unmodifiableList(listas);
	}
	
	public void addLista(ListaTareas lista) {
        listas.add(lista);
	}
	
	public Usuario getPropietario() {
		return propietario;
	}
	
    public String getUrlUnica() {
    	return urlUnica;
    }
	
	public EstadoTablero getEstado() {
		return estado;
	}
	
    public ListaTareas getListaCompletadas() {
    	return listaCompletadas;
    }
    
    public List<Accion> getHistorial() {
        return Collections.unmodifiableList(historial);
    }
    
    public void registrarAccion(Accion accion) {
        historial.add(accion);
    }
	
	public void bloquear() {
        this.estado = EstadoTablero.BLOQUEADO;
        registrarAccion(new Accion("BLOQUEAR_TABLERO", "El tablero ha sido bloqueado"));
	}
	
    public void desbloquear() {
        this.estado = EstadoTablero.ACCESIBLE;
        registrarAccion(new Accion("DESBLOQUEAR_TABLERO", "El tablero ha sido desbloqueado"));
    }
    
    public void moveTarjeta(String idListaOrigen, String idListaDestino, String idTarjeta) {
        ListaTareas origen = findLista(idListaOrigen);
        ListaTareas destino = findLista(idListaDestino);
        Tarjeta tarjeta = origen.getTarjetas().stream()
                .filter(t -> t.getId().toString().equals(idTarjeta))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Tarjeta no encontrada en la lista origen"));
        // Validación 1
        if (!destino.getListasObligatorias().isEmpty() &&
            !destino.getListasObligatorias().contains(origen.getNombre())) {
            throw new IllegalStateException("La tarjeta no ha pasado por las listas obligatorias");
        }
     // Validación 2
        if (destino.getTarjetas().size() >= destino.getMaxTarjetas()) {
            throw new IllegalStateException("La lista destino está llena");
        }

        origen.removeTarjeta(tarjeta);
        destino.addTarjeta(tarjeta);

        registrarAccion(new Accion("MOVER_TARJETA",
                "Tarjeta movida de " + origen.getNombre() + " a " + destino.getNombre()));
    }
    
    public List<Tarjeta> filtrarPorEtiqueta(String etiqueta) {
        List<Tarjeta> resultado = new ArrayList<>();
        for (ListaTareas lista : listas) {
            resultado.addAll(lista.filtrarPorEtiqueta(etiqueta));
        }
        return resultado;
    }
    
    public void configurarListaCompletadas(ListaTareas lista) {
        this.listaCompletadas = lista;
    }
    
    public ListaTareas findLista(String idLista) {
        return listas.stream()
            .filter(l -> l.getId().toString().equals(idLista))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Lista no encontrada: " + idLista));
    }
}
