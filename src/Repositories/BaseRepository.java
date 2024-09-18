package Repositories;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BaseRepository<T> {
    T save(T entity) throws SQLException;
    Optional<T> findById(Long id) throws SQLException;
    List<T> findAll() throws SQLException;
    boolean update(T entity) throws SQLException;
    void delete(Long id) throws SQLException;
}
