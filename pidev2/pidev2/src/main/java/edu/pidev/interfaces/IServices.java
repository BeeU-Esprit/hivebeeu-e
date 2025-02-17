package edu.pidev.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface IServices <T>{
    public void addEntity(T t) throws SQLException;
    public void deleteEntity(T t) throws SQLException;
    public void updateEntity(T t)throws SQLException;

    public List<T> getAllData();


}
