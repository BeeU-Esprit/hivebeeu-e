package edu.emmapi.interfaces;

import edu.emmapi.entities.TypeService;
import java.sql.SQLException;
import java.util.List;

public interface ITypeServiceService {
    void addTypeService(TypeService typeService) throws SQLException;
    List<TypeService> getAllTypeServices() throws SQLException;
}