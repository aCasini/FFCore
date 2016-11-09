package it.kasoale.beans;

import javax.validation.constraints.NotNull;

/**
 * Created by kasoale on 28/10/2016.
 */
public class Film {

    @NotNull
    private String filmName;
    private String imageUrl;
    private String filmUrl;
    private String streamingUrl;

    public Film(){
        super();
    }

    public Film(String filmName, String imageUrl, String filmUrl, String streamingUrl){
        this.filmName = filmName;
        this.imageUrl = imageUrl;
        this.filmUrl = filmUrl;
        this.streamingUrl = streamingUrl;
    }

    public String getFilmName() {
        return filmName;
    }

    public String getFilmUrl() {
        return filmUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getStreamingUrl() {
        return streamingUrl;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public void setFilmUrl(String filmUrl) {
        this.filmUrl = filmUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setStreamingUrl(String streamingUrl) {
        this.streamingUrl = streamingUrl;
    }

    public String toString(){
        return "Film Name: "+ filmName + "\n"
                + "Image URL: "+ imageUrl + "\n"
                + "Film URL: "+ filmUrl + "\n"
                + "Streaming URL: "+ streamingUrl + "\n";
    }

    public boolean isValid(){
        if(this.filmName == null || this.filmName == ""){
            return false;
        }else if(this.filmUrl == null || this.filmUrl == ""){
            return false;
        }else if(this.streamingUrl == null || this.streamingUrl == ""){
            return false;
        }

        return true;
    }
}
