package br.uern.ges.med.sim2ped.beans;

import java.io.Serializable;

/**
 * Created by Rodrigo on 21/08/2014 (11:21).
 *
 *
 *
 */

@SuppressWarnings("serial")
public class Context implements Serializable {

    private long id = 0;
    private String name;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

}
