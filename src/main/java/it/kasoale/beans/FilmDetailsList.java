package it.kasoale.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by acasini on 02/12/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilmDetailsList implements Serializable{


    private ArrayList<FilmDetails> filmsDetails;

    @JsonProperty("results")
    public ArrayList<FilmDetails> getFilmsDetails() {
        return filmsDetails;
    }

    @JsonProperty("results")
    public void setFilmsDetails(ArrayList<FilmDetails> filmsDetails) {
        this.filmsDetails = filmsDetails;
    }

    @Override
    public String toString(){
        String result = null;
        for (FilmDetails filmDetail : this.filmsDetails) {
            result += filmDetail.toString() + "\n\n";

        }

        return result;
    }
}
