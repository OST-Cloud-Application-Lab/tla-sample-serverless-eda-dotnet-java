package org.contextmapper.sample.tlas.infrastructure.webapi.events; 

import java.util.List;
import java.util.ArrayList;

public class TLAAcceptEventDto{
    private String tlaGroupName = "";
    private String tlaName = "";
    private String tlaMeaning = "";
    private List<String> tlaAlternativeMeanings = new ArrayList<>();
    private String tlaLink;

    public String getTlaGroupName() {
        return tlaGroupName;
    }

    public void setTlaGroupName(String tlaGroupName) {
        this.tlaGroupName = tlaGroupName;
    }

    public String getTlaName() {
        return tlaName;
    }

    public void setTlaName(String tlaName) {
        this.tlaName = tlaName;
    }

    public String getTlaMeaning() {
        return tlaMeaning;
    }

    public void setTlaMeaning(String tlaMeaning) {
        this.tlaMeaning = tlaMeaning;
    }

    public List<String> getTlaAlternativeMeanings() {
        return tlaAlternativeMeanings;
    }

    public void setTlaAlternativeMeanings(List<String> tlaAlternativeMeanings) {
        this.tlaAlternativeMeanings = tlaAlternativeMeanings;
    }

    public String getTlaLink() {
        return tlaLink;
    }

    public void setTlaLink(String tlaLink) {
        this.tlaLink = tlaLink;
    }
}