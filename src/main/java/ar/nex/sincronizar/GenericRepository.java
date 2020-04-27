package ar.nex.sincronizar;

/**
 *
 * @author Renzo
 */
public interface GenericRepository<T> {

    T create(T t);

    void delete(T t);

    T find(T t);

    T update(T t);

    Iterable<T> findAll();

}
