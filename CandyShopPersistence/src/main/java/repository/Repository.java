package repository;

import exception.RepositoryException;

import java.util.List;

public interface Repository<ID, T> {
    /**
     * The method will add the element with the given data
     *
     * @param elem The element to be added
     * @throws RepositoryException If there are problems when adding
     */
    void add(T elem) throws RepositoryException;

    /**
     * The method will update the element with the given id with the given data
     *
     * @param id   The id for the element to be updated
     * @param elem The element that contains the data for update
     * @throws RepositoryException If there are problems when updating
     */
    void update(ID id, T elem) throws RepositoryException;

    /**
     * The method will delete the element with the given id
     *
     * @param id The id for the element to be deleted
     * @throws RepositoryException If there are problems when deleting
     */
    void delete(ID id) throws RepositoryException;

    /**
     * The method will return all elements
     *
     * @return List of all items
     */
    List<T> findAll();
}