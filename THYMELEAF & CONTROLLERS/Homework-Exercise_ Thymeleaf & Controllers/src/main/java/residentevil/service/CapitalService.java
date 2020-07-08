package residentevil.service;

import javassist.NotFoundException;
import residentevil.domain.entities.Capital;
import residentevil.domain.model.service.CapitalServiceModel;

import java.util.List;

public interface CapitalService {

    List<CapitalServiceModel> findAllCapitals();

    Capital findById(String id) throws NotFoundException;
}
