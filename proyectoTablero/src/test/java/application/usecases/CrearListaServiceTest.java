package application.usecases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import adapters.memory.TableroRepositoryInMemory;
import domain.model.Tablero;
import domain.ports.input.commands.CrearListaCommand;
import domain.ports.input.commands.CrearTableroCommand;
import domain.ports.output.TablerosRepository;

class CrearListaServiceTest {

    private TablerosRepository repo;
    private TableroServiceImpl service;

    @BeforeEach
    void setUp() {
        repo = new TableroRepositoryInMemory();
        service = new TableroServiceImpl(repo);
    }

    @Test
    void crearListaAñadeUnaListaAlTablero() {
        String url = service.crearTablero(new CrearTableroCommand("usuario@test.com"));
        CrearListaCommand cmd = new CrearListaCommand(url, "Pendientes", 5);

        service.crearLista(cmd);

        Tablero tablero = repo.findByUrl(url).orElseThrow();
        assertEquals(1, tablero.getListas().size());
        assertEquals("Pendientes", tablero.getListas().get(0).getNombre());
    }

    @Test
    void crearListaRespetaElMaximoDeTarjetas() {
        String url = service.crearTablero(new CrearTableroCommand("usuario@test.com"));
        CrearListaCommand cmd = new CrearListaCommand(url, "Hoy", 3);

        service.crearLista(cmd);

        Tablero tablero = repo.findByUrl(url).orElseThrow();
        assertEquals(3, tablero.getListas().get(0).getMaxTarjetas());
    }
}
