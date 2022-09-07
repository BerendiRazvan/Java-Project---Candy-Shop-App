package repository.sweetRepository;


import domain.sweet.Sweet;
import repository.Repository;

import java.util.Optional;

public interface SweetRepository extends Repository<Long, Sweet> {
    /**
     * The method will search for Sweet with the given id
     *
     * @param id The id for Sweet you are looking for
     * @return Optional.of(Sweet) - if Sweet is found
     * Optional.empty() - else
     */
    Optional<Sweet> findSweetById(Long id);

    /**
     * The method that will generate an id for Sweet
     *
     * @return an available id
     */
    Optional<Long> generateSweetId();
}
