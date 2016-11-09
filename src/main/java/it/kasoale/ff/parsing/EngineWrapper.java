package it.kasoale.ff.parsing;

import it.kasoale.beans.Film;

import java.util.List;

/**
 * Created by kasoale on 01/11/2016.
 */
public class EngineWrapper {

    public static List<Film> searchStreamingInfo(String filmName, boolean isPresent){
        Engine engine = new Engine();
        return engine.parseHTTP(filmName, isPresent);
    }
}
