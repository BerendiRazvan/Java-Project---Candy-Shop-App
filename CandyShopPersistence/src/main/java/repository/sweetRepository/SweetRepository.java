package repository.sweetRepository;


import domain.sweet.Sweet;
import repository.Repository;

import java.util.Optional;

public interface SweetRepository extends Repository<Long, Sweet> {
    Optional<Sweet> findSweetById(Long id);

    Optional<Long> generateSweetId();
}
