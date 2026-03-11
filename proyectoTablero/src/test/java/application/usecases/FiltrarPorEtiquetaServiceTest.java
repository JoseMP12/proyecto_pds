package application.usecases;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import adapters.memory.TableroRepositoryInMemory;
import domain.model.Etiqueta;
import domain.model.Tablero;
import domain.model.Tarjeta;
import domain.ports.input.commands.CrearListaCommand;
import domain.ports.input.commands.CrearTableroCommand;
import domain.ports.input.commands.CrearTarjetaCommand;
import domain.ports.input.commands.FiltrarPorEtiquetaCommand;
import domain.ports.output.TablerosRepository;

class FiltrarPorEtiquetaServiceTest {

    private TablerosRepository repo;
    private TableroServiceImpl service;

    @BeforeEach
    void setUp() {
        repo = new TableroRepositoryInMemory();
        service = new TableroServiceImpl(repo);
    }

    @Test
    void filtrarPorEtiquetaDevuelveSoloLasTarjetasConLaEtiqueta() {
        String url = service.crearTablero(new CrearTableroCommand("usuario@test.com"));
        service.crearLista(new CrearListaCommand(url, "Pendientes", 10));

        Tablero tablero = repo.findByUrl(url).orElseThrow();
        String idPendientes = tablero.getListas().get(0).getId().toString();

        service.crearTarjeta(new CrearTarjetaCommand(url, idPendientes, "Tarea A", false));
        service.crearTarjeta(new CrearTarjetaCommand(url, idPendientes, "Tarea B", false));
        service.crearTarjeta(new CrearTarjetaCommand(url, idPendientes, "Tarea C", false));

        tablero = repo.findByUrl(url).orElseThrow();
        Tarjeta t1 = tablero.getListas().get(0).getTarjetas().get(0);
        Tarjeta t2 = tablero.getListas().get(0).getTarjetas().get(1);
        Tarjeta t3 = tablero.getListas().get(0).getTarjetas().get(2);

        t1.addEtiqueta(new Etiqueta("bug"));
        t2.addEtiqueta(new Etiqueta("feature"));
        t3.addEtiqueta(new Etiqueta("bug"));

        repo.save(tablero);

        List<Tarjeta> resultado = service.filtrarPorEtiqueta(
                new FiltrarPorEtiquetaCommand(url, "bug")
        );

        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(t1));
        assertTrue(resultado.contains(t3));
        assertFalse(resultado.contains(t2));
    }

    @Test
    void filtrarPorEtiquetaDevuelveListaVaciaSiNoHayCoincidencias() {
        String url = service.crearTablero(new CrearTableroCommand("usuario@test.com"));
        service.crearLista(new CrearListaCommand(url, "Pendientes", 10));

        Tablero tablero = repo.findByUrl(url).orElseThrow();
        String idPendientes = tablero.getListas().get(0).getId().toString();

        service.crearTarjeta(new CrearTarjetaCommand(url, idPendientes, "Tarea X", false));

        tablero = repo.findByUrl(url).orElseThrow();
        Tarjeta tarjeta = tablero.getListas().get(0).getTarjetas().get(0);
        tarjeta.addEtiqueta(new Etiqueta("otro"));
        repo.save(tablero);

        List<Tarjeta> resultado = service.filtrarPorEtiqueta(
                new FiltrarPorEtiquetaCommand(url, "bug")
        );

        assertTrue(resultado.isEmpty());
    }

    @Test
    void filtrarPorEtiquetaFallaSiTableroNoExiste() {
        assertThrows(IllegalStateException.class, () ->
                service.filtrarPorEtiqueta(
                        new FiltrarPorEtiquetaCommand("URL_INEXISTENTE", "bug"))
        );
    }
}
