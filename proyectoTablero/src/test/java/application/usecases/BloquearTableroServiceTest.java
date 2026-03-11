package application.usecases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import adapters.memory.TableroRepositoryInMemory;
import domain.model.EstadoTablero;
import domain.model.Tablero;
import domain.ports.input.commands.BloquearTableroCommand;
import domain.ports.input.commands.CrearTableroCommand;
import domain.ports.output.TablerosRepository;

class BloquearTableroServiceTest {

    private TablerosRepository repo;
    private TableroServiceImpl service;

    @BeforeEach
    void setUp() {
        repo = new TableroRepositoryInMemory();
        service = new TableroServiceImpl(repo);
    }

    @Test
    void bloquearTableroCambiaEstadoABloqueado() {
        // Crear tablero
        String url = service.crearTablero(new CrearTableroCommand("usuario@test.com"));

        // Ejecutar caso de uso
        service.bloquearTablero(new BloquearTableroCommand(url));

        // Recargar tablero
        Tablero tablero = repo.findByUrl(url).orElseThrow();

        // Verificar estado
        assertEquals(EstadoTablero.BLOQUEADO, tablero.getEstado());
    }

    @Test
    void bloquearTableroRegistraAccionEnHistorial() {
        String url = service.crearTablero(new CrearTableroCommand("usuario@test.com"));

        service.bloquearTablero(new BloquearTableroCommand(url));

        Tablero tablero = repo.findByUrl(url).orElseThrow();

        assertFalse(tablero.getHistorial().isEmpty());
        assertEquals("BLOQUEAR_TABLERO", tablero.getHistorial().get(0).getTipo());
    }

    @Test
    void bloquearTableroFallaSiTableroNoExiste() {
        assertThrows(IllegalStateException.class, () ->
                service.bloquearTablero(
                        new BloquearTableroCommand("URL_INEXISTENTE")));
    }
}
