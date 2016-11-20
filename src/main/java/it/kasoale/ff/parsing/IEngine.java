package it.kasoale.ff.parsing;

import it.kasoale.beans.Serie;
import org.jsoup.HttpStatusException;

import java.io.IOException;

/**
 * Created by kasoale on 13/11/2016.
 */
public interface IEngine<T> {

    public String prepareURL(String searchValue);

    public T parseHTML(String searchValue, boolean isPresent);
}

