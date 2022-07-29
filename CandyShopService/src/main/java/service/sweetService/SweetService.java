package service.sweetService;

import domain.sweet.Sweet;
import service.exception.ServiceException;

import java.util.List;

public interface SweetService {
    List<Sweet> getAvailableSweets();

    Sweet findSweetById(String sweetId) throws ServiceException;
}
