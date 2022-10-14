package net.albinhasani.reactiveofficers.dao;

import net.albinhasani.reactiveofficers.entities.Officer;
import net.albinhasani.reactiveofficers.entities.Rank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static net.albinhasani.reactiveofficers.entities.Rank.CAPTAIN;
import static net.albinhasani.reactiveofficers.entities.Rank.ENSIGN;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OfficerRepositoryTest {

    @Autowired
    private OfficerRepository officerRepository;

    private final List<Officer> officers = List.of(
            new Officer(CAPTAIN, "James", "Kirk"),
            new Officer(CAPTAIN, "Jean-Luc", "Picard"),
            new Officer(CAPTAIN, "Benjamin", "Sisko"),
            new Officer(CAPTAIN, "Kathryn", "Janeway"),
            new Officer(CAPTAIN, "Jonathan", "Archer")
    );

    @BeforeEach
    public void setUp() {
        officerRepository.deleteAll()
                .thenMany(Flux.fromIterable(officers))
                .flatMap(officerRepository::save)
                .then()
                .block();
    }

    @Test
    public void save() {
        Officer lorca = new Officer(CAPTAIN, "Gabriel", "Lorca");
        StepVerifier.create(officerRepository.save(lorca))
                .expectNextMatches(officer -> !officer.getId().equals(""))
                .verifyComplete();
    }

    @Test
    public void findAll() {
        StepVerifier.create(officerRepository.findAll().log())
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void findById() {
        officers.stream()
                .map(Officer::getId)
                .forEach(id -> StepVerifier.create(officerRepository.findById(id))
                        .expectNextCount(1)
                        .verifyComplete());
    }

    @Test
    public void findByIdNotExist() {
        StepVerifier.create(officerRepository.findById("xyz"))
                .verifyComplete();
    }

    @Test
    public void count() {
        StepVerifier.create(officerRepository.count())
                .expectNext(5L)
                .verifyComplete();
    }

    @Test
    public void findByRank() {
        StepVerifier.create(officerRepository.findByRank(CAPTAIN)
                .map(Officer::getRank)
                .distinct())
                .expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(officerRepository.findByRank(ENSIGN)
                .map(Officer::getRank)
                .distinct())
                .verifyComplete();
    }

    @Test
    public void findByLast() {
        officers.stream()
                .map(Officer::getLast)
                .forEach(last -> StepVerifier.create(officerRepository.findByLast(last))
                        .expectNextMatches(officer -> officer.getLast().equals(last))
                        .verifyComplete());
    }

}
