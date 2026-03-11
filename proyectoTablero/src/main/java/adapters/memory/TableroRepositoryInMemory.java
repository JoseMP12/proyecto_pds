package adapters.memory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import domain.model.Tablero;
import domain.ports.output.TablerosRepository;

public class TableroRepositoryInMemory implements TablerosRepository {
    private final Map<String, Tablero> data = new HashMap<>();
    
    @Override
    public Optional<Tablero> findByUrl(String url) {
        return Optional.ofNullable(data.get(url));
    }

    @Override
    public void save(Tablero tablero) {
        data.put(tablero.getUrlUnica(), tablero);
    }
}
