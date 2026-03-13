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
import domain.ports.input.commands.MoverTarjetaCommand;
import domain.ports.output.TablerosRepository;

class MoverTarjetaServiceTest {

    private TablerosRepository repo;
    private TableroServiceImpl service;

    @BeforeEach
    void setUp() {
        repo = new TableroRepositoryInMemory();
        service = new TableroServiceImpl(repo);
    }

    @Test
    void moverTarjetaLaMueveDeUnaListaAOtra() {
        String url = service.crearTablero(new CrearTableroCommand("usuario@test.com"));

        service.crearLista(new CrearListaCommand(url, "Pendientes", 5));
        service.crearLista(new CrearListaCommand(url, "En proceso", 5));

        // Recargar tablero para obtener IDs reales
        Tablero tablero = repo.findByUrl(url).orElseThrow();
        String idPendientes = tablero.getListas().stream()
                .filter(l -> l.getNombre().equals("Pendientes"))
                .findFirst().get().getId().toString();
        String idProceso = tablero.getListas().stream()
                .filter(l -> l.getNombre().equals("En proceso"))
                .findFirst().get().getId().toString();

        service.crearTarjeta(new CrearTarjetaCommand(url, idPendientes, "Tarea X", false));

        tablero = repo.findByUrl(url).orElseThrow();
        Tarjeta tarjeta = tablero.getListas().get(0).getTarjetas().get(0);

        service.moverTarjeta(new MoverTarjetaCommand(url, tarjeta.getId().toString(), idPendientes, idProceso));

        tablero = repo.findByUrl(url).orElseThrow();

        assertTrue(tablero.getListas().get(1).getTarjetas().contains(tarjeta));
        assertFalse(tablero.getListas().get(0).getTarjetas().contains(tarjeta));
    }

    @Test
    void moverTarjetaFallaSiLaListaDestinoEstaLlena() {
        String url = service.crearTablero(new CrearTableroCommand("usuario@test.com"));

        service.crearLista(new CrearListaCommand(url, "Pendientes", 5));
        service.crearLista(new CrearListaCommand(url, "Hecho", 0));

        Tablero tablero = repo.findByUrl(url).orElseThrow();
        String idPendientes = tablero.getListas().get(0).getId().toString();
        String idHecho = tablero.getListas().get(1).getId().toString();

        service.crearTarjeta(new CrearTarjetaCommand(url, idPendientes, "Tarea X", false));

        tablero = repo.findByUrl(url).orElseThrow();
        Tarjeta tarjeta = tablero.getListas().get(0).getTarjetas().get(0);

        assertThrows(IllegalStateException.class, () -> {
            service.moverTarjeta(new MoverTarjetaCommand(url, tarjeta.getId().toString(), idPendientes, idHecho));
        });
    }

    @Test
    void moverTarjetaFallaSiNoHaPasadoPorListasObligatorias() {
        String url = service.crearTablero(new CrearTableroCommand("usuario@test.com"));

        service.crearLista(new CrearListaCommand(url, "Pendientes", 5));
        service.crearLista(new CrearListaCommand(url, "En proceso", 5));
        service.crearLista(new CrearListaCommand(url, "Revisión", 5));

        // Recargar para obtener IDs reales
        Tablero tablero = repo.findByUrl(url).orElseThrow();
        String idPendientes = tablero.getListas().get(0).getId().toString();
        String idRevision = tablero.getListas().get(2).getId().toString();

        // Configurar listas obligatorias (DDD estricto: modificar agregado y guardar)
        tablero.getListas().get(2).añadirListaPrevia("En proceso");
        repo.save(tablero);

        service.crearTarjeta(new CrearTarjetaCommand(url, idPendientes, "Tarea X", false));

        tablero = repo.findByUrl(url).orElseThrow();
        Tarjeta tarjeta = tablero.getListas().get(0).getTarjetas().get(0);

        assertThrows(IllegalStateException.class, () -> {
            service.moverTarjeta(new MoverTarjetaCommand(url, tarjeta.getId().toString(), idPendientes, idRevision));
        });
    }

    @Test
    void moverTarjetaRegistraAccionEnHistorial() {
        String url = service.crearTablero(new CrearTableroCommand("usuario@test.com"));

        service.crearLista(new CrearListaCommand(url, "Pendientes", 5));
        service.crearLista(new CrearListaCommand(url, "En proceso", 5));

        Tablero tablero = repo.findByUrl(url).orElseThrow();
        String idPendientes = tablero.getListas().stream()
                .filter(l -> l.getNombre().equals("Pendientes"))
                .findFirst().get().getId().toString();
        String idProceso = tablero.getListas().stream()
                .filter(l -> l.getNombre().equals("En proceso"))
                .findFirst().get().getId().toString();

        service.crearTarjeta(new CrearTarjetaCommand(url, idPendientes, "Tarea X", false));

        tablero = repo.findByUrl(url).orElseThrow();
        Tarjeta tarjeta = tablero.getListas().get(0).getTarjetas().get(0);

        service.moverTarjeta(new MoverTarjetaCommand(url, tarjeta.getId().toString(), idPendientes, idProceso));

        tablero = repo.findByUrl(url).orElseThrow();

        assertFalse(tablero.getHistorial().isEmpty());
        assertEquals("MOVER_TARJETA", tablero.getHistorial().get(0).getTipo());
    }
}
