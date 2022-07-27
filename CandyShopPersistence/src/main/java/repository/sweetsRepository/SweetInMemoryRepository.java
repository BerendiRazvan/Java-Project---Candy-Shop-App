package repository.sweetsRepository;

import domain.sweet.Sweet;
import repository.exception.RepositoryException;

import java.util.List;

public class SweetInMemoryRepository implements SweetRepository {

    private List<Sweet> sweetList;

    public SweetInMemoryRepository(List<Sweet> sweetList) {
        this.sweetList = sweetList;
    }

    @Override
    public void add(Sweet elem) throws RepositoryException {
        if (!sweetList.contains(elem))
            sweetList.add(elem);
        else
            throw new RepositoryException("This element already exists!");
    }

    @Override
    public void update(Long aLong, Sweet elem) throws RepositoryException {
        boolean exists = false;
        for (Sweet sweet : sweetList) {
            if (sweet.getIdSweet() == elem.getIdSweet()) {
                sweetList.set(sweetList.indexOf(sweet), elem);
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
        for (Sweet sweet : sweetList) {
            if (sweet.getIdSweet() == aLong) {
                sweetList.remove(sweet);
                exists = true;
                break;
            }
        }
        if (!exists)
            throw new RepositoryException("This element does not exist!");
    }

    @Override
    public List<Sweet> findAll() {
        return sweetList;
    }

    @Override
    public Sweet findOneSweet(Long id) {
        for (Sweet sweet : sweetList)
            if (id == sweet.getIdSweet()) return sweet;
        return null;
    }
}
