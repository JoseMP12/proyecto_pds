package application.usecases;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import adapters.memory.TableroRepositoryInMemory;
import domain.model.Accion;
import domain.model.Tablero;
import domain.ports.input.commands.CrearTableroCommand;
import domain.ports.input.commands.ObtenerHistorialCommand;
import domain.ports.output.TablerosRepository;

class ObtenerHistorialServiceTest {

    private TablerosRepository repo;
    private TableroServiceImpl service;

    @BeforeEach
    void setUp() {
        repo = new TableroRepositoryInMemory();
        service = new TableroServiceImpl(repo);
    }

    @Test
    void obtenerHistorialDevuelveAccionesRegistradas() {
        String url = service.crearTablero(new CrearTableroCommand("usuario@test.com"));

        // Registrar una acción manualmente
        Tablero tablero = repo.findByUrl(url).orElseThrow();
        tablero.registrarAccion(new Accion("TEST", "Acción de prueba"));
        repo.save(tablero);

        List<String> historial = service.obtenerHistorial(
                new ObtenerHistorialCommand(url)
        );

        assertFalse(historial.isEmpty());
        assertTrue(historial.get(0).contains("Acción de prueba"));
    }


    @Test
    void obtenerHistorialVacioSiNoHayAcciones() {
        String url = service.crearTablero(new CrearTableroCommand("usuario@test.com"));

        List<String> historial = service.obtenerHistorial(
                new ObtenerHistorialCommand(url)
        );

        assertTrue(historial.isEmpty());
    }

    @Test
    void obtenerHistorialFallaSiTableroNoExiste() {
        assertThrows(IllegalStateException.class, () ->
                service.obtenerHistorial(
                        new ObtenerHistorialCommand("URL_INEXISTENTE")));
    }
}
