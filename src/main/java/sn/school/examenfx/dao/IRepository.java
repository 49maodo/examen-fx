package sn.school.examenfx.dao;

import javafx.collections.ObservableList;

public interface IRepository<T> {
    public void add(T t);
    public void update(T t);
    public void delete(int id);
    public ObservableList<T> getAll();
    public T get(int id);
}
