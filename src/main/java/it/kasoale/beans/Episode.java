package it.kasoale.beans;

/**
 * Created by kasoale on 13/11/2016.
 */
public class Episode {

    private String title;
    private String episodeID;
    private String imageEpisode;
    private String description;
    private String streamingURL;

    public String getDescription() {
        return description;
    }

    public String getEpisodeID() {
        return episodeID;
    }

    public String getImageEpisode() {
        return imageEpisode;
    }

    public String getStreamingURL() {
        return streamingURL;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEpisodeID(String episodeID) {
        this.episodeID = episodeID;
    }

    public void setImageEpisode(String imageEpisode) {
        this.imageEpisode = imageEpisode;
    }

    public void setStreamingURL(String streamingURL) {
        this.streamingURL = streamingURL;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString(){
        return "Title Episode: "+ this.title + "\n"
                + "Espisode ID: "+ this.episodeID + "\n"
                + "Description: "+ this.description +"\n"
                + "Image: "+ this.imageEpisode + "\n"
                + "Streaming URL: "+ this.streamingURL;
    }
}
