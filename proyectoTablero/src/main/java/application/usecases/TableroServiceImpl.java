package application.usecases;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.model.Checklist;
import domain.model.ListaTareas;
import domain.model.Tablero;
import domain.model.Tarea;
import domain.model.Tarjeta;
import domain.model.Usuario;
import domain.ports.input.TableroService;
import domain.ports.input.commands.BloquearTableroCommand;
import domain.ports.input.commands.CrearListaCommand;
import domain.ports.input.commands.CrearTableroCommand;
import domain.ports.input.commands.CrearTarjetaCommand;
import domain.ports.input.commands.FiltrarPorEtiquetaCommand;
import domain.ports.input.commands.MarcarTarjetaCompletadaCommand;
import domain.ports.input.commands.MoverTarjetaCommand;
import domain.ports.input.commands.ObtenerHistorialCommand;
import domain.ports.output.TablerosRepository;

public class TableroServiceImpl implements TableroService {
	private static final Logger log = LoggerFactory.getLogger(TableroServiceImpl.class);
    private final TablerosRepository tableroPort;
    
    public TableroServiceImpl(TablerosRepository tableroRepository) {
        this.tableroPort = tableroRepository;
    }
	
    @Override
	public String crearTablero(CrearTableroCommand cmd) {
        Usuario usuario = new Usuario(cmd.correoUsuario());
    	String url = java.util.UUID.randomUUID().toString();
        Tablero tablero = new Tablero(usuario, url);
        tableroPort.save(tablero);
        log.debug("Tablero creado con URL {}", url);
        return url;
	}
	
    @Override
	public void crearLista(CrearListaCommand cmd) {
    	Tablero tablero = tableroPort.findByUrl(cmd.urlTablero())
                .orElseThrow();
    	ListaTareas lista = new ListaTareas(cmd.nombreLista(), cmd.maxTarjetas());
        tablero.addLista(lista);
        tableroPort.save(tablero);
        log.debug("Lista '{}' creada correctamente", cmd.nombreLista());
	}
	
    @Override
	public void crearTarjeta(CrearTarjetaCommand cmd) {
    	Tablero tablero = tableroPort.findByUrl(cmd.urlTablero())
                .orElseThrow();
        ListaTareas lista = tablero.findLista(cmd.idLista());
        Tarjeta tarjeta = cmd.isChecklist()
                ? new Checklist(cmd.titulo())
                : new Tarea(cmd.titulo());
        lista.addTarjeta(tarjeta);
        tableroPort.save(tablero);
        log.debug("Tarjeta '{}' creada correctamente", cmd.titulo());
	}
	
    @Override
    public void moverTarjeta(MoverTarjetaCommand cmd) {
        Tablero tablero = tableroPort.findByUrl(cmd.urlTablero())
			 	.orElseThrow(() -> new IllegalStateException("Tablero no encontrado"));
        tablero.moveTarjeta(cmd.idListaOrigen(), cmd.idListaDestino(), cmd.idTarjeta());
        tableroPort.save(tablero);
    }


	
    @Override
	public void marcarTarjetaCompletada(MarcarTarjetaCompletadaCommand cmd) {
    	Tablero tablero = tableroPort.findByUrl(cmd.urlTablero())
    			.orElseThrow(() -> new IllegalStateException("Tablero no encontrado"));
        ListaTareas origen = tablero.getListas().stream()
                .filter(l -> l.getTarjetas().stream()
                        .anyMatch(t -> t.getId().toString().equals(cmd.idTarjeta())))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Tarjeta no encontrada"));
        Tarjeta tarjeta = origen.getTarjetas().stream()
                .filter(t -> t.getId().toString().equals(cmd.idTarjeta()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Tarjeta no encontrada"));
        tarjeta.marcarCompletada();

        // Mover si hay lista de completadas
        if (tablero.getListaCompletadas() != null) {
            origen.removeTarjeta(tarjeta);
            tablero.getListaCompletadas().addTarjeta(tarjeta);
        }
        tablero.registrarAccion(new domain.model.Accion(
                "COMPLETAR_TARJETA",
                "Tarjeta " + tarjeta.getTitulo() + " marcada como completada"
        ));

        tableroPort.save(tablero);
        log.debug("Tarjeta {} marcada como completada", cmd.idTarjeta());
	}
	
    @Override
	public List<Tarjeta> filtrarPorEtiqueta(FiltrarPorEtiquetaCommand cmd) {
        log.info("Filtrando tarjetas por etiqueta '{}' en tablero {}", cmd.etiqueta(), cmd.urlTablero());
    	 Tablero tablero = tableroPort.findByUrl(cmd.urlTablero())
    			 	.orElseThrow(() -> new IllegalStateException("Tablero no encontrado"));
    	 return tablero.filtrarPorEtiqueta(cmd.etiqueta());
	}
	
    @Override
	public void bloquearTablero(BloquearTableroCommand cmd) {
    	log.info("Bloqueando tablero {}", cmd.urlTablero());

        Tablero tablero = tableroPort.findByUrl(cmd.urlTablero())
		 		.orElseThrow(() -> new IllegalStateException("Tablero no encontrado"));
        tablero.bloquear();
        tableroPort.save(tablero);
        log.debug("Tablero {} bloqueado correctamente", cmd.urlTablero());
	}
	
    @Override
	public List<String> obtenerHistorial(ObtenerHistorialCommand cmd) {
        log.info("Obteniendo historial del tablero {}", cmd.urlTablero());
    	Tablero tablero = tableroPort.findByUrl(cmd.urlTablero())
		 		.orElseThrow(() -> new IllegalStateException("Tablero no encontrado"));
        return tablero.getHistorial().stream()
                .map(a -> a.getFecha() + " - " + a.getDescripcion())
                .toList();
	}
}