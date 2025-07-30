package Services;



import java.util.Collection;
public interface IService <T, I>{
    Collection<T> findAll();

    void create(T object);

    T findOne(I id);

    void update(T object);

    void delete(I id);

}
