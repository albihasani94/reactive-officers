package net.albinhasani.reactiveofficers.controller;

import net.albinhasani.reactiveofficers.dao.OfficerRepository;
import net.albinhasani.reactiveofficers.entities.Officer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static net.albinhasani.reactiveofficers.entities.Rank.CAPTAIN;
import static net.albinhasani.reactiveofficers.entities.Rank.LIEUTENANT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OfficerControllerTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    private OfficerRepository repository;

    private final List<Officer> officers = List.of(
            new Officer(CAPTAIN, "James", "Kirk"),
            new Officer(CAPTAIN, "Jean-Luc", "Picard"),
            new Officer(CAPTAIN, "Benjamin", "Sisko"),
            new Officer(CAPTAIN, "Kathryn", "Janeway"),
            new Officer(CAPTAIN, "Jonathan", "Archer")
    );

    @BeforeEach
    public void setUp() {
        repository.deleteAll()
                .thenMany(Flux.fromIterable(officers))
                .flatMap(repository::save)
                .doOnNext(System.out::println)
                .then()
                .block();
    }

    @Test
    public void testGetAllOfficers() {
        client.get().uri("/officers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Officer.class)
                .hasSize(5)
                .consumeWith(System.out::println);
    }

    @Test
    public void testGetOfficer() {
        client.get().uri("/officers/{id}", officers.get(0).getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Officer.class)
                .consumeWith(System.out::println);
    }

    @Test
    public void testCreateOfficer() {
        Officer officer = new Officer(LIEUTENANT, "Hikaru", "Sulu");

        client.post()
                .uri("/officers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(officer), Officer.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id")
                .isNotEmpty()
                .jsonPath("$.first")
                .isEqualTo("Hikaru")
                .jsonPath("$.last")
                .isEqualTo("Sulu")
                .consumeWith(System.out::println);
    }

}
