package org.softuni.residentevil.domain.models.view;

import org.softuni.residentevil.domain.entities.Magnitude;

import java.time.LocalDate;

public class VirusListViewModel {

    private String id;
    private String name;
    private Magnitude magnitude;
    private LocalDate releasedOn;

    public VirusListViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Magnitude getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(Magnitude magnitude) {
        this.magnitude = magnitude;
    }

    public LocalDate getReleasedOn() {
        return releasedOn;
    }

    public void setReleasedOn(LocalDate releasedOn) {
        this.releasedOn = releasedOn;
    }
}
