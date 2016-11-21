package it.kasoale.database.dao;

import it.kasoale.beans.Episode;
import it.kasoale.beans.Season;
import it.kasoale.beans.SerieTV;

import java.util.List;

/**
 * Created by kasoale on 20/11/2016.
 */
public interface SerieDAO {

    public void insertEpisode(Episode episode);
    public void insertSeason(Season season);
    public void insertSerie(SerieTV serieTV);

    public void updateEpisodeStreamingURL(Episode episode);

    public List<Episode> getEpisodes(String serieName, String seasonNumber);
    public Episode getEpisode(String serieName, String seasonNumber, String episodeNumber);

    public Season getSeason(String serieName, String sesonNumber);
    public List<Season> getSeasons(String serieName);

    public SerieTV getSerieTV(String serieName);
    public List<SerieTV> getSerieTVs();

}