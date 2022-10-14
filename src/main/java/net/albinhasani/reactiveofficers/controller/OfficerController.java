package net.albinhasani.reactiveofficers.controller;

import net.albinhasani.reactiveofficers.dao.OfficerRepository;
import net.albinhasani.reactiveofficers.entities.Officer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/officers")
public class OfficerController {

    private final OfficerRepository officerRepository;

    public OfficerController(OfficerRepository officerRepository) {
        this.officerRepository = officerRepository;
    }

    @GetMapping
    public Flux<Officer> getAllOfficers() {
        return officerRepository.findAll();
    }

    @GetMapping("{id}")
    public Mono<Officer> getOfficer(@PathVariable String id) {
        return officerRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Officer> createOfficer(@RequestBody Officer officer) {
        return officerRepository.save(officer);
    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<Officer>> updateOfficer(@PathVariable String id, @RequestBody Officer officer) {
        return officerRepository.findById(id)
                .flatMap(existingOfficer -> {
                    existingOfficer.setRank(officer.getRank());
                    existingOfficer.setFirst(officer.getFirst());
                    existingOfficer.setLast(officer.getLast());
                    return officerRepository.save(existingOfficer);
                })
                .map(updatedOfficer -> ResponseEntity.status(HttpStatus.OK).body(updatedOfficer))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Void>> deleteOfficer(@PathVariable String id) {
        return officerRepository.deleteById(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping
    public Mono<Void> removeAllOfficers() {
        return officerRepository.deleteAll();
    }
}
