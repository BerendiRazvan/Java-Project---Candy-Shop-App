package repository;

import repository.exception.RepositoryException;

import java.util.List;

public interface Repository<ID, T> {
    void add(T elem) throws RepositoryException;

    void update(ID id, T elem) throws RepositoryException;

    void delete(ID id) throws RepositoryException;

    List<T> findAll();
}