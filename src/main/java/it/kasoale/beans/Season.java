package it.kasoale.beans;

import java.util.List;

/**
 * Created by kasoale on 13/11/2016.
 */
public class Season {

    private String seasonID;
    private String name;
    private String imageSeason;
    private List<Episode> episodes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageSeason() {
        return imageSeason;
    }

    public String getSeasonID() {
        return seasonID;
    }

    public void setImageSeason(String imageSeason) {
        this.imageSeason = imageSeason;
    }

    public void setSeasonID(String seasonID) {
        this.seasonID = seasonID;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    @Override
    public String toString() {
        return "Season ID: "+this.seasonID + "\n"
                + "Naem: "+ this.name + "\n"
                + "#Episodes: "+this.episodes.size();
    }
}
