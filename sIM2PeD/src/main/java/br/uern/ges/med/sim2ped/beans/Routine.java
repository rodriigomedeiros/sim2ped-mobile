package br.uern.ges.med.sim2ped.beans;

import java.io.Serializable;

/**
 * Created by Rodrigo on 21/08/2014 (11:22).
 *
 *
 * Package: ${PACKAGE_NAME}
 */

@SuppressWarnings("serial")
public class Routine implements Serializable {

    private long id = 0;
    //private int weekDay = 0;
    private long time = 0;
    private long contextId = 0;
    private long userId = 0;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /*public int getWeekDay() {
        return weekDay;
    } */

    /*public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    } */

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getContextId() {
        return contextId;
    }

    public void setContextId(long contextId) {
        this.contextId = contextId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
