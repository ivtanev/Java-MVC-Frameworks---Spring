package residentevil.domain.model.binding;

import residentevil.domain.entities.Creator;
import residentevil.domain.entities.Magnitude;
import residentevil.domain.entities.Mutation;

import java.time.LocalDate;
import java.util.List;

public class VirusEditBindingModel extends VirusBindingModel {
    private String id;

    public VirusEditBindingModel() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

