package repository.sweetsRepository;


import domain.sweet.Sweet;
import repository.Repository;

public interface SweetRepository extends Repository<Long, Sweet> {
    Sweet findOneSweet(Long id);
}
