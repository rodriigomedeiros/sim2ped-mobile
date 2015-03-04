package br.uern.ges.med.sim2ped.beans;

import java.io.Serializable;

/**
 * Created by Rodrigo on 21/08/2014 (11:22).
 *
 *
 * Package: ${PACKAGE_NAME}
 */
@SuppressWarnings("serial")
public class HistoryResponse implements Serializable {

    private long id = 0;
    private long contextId = 0;
    private long careId = 0;
    private long routineId = 0;
    private long responseHour = 0;
    private String response = "";
    private int sentServer = 0;

    public long getId() {
        return id;
    }

    public long getContextId() {
        return contextId;
    }

    public void setContextId(long contextId) {
        this.contextId = contextId;
    }

    public long getCareId() {
        return careId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCareId(long careId) {
        this.careId = careId;
    }

    public long getRoutineId() {
        return routineId;
    }

    public void setRoutineId(long routineId) {
        this.routineId = routineId;
    }

    public long getResponseHour() {
        return responseHour;
    }

    public void setResponseHour(long responseHour) {
        this.responseHour = responseHour;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getSentServer() {
        return sentServer;
    }

    public void setSentServer(int sent){
        this.sentServer = sent;
    }
}
