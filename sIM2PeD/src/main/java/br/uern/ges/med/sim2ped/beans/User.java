package br.uern.ges.med.sim2ped.beans;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable{


	private long id = 0;
    private String name = "";
    private String avatarPath = "";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getFirstName(){
        String[] nameFull = name.split(" ");
        return nameFull[0];
    }

    public String getLastName(){
        String[] nameFull = name.split(" ");
        return nameFull[nameFull.length-1];
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }
}
