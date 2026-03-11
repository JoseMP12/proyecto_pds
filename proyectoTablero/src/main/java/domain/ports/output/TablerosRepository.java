package domain.ports.output;

import java.util.Optional;

import domain.model.Tablero;

public interface TablerosRepository {
	Optional<Tablero> findByUrl(String url);
    void save(Tablero tablero);
}