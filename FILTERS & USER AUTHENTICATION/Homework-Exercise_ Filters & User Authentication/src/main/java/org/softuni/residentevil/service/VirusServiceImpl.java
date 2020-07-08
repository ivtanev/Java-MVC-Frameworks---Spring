package org.softuni.residentevil.service;

import org.modelmapper.ModelMapper;
import org.softuni.residentevil.domain.entities.Capital;
import org.softuni.residentevil.domain.entities.Virus;
import org.softuni.residentevil.domain.models.service.VirusServiceModel;
import org.softuni.residentevil.repository.VirusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VirusServiceImpl implements VirusService {

    private final ModelMapper modelMapper;
    private final VirusRepository virusRepository;

    @Autowired
    public VirusServiceImpl(ModelMapper modelMapper, VirusRepository virusRepository) {
        this.modelMapper = modelMapper;
        this.virusRepository = virusRepository;
    }


    @Override
    public List<VirusServiceModel> getAllViruses() {
        return this.virusRepository.findAll()
                .stream()
                .map(v -> this.modelMapper.map(v, VirusServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public VirusServiceModel spreadVirus(VirusServiceModel virusServiceModel) {
        Virus virus = this.modelMapper.map(virusServiceModel, Virus.class);
        try {
            virus = this.virusRepository.saveAndFlush(virus);
            return this.modelMapper.map(virus, VirusServiceModel.class);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteVirusById(String id) {
        try {
            this.virusRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public VirusServiceModel findById(String id) {
        Virus virus = this.virusRepository.findById(id).orElse(null);
        if (virus == null) {
            throw new IllegalArgumentException("Invalid id");
        }
//        List<String> capitalIds = virus.getCapitalList().stream().map(Capital::getId).collect(Collectors.toList());
//        virus.setCapitalList(capitalIds);
        return this.modelMapper.map(virus, VirusServiceModel.class);
    }

    @Override
    @Transactional
    public VirusServiceModel editVirus(VirusServiceModel virusServiceModel) {
        Virus entity = this.modelMapper.map(virusServiceModel, Virus.class);

        return this.modelMapper.map(this.virusRepository.saveAndFlush(entity), VirusServiceModel.class);
    }
}
