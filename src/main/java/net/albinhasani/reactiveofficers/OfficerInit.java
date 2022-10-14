package net.albinhasani.reactiveofficers;

import net.albinhasani.reactiveofficers.dao.OfficerRepository;
import net.albinhasani.reactiveofficers.entities.Officer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

import static net.albinhasani.reactiveofficers.entities.Rank.CAPTAIN;

@Component
public class OfficerInit implements ApplicationRunner {

    private final OfficerRepository repository;

    public OfficerInit(OfficerRepository repository) {
        this.repository = repository;
    }

    private final List<Officer> officers = List.of(
            new Officer(CAPTAIN, "James", "Kirk"),
            new Officer(CAPTAIN, "Jean-Luc", "Picard"),
            new Officer(CAPTAIN, "Benjamin", "Sisko"),
            new Officer(CAPTAIN, "Kathryn", "Janeway"),
            new Officer(CAPTAIN, "Jonathan", "Archer")
    );

    @Override
    public void run(ApplicationArguments args) throws Exception {
        repository.deleteAll()
                .thenMany(Flux.fromIterable(officers))
                .flatMap(repository::save)
                .then()
                .block();
    }
}
