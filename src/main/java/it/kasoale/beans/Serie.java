package it.kasoale.beans;

import java.util.List;

/**
 * Created by kasoale on 13/11/2016.
 */
@Deprecated
public class Serie {

    private List<Episode> episodes;
    private String serieTile;
    private String imageSerie;

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public String getSerieTile() {
        return serieTile;
    }

    public String getImageSerie() {
        return imageSerie;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    public void setSerieTile(String serieTile) {
        this.serieTile = serieTile;
    }

    public void setImageSerie(String imageSerie) {
        this.imageSerie = imageSerie;
    }

    @Override
    public String toString() {
        return "Serie Title: "+this.serieTile + "\n"
                + "#Episodes: "+this.episodes.size();
    }
}