package service.sweetService;

import domain.Shop;
import domain.sweet.Sweet;
import repository.sweetsRepository.SweetRepository;
import service.exception.ServiceException;

import java.util.List;

public class SweetServiceImpl implements SweetService {

    private SweetRepository sweetRepository;

    public SweetServiceImpl(SweetRepository sweetRepository) {
        this.sweetRepository = sweetRepository;
    }

    @Override
    public List<Sweet> getAvailableSweets() {
        return sweetRepository.findAll();
    }

    @Override
    public Sweet findSweet(String sweetId) throws ServiceException {
        long id;
        try {
            id = Long.parseLong(sweetId);
        } catch (Exception e) {
            throw new ServiceException("Invalid sweet id!");
        }

        return sweetRepository.findOneSweet(id);

    }

}
