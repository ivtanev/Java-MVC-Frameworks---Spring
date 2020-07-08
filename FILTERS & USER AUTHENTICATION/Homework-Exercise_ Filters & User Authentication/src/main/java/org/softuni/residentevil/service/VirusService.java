package org.softuni.residentevil.service;

import org.softuni.residentevil.domain.models.service.VirusServiceModel;

import java.util.List;

public interface VirusService {

    VirusServiceModel spreadVirus(VirusServiceModel virusServiceModel);
    List<VirusServiceModel> getAllViruses();
    boolean deleteVirusById(String id);
    VirusServiceModel findById(String id);
    VirusServiceModel editVirus(VirusServiceModel virusServiceModel);

}
