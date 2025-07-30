package Repositories;

import java.util.Collection;

public interface IRepository<T, I> {
    Collection<T> findAll();

    void create(T object);

    T findOne(I id);

    void update(T object);

    void delete(I id);

}
