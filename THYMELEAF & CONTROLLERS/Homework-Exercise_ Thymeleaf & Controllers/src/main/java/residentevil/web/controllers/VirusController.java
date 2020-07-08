package residentevil.web.controllers;

import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import residentevil.domain.entities.Capital;
import residentevil.domain.model.binding.VirusAddBindingModel;
import residentevil.domain.model.binding.VirusBindingModel;
import residentevil.domain.model.binding.VirusEditBindingModel;
import residentevil.domain.model.service.CapitalServiceModel;
import residentevil.domain.model.service.VirusServiceModel;
import residentevil.domain.model.view.CapitalListViewModel;
import residentevil.domain.model.view.VirusEditViewModel;
import residentevil.domain.model.view.VirusListViewModel;
import residentevil.service.CapitalService;
import residentevil.service.VirusService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/viruses")
public class VirusController extends BaseController {

    private final CapitalService capitalService;
    private final VirusService virusService;
    private final ModelMapper modelMapper;

    @Autowired
    public VirusController(CapitalService capitalService, VirusService virusService, ModelMapper modelMapper) {
        this.capitalService = capitalService;
        this.virusService = virusService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    public ModelAndView add(
            @ModelAttribute(name = "bindingModel") VirusAddBindingModel bindingModel,
            ModelAndView modelAndView) {

        modelAndView.addObject("bindingModel", bindingModel);

        List<CapitalListViewModel> capitals = this.getCapitals();

        modelAndView.addObject("capitals", capitals);
        return super.view("add-virus", modelAndView);
    }

    @PostMapping("/add")
    public ModelAndView addConfirm(
            @Valid
            @ModelAttribute(name = "bindingModel") VirusAddBindingModel bindingModel,
            BindingResult bindingResult,
            ModelAndView modelAndView) {

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("bindingModel", bindingModel);
            modelAndView.addObject("capitals", this.getCapitals());
            return super.view("add-virus", modelAndView);
        }

        VirusServiceModel virusServiceModel = this.modelMapper.map(bindingModel, VirusServiceModel.class);
        this.populateCapitals(virusServiceModel, bindingModel);
        this.virusService.addVirus(virusServiceModel);

        return super.redirect("/");
    }

    @GetMapping("/show")
    public ModelAndView show(ModelAndView modelAndView) {
        List<VirusListViewModel> viruses = this.virusService.getAllViruses()
                .stream()
                .map(v -> this.modelMapper.map(v, VirusListViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("viruses", viruses);

        return super.view("all-viruses", modelAndView);
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(
            @PathVariable(name = "id") String id,
            ModelAndView modelAndView,
            @ModelAttribute(name = "bindingModel") VirusEditBindingModel bindingModel) {

        VirusServiceModel virusServiceModel = this.virusService.findById(id);

        if (virusServiceModel == null) {
            return super.view("error");
        }

        modelAndView.addObject("capitals", this.getCapitals());
        return super.view("edit", modelAndView);
    }

    @PostMapping("/edit/{id}")
    public ModelAndView edinConfirm(
            @PathVariable(name = "id") String id,
            ModelAndView modelAndView,
            @Valid
            @ModelAttribute(name = "bindingModel") VirusEditBindingModel bindingModel,
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
    public ModelAndView delete( @PathVariable(name = "id") String id, ModelAndView modelAndView){
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
        for (String capitalId : bindingModel.getCapitalList()) {
            Capital entity;
            try {
                entity = this.capitalService.findById(capitalId);
            } catch (NotFoundException e) {
                continue;
            }
            capitalList.add(entity);
        }
        virusServiceModel.setCapitalList(capitalList);
    }
}
