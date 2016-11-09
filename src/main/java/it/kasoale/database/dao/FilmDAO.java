package it.kasoale.database.dao;

import it.kasoale.beans.Film;

import java.util.List;

/**
 * Created by kasoale on 30/10/2016.
 */
public interface FilmDAO {

    public void insertFilm(Film film);
    public Film getFilmByFilmName(String filmName);
    public List<Film> getFilmsByFilmName(String filmName);
    public void updateFilmStreaming(Film film);

}
