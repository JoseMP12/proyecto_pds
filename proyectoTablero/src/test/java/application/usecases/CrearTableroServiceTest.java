package application.usecases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import adapters.memory.TableroRepositoryInMemory;
import domain.model.Tablero;
import domain.ports.input.commands.CrearTableroCommand;
import domain.ports.output.TablerosRepository;

class CrearTableroServiceTest {
    private TablerosRepository repo;
    private TableroServiceImpl service;

    @BeforeEach
    void setUp() {
        repo = new TableroRepositoryInMemory();
        service = new TableroServiceImpl(repo);
    }

    @Test
    void crearTableroDevuelveUrlUnica() {
        CrearTableroCommand cmd = new CrearTableroCommand("usuario@test.com");

        String url = service.crearTablero(cmd);

        assertNotNull(url);
        assertFalse(url.isBlank());
    }

    @Test
    void crearTableroGuardaElTableroEnElRepositorio() {
        CrearTableroCommand cmd = new CrearTableroCommand("usuario@test.com");

        String url = service.crearTablero(cmd);

        assertTrue(repo.findByUrl(url).isPresent());
    }

    @Test
    void tableroCreadoTieneEstadoAccesible() {
        CrearTableroCommand cmd = new CrearTableroCommand("usuario@test.com");

        String url = service.crearTablero(cmd);
        Tablero tablero = repo.findByUrl(url).orElseThrow();

        assertEquals(domain.model.EstadoTablero.ACCESIBLE, tablero.getEstado());
    }

    @Test
    void tableroCreadoTienePropietarioCorrecto() {
        String correo = "usuario@test.com";
        CrearTableroCommand cmd = new CrearTableroCommand(correo);

        String url = service.crearTablero(cmd);
        Tablero tablero = repo.findByUrl(url).orElseThrow();

        assertEquals(correo, tablero.getPropietario().getCorreo());
    }

    @Test
    void tableroCreadoEmpiezaSinListas() {
        CrearTableroCommand cmd = new CrearTableroCommand("usuario@test.com");

        String url = service.crearTablero(cmd);
        Tablero tablero = repo.findByUrl(url).orElseThrow();

        assertTrue(tablero.getListas().isEmpty());
    }

    @Test
    void tableroCreadoEmpiezaConHistorialVacio() {
        CrearTableroCommand cmd = new CrearTableroCommand("usuario@test.com");

        String url = service.crearTablero(cmd);
        Tablero tablero = repo.findByUrl(url).orElseThrow();

        assertTrue(tablero.getHistorial().isEmpty());
    }
}