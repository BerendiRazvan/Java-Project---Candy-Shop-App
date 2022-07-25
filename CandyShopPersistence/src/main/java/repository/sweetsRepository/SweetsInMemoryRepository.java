package repository.sweetsRepository;

import domain.Shop;
import domain.sweet.Sweet;
import repository.exception.RepositoryException;

import java.util.List;

public class SweetsInMemoryRepository implements SweetsRepository {

    private List<Sweet> sweetsMemoryList;

    public SweetsInMemoryRepository(List<Sweet> sweetsMemoryList) {
        this.sweetsMemoryList = sweetsMemoryList;
    }

    @Override
    public void add(Sweet elem) throws RepositoryException {
        if (!sweetsMemoryList.contains(elem))
            sweetsMemoryList.add(elem);
        else
            throw new RepositoryException("This element already exists!");
    }

    @Override
    public void update(Long aLong, Sweet elem) throws RepositoryException {
        boolean exists = false;
        for (Sweet sweet : sweetsMemoryList) {
            if (sweet.getIdSweet() == elem.getIdSweet()) {
                sweetsMemoryList.set(sweetsMemoryList.indexOf(sweet), elem);
                exists = true;
                break;
            }
        }
        if (!exists)
            throw new RepositoryException("This element does not exist!");
    }

    @Override
    public void delete(Long aLong) throws RepositoryException {
        boolean exists = false;
        for (Sweet sweet : sweetsMemoryList) {
            if (sweet.getIdSweet() == aLong) {
                sweetsMemoryList.remove(sweet);
                exists = true;
                break;
            }
        }
        if (!exists)
            throw new RepositoryException("This element does not exist!");
    }

    @Override
    public List<Sweet> findAll() {
        return sweetsMemoryList;
    }

    @Override
    public Sweet findOneSweet(Long id) {
        for (Sweet sweet : sweetsMemoryList)
            if (id == sweet.getIdSweet()) return sweet;
        return null;
    }
}
