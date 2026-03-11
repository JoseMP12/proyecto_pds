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
import domain.ports.output.TablerosRepository;

class CrearTarjetaServiceTest {

    private TablerosRepository repo;
    private TableroServiceImpl service;

    @BeforeEach
    void setUp() {
        repo = new TableroRepositoryInMemory();
        service = new TableroServiceImpl(repo);
    }

    @Test
    void crearTarjetaAñadeTarjetaALaLista() {
        String url = service.crearTablero(new CrearTableroCommand("usuario@test.com"));
        service.crearLista(new CrearListaCommand(url, "Pendientes", 5));

        String idLista = repo.findByUrl(url).orElseThrow()
                .getListas().get(0).getId().toString();
        CrearTarjetaCommand cmd = new CrearTarjetaCommand(url, idLista, "Hacer la compra", false);

        service.crearTarjeta(cmd);

        Tablero tablero = repo.findByUrl(url).orElseThrow();
        Tarjeta tarjeta = tablero.getListas().get(0).getTarjetas().get(0);

        assertEquals("Hacer la compra", tarjeta.getTitulo());
    }

    @Test
    void crearTarjetaChecklistSeCreaComoChecklist() {
        String url = service.crearTablero(new CrearTableroCommand("usuario@test.com"));
        service.crearLista(new CrearListaCommand(url, "Hoy", 5));

        String idLista = repo.findByUrl(url).orElseThrow()
                .getListas().get(0).getId().toString();

        CrearTarjetaCommand cmd = new CrearTarjetaCommand(url, idLista, "Lista de tareas", true);

        service.crearTarjeta(cmd);

        Tablero tablero = repo.findByUrl(url).orElseThrow();
        Tarjeta tarjeta = tablero.getListas().get(0).getTarjetas().get(0);

        assertTrue(tarjeta.isChecklist());
    }

    @Test
    void crearTarjetaRespetaElMaximoDeTarjetas() {
        String url = service.crearTablero(new CrearTableroCommand("usuario@test.com"));
        service.crearLista(new CrearListaCommand(url, "Hoy", 1));

        String idLista = repo.findByUrl(url).orElseThrow()
                .getListas().get(0).getId().toString();

        // Primera tarjeta OK
        service.crearTarjeta(new CrearTarjetaCommand(url, idLista, "T1", false));

        // Segunda tarjeta debe fallar
        assertThrows(IllegalStateException.class, () -> {
            service.crearTarjeta(new CrearTarjetaCommand(url, idLista, "T2", false));
        });
    }

    @Test
    void crearTarjetaSeGuardaEnElRepositorio() {
        String url = service.crearTablero(new CrearTableroCommand("usuario@test.com"));
        service.crearLista(new CrearListaCommand(url, "Pendientes", 5));

        String idLista = repo.findByUrl(url).orElseThrow()
                .getListas().get(0).getId().toString();

        CrearTarjetaCommand cmd = new CrearTarjetaCommand(url, idLista, "Tarea X", false);

        service.crearTarjeta(cmd);

        Tablero tablero = repo.findByUrl(url).orElseThrow();
        assertEquals(1, tablero.getListas().get(0).getTarjetas().size());
    }
}
