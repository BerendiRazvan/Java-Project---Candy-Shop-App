package repository.sweetsRepository;


import domain.sweet.Sweet;
import repository.Repository;

public interface SweetsRepository extends Repository<Long, Sweet> {
    Sweet findOneSweet(Long id);
}
