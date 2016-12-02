package it.kasoale.beans;

/**
 * Created by acasini on 02/12/16.
 */
public class Cast {

    private int castId;
    private String character;
    private String name;
    private String profilePath;

    public int getCastId() {
        return castId;
    }

    public String getCharacter() {
        return character;
    }

    public String getName() {
        return name;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setCastId(int castId) {
        this.castId = castId;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

}
