package application.usecases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import adapters.memory.TableroRepositoryInMemory;
import domain.model.Tablero;
import domain.model.Tarjeta;
import domain.ports.input.commands.CrearListaCommand;
import domain.ports.input.commands.CrearTableroCommand;
import domain.ports.input.commands.CrearTarjetaCommand;
import domain.ports.input.commands.MarcarTarjetaCompletadaCommand;
import domain.ports.output.TablerosRepository;

class MarcarTarjetaCompletadaServiceTest {

    private TablerosRepository repo;
    private TableroServiceImpl service;

    @BeforeEach
    void setUp() {
        repo = new TableroRepositoryInMemory();
        service = new TableroServiceImpl(repo);
    }

    @Test
    void marcarTarjetaComoCompletadaLaMueveALaListaCompletadas() {
        String url = service.crearTablero(new CrearTableroCommand("usuario@test.com"));

        service.crearLista(new CrearListaCommand(url, "Pendientes", 5));
        service.crearLista(new CrearListaCommand(url, "Completadas", 10));

        Tablero tablero = repo.findByUrl(url).orElseThrow();
        String idPendientes = tablero.getListas().get(0).getId().toString();

        tablero.configurarListaCompletadas(tablero.getListas().get(1));
        repo.save(tablero);

        service.crearTarjeta(new CrearTarjetaCommand(url, idPendientes, "Tarea X", false));

        tablero = repo.findByUrl(url).orElseThrow();
        Tarjeta tarjeta = tablero.getListas().get(0).getTarjetas().get(0);

        service.marcarTarjetaCompletada(
                new MarcarTarjetaCompletadaCommand(url, tarjeta.getId().toString())
        );

        tablero = repo.findByUrl(url).orElseThrow();

        assertTrue(tarjeta.isCompletada());
        assertTrue(tablero.getListaCompletadas().getTarjetas().contains(tarjeta));
        assertTrue(tablero.getListas().get(0).getTarjetas().isEmpty());

        assertFalse(tablero.getHistorial().isEmpty());
        assertEquals("COMPLETAR_TARJETA", tablero.getHistorial().get(0).getTipo());
    }

    @Test
    void marcarTarjetaComoCompletadaSinListaCompletadasNoLaMueve() {
        String url = service.crearTablero(new CrearTableroCommand("usuario@test.com"));

        service.crearLista(new CrearListaCommand(url, "Pendientes", 5));

        Tablero tablero = repo.findByUrl(url).orElseThrow();
        String idPendientes = tablero.getListas().get(0).getId().toString();

        service.crearTarjeta(new CrearTarjetaCommand(url, idPendientes, "Tarea X", false));

        tablero = repo.findByUrl(url).orElseThrow();
        Tarjeta tarjeta = tablero.getListas().get(0).getTarjetas().get(0);

        service.marcarTarjetaCompletada(
                new MarcarTarjetaCompletadaCommand(url, tarjeta.getId().toString())
        );

        tablero = repo.findByUrl(url).orElseThrow();

        assertTrue(tablero.getListas().get(0).getTarjetas().contains(tarjeta));

        assertTrue(tarjeta.isCompletada());
    }

    @Test
    void marcarTarjetaComoCompletadaFallaSiTarjetaNoExiste() {
        String url = service.crearTablero(new CrearTableroCommand("usuario@test.com"));

        assertThrows(IllegalStateException.class, () ->
                service.marcarTarjetaCompletada(
                        new MarcarTarjetaCompletadaCommand(url, "ID_INEXISTENTE"))
        );
    }
}
