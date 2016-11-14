package it.kasoale.ff.parsing;

import it.kasoale.beans.Serie;

/**
 * Created by kasoale on 13/11/2016.
 */
public interface IEngine<T> {

    public String prepareURL(String searchValue);

    public T parseHTML(String searchValue, boolean isPresent);
}

