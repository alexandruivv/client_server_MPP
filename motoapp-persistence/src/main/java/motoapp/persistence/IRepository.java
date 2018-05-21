package motoapp.persistence;

public interface IRepository<E, Id> {
    int size();
    void save(E entity);
    void update(E entity);
    void delete(Id id);
    E getOne(Id id);
    Iterable<E> findAll();
}
