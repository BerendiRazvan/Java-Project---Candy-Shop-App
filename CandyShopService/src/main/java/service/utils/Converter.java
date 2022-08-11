package service.utils;

import service.exception.ServiceException;

public class Converter {
    public static long convertStringToLong(String number) throws ServiceException {
        try {
            return Long.parseLong(number);
        } catch (Exception e) {
            throw new ServiceException("Invalid input");
        }
    }

    public static int convertStringToInt(String number) throws ServiceException {
        try {
            return Integer.parseInt(number);
        } catch (Exception e) {
            throw new ServiceException("Invalid input");
        }
    }
}
