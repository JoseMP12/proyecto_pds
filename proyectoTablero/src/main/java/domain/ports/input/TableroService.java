package domain.ports.input;

import java.util.List;

import domain.model.Tarjeta;
import domain.ports.input.commands.*;

public interface TableroService {

    String crearTablero(CrearTableroCommand cmd);

    void crearLista(CrearListaCommand cmd);

    void crearTarjeta(CrearTarjetaCommand cmd);

    void moverTarjeta(MoverTarjetaCommand cmd);

    void marcarTarjetaCompletada(MarcarTarjetaCompletadaCommand cmd);

    List<Tarjeta> filtrarPorEtiqueta(FiltrarPorEtiquetaCommand cmd);

    void bloquearTablero(BloquearTableroCommand cmd);

    List<String> obtenerHistorial(ObtenerHistorialCommand cmd);

    //Tablero obtenerTableroPorUrl(String url);
}
