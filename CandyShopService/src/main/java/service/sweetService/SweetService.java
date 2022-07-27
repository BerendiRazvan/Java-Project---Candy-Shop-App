package service.sweetService;

import domain.sweet.Sweet;
import service.exception.ServiceException;

import java.util.List;

public interface SweetService {
    List<Sweet> getAvailableSweets();

    Sweet findSweet(String sweetId) throws ServiceException;
}
