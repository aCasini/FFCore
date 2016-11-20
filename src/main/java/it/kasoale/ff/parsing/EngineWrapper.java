package it.kasoale.ff.parsing;

import it.kasoale.beans.Film;
import it.kasoale.beans.SerieTV;
import it.kasoale.ff.parsing.impl.EngineSerie;
import org.apache.log4j.Logger;

import java.util.List;


/**
 * Created by kasoale on 01/11/2016.
 */
public class EngineWrapper {

    private static Logger logger = Logger.getLogger(EngineWrapper.class);


    public static List<Film> searchStreamingInfo(String filmName, boolean isPresent){
        Engine engine = new Engine();
        return engine.parseHTTP(filmName, isPresent);
    }

    public static SerieTV searchStreamingInfoSerie(String serieName, boolean isPresnet){
        EngineSerie engineSerie = new EngineSerie();
        int retry = 0;

        SerieTV serieTV = engineSerie.parseHTML(serieName, isPresnet);

        if(serieTV == null && retry < 5) {
            logger.info("Call with retry policy -- Number Retry: "+retry);
            retry ++;
            serieTV = engineSerie.parseHTML(serieName, isPresnet);
        }

        return serieTV;
    }
}
