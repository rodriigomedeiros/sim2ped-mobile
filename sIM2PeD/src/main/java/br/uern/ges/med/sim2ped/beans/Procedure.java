package br.uern.ges.med.sim2ped.beans;

import java.io.Serializable;

/**
 * Created by Rodrigo on 01/10/2014 (14:37).
 *
 * Package: br.uern.ges.med.sim2ped.beans
 */

public class Procedure implements Serializable{
    private String contextName;
    private String titleCare;
    private String hourResponse;
    private String response;

    public Procedure() {   }

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public String getTitleCare() {
        return titleCare;
    }

    public void setTitleCare(String titleCare) {
        this.titleCare = titleCare;
    }

    public String getHourResponse() {
        return hourResponse;
    }

    public void setHourResponse(String hourResponse) {
        this.hourResponse = hourResponse;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
