package com.ishvatov.model.dao;

import com.ishvatov.model.entity.AbstractEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.criteria.*;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Abstract DAO class with default methods implementations.
 *
 * @param <U> type of the unique key.
 * @param <T> entity type.
 * @author Sergey Khvatov
 */
public abstract class AbstractDao<U, T> implements BaseDaoInterface<U, T> {

    /**
     * Current session factory object
     * that is used to manage the transactions.
     */
    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Current persistent class.
     * Class of the entity object that is used
     * in the implementation of the DAO.
     */
    private final Class<T> persistentClass;

    /**
     * Default class constructor, which
     * is used to init the criteria building system..
     */
    @SuppressWarnings("unchecked")
    protected AbstractDao() {
        this.persistentClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    /**
     * Finds entity by it's unique id.
     *
     * @param key unique key of the id.
     * @return Unique entity with this id.
     */
    @Override
    public T findByUniqueKey(U key) {
        // generate criteria
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(persistentClass);
        Root<T> root = criteriaQuery.from(persistentClass);

        // form predicate and retrieve entities from data base
        Predicate predicate = criteriaBuilder.equal(root.get(AbstractEntity.UNIQUE_KEY_COLUMN_NAME), key);
        List<T> entities = findEntities(predicate, criteriaQuery, root);

        // return result
        return entities.isEmpty() ? null : entities.get(0);
    }

    /**
     * Finds entity by it's id in the DB.
     *
     * @param id id of the entity in DB.
     * @return T object with this unique id.
     */
    public T findById(int id) {
        return findEntityById(id);
    }

    /**
     * Adds entity to the DB.
     *
     * @param entity new entity to add.
     */
    public void save(T entity) {
        saveEntity(entity);
    }

    /**
     * Deletes entity from the DB.
     *
     * @param entity entity to delete.
     */
    public void delete(T entity) {
        deleteEntity(entity);
    }

    /**
     * Deletes entity by it's id.
     *
     * @param id id of the entity.
     */
    public void deleteById(int id) {
        T entity = getSession().load(persistentClass, id);
        deleteEntity(entity);
    }

    /**
     * Finds all the entities in the DB.
     *
     * @return list with all the entities.
     */
    public List<T> findAll() {
        return findAllEntities();
    }

    /**
     * Checks if entity with such key exists in teh database.
     *
     * @param key unique key of the id.
     * @return true, if exists, false otherwise.
     */
    @Override
    public boolean exists(U key) {
        return findByUniqueKey(key) != null;
    }

    /**
     * Finds the entities that suit this predicate.
     *
     * @param predicate     boolean conditional expression.
     * @param criteriaQuery instance of the {@link CriteriaQuery} which is used
     *                      to from the query.
     * @param root          instance of the {@link Root}.
     * @return entities that suit this predicate.
     */
    public List<T> findEntities(Predicate predicate, CriteriaQuery<T> criteriaQuery, Root<T> root) {
        // create request
        criteriaQuery.select(root).where(predicate);

        // return the result
        return getSession().createQuery(criteriaQuery).getResultList();
    }

    /**
     * Deletes the entities that suit this predicate.
     *
     * @param predicate      boolean conditional expression.
     * @param criteriaDelete instance of the {@link CriteriaDelete} which is used
     *                       to from the query.
     * @param root           instance of the {@link Root}.
     */
    public void deleteEntities(Predicate predicate, CriteriaDelete<T> criteriaDelete, Root<T> root) {
        // process the request
        criteriaDelete.where(predicate);

        // start the transaction
        Transaction transaction = getSession().beginTransaction();
        getSession().createQuery(criteriaDelete).executeUpdate();
        transaction.commit();
    }

    /**
     * Finds entity in the DB by it's id.
     *
     * @param id id of the entity.
     * @return entity with the same id.
     */
    private T findEntityById(int id) {
        return getSession().get(persistentClass, id);
    }

    /**
     * Saves new entity in the DB.
     *
     * @param entity new entity to save.
     */
    private void saveEntity(T entity) {
        getSession().persist(entity);
    }

    /**
     * Deletes entity from DB.
     *
     * @param entity entity to delete.
     */
    private void deleteEntity(T entity) {
        getSession().delete(entity);
    }

    /**
     * Gets all the entities from DB.
     *
     * @return all the entities in the DB.
     */
    private List<T> findAllEntities() {
        // init criteria
        CriteriaBuilder cb = getSession().getCriteriaBuilder();
        CriteriaQuery<T> cr = cb.createQuery(persistentClass);
        Root<T> root = cr.from(persistentClass);

        // create request
        cr.select(root);

        // return the result
        return getSession().createQuery(cr).getResultList();
    }

    /**
     * Return current session.
     *
     * @return current session from sessionFactory.
     */
    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}