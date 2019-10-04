package hu.bme.akos.ruszkabanyai.dao.interfaces;

public interface Saver<T> {

    T save(T o);

}
