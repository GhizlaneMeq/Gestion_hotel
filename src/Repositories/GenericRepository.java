package Repositories;

import Entities.User;

import java.sql.SQLException;
import java.util.List;

public interface GenericRepository<T, ID> {
    void save(T entity) throws SQLException;
    T findById(ID id) throws SQLException;
    List<T> findAll() throws SQLException;
    void update(T entity, ID id) throws SQLException;
    void delete(ID id) throws SQLException;
}
