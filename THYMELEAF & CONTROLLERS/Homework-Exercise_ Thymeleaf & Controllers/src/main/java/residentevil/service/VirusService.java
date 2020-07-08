package residentevil.service;

import residentevil.domain.model.service.VirusServiceModel;

import java.util.List;

public interface VirusService {
    VirusServiceModel addVirus(VirusServiceModel virusServiceModel);

    List<VirusServiceModel> getAllViruses();

    VirusServiceModel findById(String id);

    VirusServiceModel editVirus(VirusServiceModel virusServiceModel);

    void deleteVirusById(String id);
}
