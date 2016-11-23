package it.kasoale.controllers;

import it.kasoale.beans.Episode;
import it.kasoale.beans.Season;
import it.kasoale.beans.SerieTV;
import it.kasoale.database.dao.impl.JDBCSerieDAO;
import it.kasoale.ff.parsing.EngineWrapper;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Produces;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kasoale on 13/11/2016.
 */
@RestController
public class SeriesController {

    private static Logger logger = Logger.getLogger(SeriesController.class);
    private SerieTV serieTV;

    @Produces("application/json")
    @RequestMapping(value = "/serie", method = RequestMethod.GET)
    public SerieTV getSerieTV(@RequestParam(value = "serieName", defaultValue = "") String serieName){

        JDBCSerieDAO dao = JDBCSerieDAO.getInstance();
        serieTV = dao.getSerieTV(serieName);
        if( serieTV != null){
            List<Season> seasons = dao.getSeasons(serieTV.getTitoloOriginale());
            serieTV.setSeasons(seasons);

            if(seasons.size() > 0){
                for (Season s : serieTV.getSeasons() ) {
                    List<Episode> episodeList = dao.getEpisodes(serieTV.getTitoloOriginale(), s.getSeasonID());
                    s.setEpisodes(episodeList);
                }
            }
        }else{

            serieTV = EngineWrapper.searchStreamingInfoSerie(serieName, true);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> {
                String threadName = Thread.currentThread().getName();
                dao.insertSerie(getSerieTV());
                for (Season s : getSerieTV().getSeasons()) {
                    dao.insertSeason(s, getSerieTV().getTitoloOriginale());
                    dao.insertEpisode(s, getSerieTV().getTitoloOriginale());
                }

                logger.info("Serie TV Stored Succesfully");
            });

        }

        return serieTV;
    }

    public SerieTV getSerieTV() {
        return serieTV;
    }

    public void setSerieTV(SerieTV serieTV) {
        this.serieTV = serieTV;
    }
}
