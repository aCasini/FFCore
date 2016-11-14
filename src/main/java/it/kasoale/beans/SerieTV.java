package it.kasoale.beans;

import java.util.List;

/**
 * Created by kasoale on 14/11/2016.
 */
public class SerieTV {

    private String imageURL;
    private List<Season> seasons;

    private String titoloOriginale;
    private String genere;
    private String nazione;
    private String ideatore;
    private String produzione;
    private String anno;
    private String cast;

    public String getAnno() {
        return anno;
    }

    public String getCast() {
        return cast;
    }

    public String getGenere() {
        return genere;
    }

    public String getIdeatore() {
        return ideatore;
    }

    public String getNazione() {
        return nazione;
    }

    public String getProduzione() {
        return produzione;
    }

    public String getTitoloOriginale() {
        return titoloOriginale;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public void setIdeatore(String ideatore) {
        this.ideatore = ideatore;
    }

    public void setNazione(String nazione) {
        this.nazione = nazione;
    }

    public void setProduzione(String produzione) {
        this.produzione = produzione;
    }

    public void setTitoloOriginale(String titoloOriginale) {
        this.titoloOriginale = titoloOriginale;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }

    @Override
    public String toString() {
        return "Titolo Originale: "+this.titoloOriginale + "\n"
                + "Genere: "+this.genere + "\n"
                + "Nazione: "+this.nazione + "\n"
                + "Ideatore: "+this.ideatore + "\n"
                + "Produzione: "+this.produzione + "\n"
                + "Anno: "+this.anno + "\n"
                + "Cast: "+this.cast + "\n"
                + "Locandina Img Url: "+ this.imageURL;
    }
}
