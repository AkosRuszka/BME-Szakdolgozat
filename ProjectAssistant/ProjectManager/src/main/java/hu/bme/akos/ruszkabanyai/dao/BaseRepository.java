package hu.bme.akos.ruszkabanyai.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public abstract class BaseRepository<T> {

    @PersistenceUnit(unitName = "ogm-mongodb")
    protected EntityManagerFactory entityManagerFactory;
    protected EntityManager entityManager = entityManagerFactory.createEntityManager();

    private final Class<T> myClass;

    public BaseRepository(Class<T> _class) {
        this.myClass = _class;
    }

    protected CriteriaBuilder getCb() {
        return entityManagerFactory.createEntityManager().getCriteriaBuilder();
    }

    protected CriteriaQuery getCq() {
        return getCb().createQuery(myClass);
    }

    protected Root<T> getRoot() {
        return getCq().from(myClass);
    }

}
