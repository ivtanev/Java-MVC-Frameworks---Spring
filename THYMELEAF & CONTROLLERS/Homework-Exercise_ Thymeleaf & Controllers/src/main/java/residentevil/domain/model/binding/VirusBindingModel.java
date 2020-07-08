package residentevil.domain.model.binding;

import org.springframework.format.annotation.DateTimeFormat;
import residentevil.domain.entities.Creator;
import residentevil.domain.entities.Magnitude;
import residentevil.domain.entities.Mutation;
import residentevil.validators.CapitalsListValidation;
import residentevil.validators.CreatorEnumValidation;
import residentevil.validators.DateValidation;
import residentevil.validators.MutationEnumValidation;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public abstract class VirusBindingModel {
    private static final String CAN_NOT_BE_EMPTY = "Invalid name! Field can not be emplty!";
    private static final String INVALID_NAME = "Invalid name! Symbols must be between 3 and 10!";
    private static final String INVALID_DESCRIPTION = "Description can not be empty!";
    private static final String INVALID_SIDE_EFFECT = "Side effect must be between 1 and 50 symbols!";
    private static final String TURNOVER_RATE_CANNOT_BE_NULL = "Turnover rate can not be empty field";
    private static final String INVALID_TURNOVER_RATE = "Turnover rate can be between 1 and 100";
    private static final String HOURS_CANNOT_BE_NULL = "Hours until turn can not be empty field";
    private static final String INVALID_HOURS_UNTIL_TURN = "Hours until turn rate can be between 1 and 12";

    private String name;
    private String description;
    private String sideEffects;
    private Creator creator;
    private Boolean isDeadly;
    private Boolean isCurable;
    private Mutation mutation;
    private Integer turnoverRate;
    private Integer hoursUntilTurn;
    private Magnitude magnitude;
    private LocalDate releasedOn;
    private List<String> capitalList;

    public VirusBindingModel() {
    }

    @NotNull(message = CAN_NOT_BE_EMPTY)
    @Size(min = 3, max = 10, message = INVALID_NAME)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotEmpty(message = INVALID_DESCRIPTION)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull
    @Size(min = 1, max = 50, message = INVALID_SIDE_EFFECT)
    public String getSideEffects() {
        return this.sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    @CreatorEnumValidation(enumClazz = Creator.class)
    public Creator getCreator() {
        return this.creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public Boolean getDeadly() {
        return this.isDeadly;
    }

    public void setDeadly(Boolean deadly) {
        isDeadly = deadly;
    }

    public Boolean getCurable() {
        return this.isCurable;
    }

    public void setCurable(Boolean curable) {
        isCurable = curable;
    }

    @MutationEnumValidation(enumClazz = Mutation.class)
    public Mutation getMutation() {
        return this.mutation;
    }

    public void setMutation(Mutation mutation) {
        this.mutation = mutation;
    }

    @NotNull(message = TURNOVER_RATE_CANNOT_BE_NULL)
    @DecimalMin(value = "1", message = INVALID_TURNOVER_RATE)
    @DecimalMax(value = "100", message = INVALID_TURNOVER_RATE)
    public Integer getTurnoverRate() {
        return this.turnoverRate;
    }

    public void setTurnoverRate(Integer turnoverRate) {
        this.turnoverRate = turnoverRate;
    }

    @NotNull(message = HOURS_CANNOT_BE_NULL)
    @DecimalMin(value = "1", message = INVALID_HOURS_UNTIL_TURN)
    @DecimalMax(value = "12", message = INVALID_HOURS_UNTIL_TURN)
    public Integer getHoursUntilTurn() {
        return this.hoursUntilTurn;
    }

    public void setHoursUntilTurn(Integer hoursUntilTurn) {
        this.hoursUntilTurn = hoursUntilTurn;
    }

    public Magnitude getMagnitude() {
        return this.magnitude;
    }

    public void setMagnitude(Magnitude magnitude) {
        this.magnitude = magnitude;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @DateValidation
    public LocalDate getReleasedOn() {
        return this.releasedOn;
    }

    public void setReleasedOn(LocalDate releasedOn) {
        this.releasedOn = releasedOn;
    }

    @CapitalsListValidation
    public List<String> getCapitalList() {
        return this.capitalList;
    }

    public void setCapitalList(List<String> capitalList) {
        this.capitalList = capitalList;
    }
}
