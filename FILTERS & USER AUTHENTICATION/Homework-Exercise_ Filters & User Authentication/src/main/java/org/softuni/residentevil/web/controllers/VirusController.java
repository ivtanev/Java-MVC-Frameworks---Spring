package org.softuni.residentevil.web.controllers;

import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.softuni.residentevil.domain.entities.Capital;
import org.softuni.residentevil.domain.models.binding.VirusAddBindingModel;
import org.softuni.residentevil.domain.models.binding.VirusBindingModel;
import org.softuni.residentevil.domain.models.binding.VirusEditBindingModel;
import org.softuni.residentevil.domain.models.service.CapitalServiceModel;
import org.softuni.residentevil.domain.models.service.VirusServiceModel;
import org.softuni.residentevil.domain.models.view.CapitalListViewModel;
import org.softuni.residentevil.domain.models.view.VirusListViewModel;
import org.softuni.residentevil.service.CapitalService;
import org.softuni.residentevil.service.VirusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/viruses")
public class VirusController extends BaseController {

    private final CapitalService capitalService;
    private final ModelMapper modelMapper;
    private final VirusService virusService;

    @Autowired
    public VirusController(CapitalService capitalService, ModelMapper modelMapper, VirusService virusService) {
        this.capitalService = capitalService;
        this.modelMapper = modelMapper;
        this.virusService = virusService;
    }

    @GetMapping("/add")
    public ModelAndView add(ModelAndView modelAndView, @ModelAttribute(name = "bindingModel") VirusAddBindingModel bindingModel) {
        modelAndView.addObject("bindingModel", bindingModel);
        modelAndView.addObject("capitals", this.capitalService.findAllCapitals().stream()
                .map(c -> this.modelMapper.map(c, CapitalServiceModel.class))
                .collect(Collectors.toList()));

        return super.view("add-virus", modelAndView);
    }

    @PostMapping("/add")
    public ModelAndView addConfirm(@Valid @ModelAttribute(name = "bindingModel") VirusAddBindingModel bindingModel,
                                   BindingResult bindingResult, ModelAndView modelAndView) {
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("bindingModel", bindingModel);

            return super.view("add-virus", modelAndView);
        }

        VirusServiceModel virusServiceModel = this.modelMapper.map(bindingModel, VirusServiceModel.class);
        this.populateCapitals(virusServiceModel, bindingModel);
        this.virusService.spreadVirus(virusServiceModel);

//        VirusServiceModel virusServiceModel = this.virusService
//                .spreadVirus(this.modelMapper.map(bindingModel, VirusServiceModel.class));
//
//        if(virusServiceModel == null){
//            throw new IllegalArgumentException("Spread virus went wrong!");
//        }
        return super.redirect("/");
    }

    @GetMapping("/show")
    public ModelAndView showViruses(ModelAndView modelAndView) {

        List<VirusListViewModel> viruses = this.virusService.getAllViruses()
                .stream()
                .map(v -> this.modelMapper.map(v, VirusListViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("viruses", viruses);

        return super.view("show-virus", modelAndView);
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable(name = "id") String id, ModelAndView modelAndView,
                             @ModelAttribute(name = "bindingModel") VirusEditBindingModel bindingModel) {

        VirusServiceModel virusServiceModel = this.virusService.findById(id);
        modelAndView.addObject("bindingModel", virusServiceModel);

        if (virusServiceModel == null) {
            return super.view("error");
        }

        modelAndView.addObject("capitals", this.getCapitals());
        return super.view("edit", modelAndView);
    }

    @PostMapping("/edit/{id}")
    public ModelAndView edinConfirm(@PathVariable(name = "id") String id, ModelAndView modelAndView,
                                    @Valid @ModelAttribute(name = "bindingModel") VirusEditBindingModel bindingModel,
                                    BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("bindingModel", bindingModel);
            modelAndView.addObject("capitals", this.getCapitals());
            return super.view("edit", modelAndView);
        }

        VirusServiceModel virusServiceModel = this.modelMapper.map(bindingModel, VirusServiceModel.class);
        this.populateCapitals(virusServiceModel, bindingModel);
        this.virusService.editVirus(virusServiceModel);

        return super.redirect("/viruses/show");
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable(name = "id") String id, ModelAndView modelAndView) {
        this.virusService.deleteVirusById(id);
        return super.redirect("/viruses/show");
    }

    private List<CapitalListViewModel> getCapitals() {
        return this.capitalService.findAllCapitals()
                .stream()
                .map(c -> this.modelMapper.map(c, CapitalListViewModel.class))
                .collect(Collectors.toList());
    }


    private void populateCapitals(VirusServiceModel virusServiceModel, VirusBindingModel bindingModel) {
        List<Capital> capitalList = new ArrayList<>();
        for (Capital capital : bindingModel.getCapitals()) {
            Capital entity;
            try {
                entity = this.capitalService.findById(capital.getId());
            } catch (NotFoundException e) {
                continue;
            }
            capitalList.add(entity);
        }
        virusServiceModel.setCapitals(capitalList);
    }
}
