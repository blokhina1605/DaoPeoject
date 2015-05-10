package dao;

import entity.ADaoEntity;

import java.util.List;

/**
 * Created by jecka on 10.05.2015.
 */
public abstract class AbstractDao<K, T extends ADaoEntity> {
    public abstract List<T> findAll();
    public abstract T findEntityById(K id);
    public abstract boolean delete(K id);
    public abstract boolean delete(T entity);
    public abstract boolean create(T entity);
    public abstract T update(T entity);


}
