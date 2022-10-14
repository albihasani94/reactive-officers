package net.albinhasani.reactiveofficers.dao;

import net.albinhasani.reactiveofficers.entities.Officer;
import net.albinhasani.reactiveofficers.entities.Rank;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OfficerRepository extends ReactiveMongoRepository<Officer, String> {
    Flux<Officer> findByRank(Rank rank);
    Flux<Officer> findByLast(String last);
}
